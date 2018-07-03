package com.mobileshop.mobile;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mobileshop.mobile.adapter.AddressListAdapter;
import com.mobileshop.mobile.model.Address;
import com.mobileshop.mobile.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private final int CREATE_ADDRESS = 1;
    private final int EDIT_ADDRESS = 2;

    private int type;

    private ImageView backIV;

    private ListView addressLV;
    private AddressListAdapter addressListAdapter;
    private List<Address> addressList = new ArrayList<>();

    private LinearLayout nodataLayout;
    private LinearLayout createLayout;

    private Button newBtn;
    private Button createBtn;

    private ProgressDialog progressDialog;

    public AddressActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        type = getIntent().getIntExtra("type", 0);

        backIV = (ImageView)findViewById(R.id.title_back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nodataLayout = (LinearLayout)findViewById(R.id.layout_address_no_data);
        createLayout = (LinearLayout)findViewById(R.id.layout_address_create);

        newBtn = (Button)findViewById(R.id.button_address_new);
        newBtn.setOnClickListener(this);
        createBtn = (Button)findViewById(R.id.button_address_create);
        createBtn.setOnClickListener(this);

        addressLV = (ListView)findViewById(R.id.address_listview);
        addressLV.setDivider(null);
        addressListAdapter = new AddressListAdapter(this, addressList, type);
        addressLV.setAdapter(addressListAdapter);
        addressLV.setOnItemClickListener(this);
        if(type == 1){
            addressLV.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                public void onCreateContextMenu(ContextMenu menu, View v,
                                                ContextMenu.ContextMenuInfo menuInfo) {
                    menu.setHeaderTitle("选择操作");
                    menu.add(0, 0, 0, "删除地址");
                }
            });
        }

        loadAddress();
    }

    //给菜单项添加事件
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 0:
                progressDialog = ProgressDialog.show(this, null, "正在删除...");
                final Address address = addressList.get(menuInfo.position);
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        progressDialog.dismiss();
                        switch (msg.what) {
                            case -1:
                                Toast.makeText(AddressActivity.this, "删除失败，请您重试！", Toast.LENGTH_SHORT).show();
                                break;
                            case 0:
                                Toast.makeText(AddressActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                addressList.remove(address);
                                addressListAdapter.notifyDataSetChanged();
                                if(addressList.size() == 0){
                                    nodataLayout.setVisibility(View.VISIBLE);
                                    addressLV.setVisibility(View.GONE);
                                    createLayout.setVisibility(View.GONE);
                                }else{
                                    nodataLayout.setVisibility(View.GONE);
                                    addressLV.setVisibility(View.VISIBLE);
                                    createLayout.setVisibility(View.VISIBLE);
                                }
                                Toast.makeText(AddressActivity.this, "删除收货地址成功！", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        super.handleMessage(msg);
                    }
                };

                new Thread() {
                    @Override
                    public void run() {
                        String json = HttpUtils.getJson("/api/mobile/address!delete.do?addr_id=" + address.getAddr_id());
                        if ("".equals(json)) {
                            handler.sendEmptyMessage(-1);
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject == null) {
                                handler.sendEmptyMessage(-1);
                                return;
                            }

                            Message message = Message.obtain();
                            message.what = jsonObject.getInt("result");
                            message.obj = jsonObject.getString("message");
                            handler.sendMessage(message);
                        } catch (Exception ex) {
                            Log.e("Remove Address", ex.getMessage());
                        }
                    }
                }.start();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(AddressActivity.this, AddressModifyActivity.class), CREATE_ADDRESS);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Address address = (Address) parent.getAdapter().getItem(position);
        if(type == 0) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("address", address);
            setResult(RESULT_OK, returnIntent);
            finish();
            return;
        }

        Intent intent = new Intent(this, AddressModifyActivity.class);
        intent.putExtra("address", address);
        startActivityForResult(intent, EDIT_ADDRESS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_ADDRESS) {
            if(resultCode == Activity.RESULT_OK){
                loadAddress();
            }
            return;
        }
        if(requestCode == EDIT_ADDRESS){
            if(resultCode == Activity.RESULT_OK){
                loadAddress();
            }
            return;
        }
    }

    /**
     * 载入地址列表
     */
    private void loadAddress() {
        progressDialog = ProgressDialog.show(this, null, "载入中...");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case -1:
                        Toast.makeText(AddressActivity.this, "载入失败，请您重试！", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:
                        startActivity(new Intent(AddressActivity.this, LoginActivity.class));
                        Toast.makeText(AddressActivity.this, "未登录或登录已过期，请重新登录！", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        if (msg.obj != null) {
                            addressList.clear();
                            List<Address> addresses = (List<Address>) msg.obj;
                            if(addresses.size() > 0) {
                                nodataLayout.setVisibility(View.GONE);
                                addressLV.setVisibility(View.VISIBLE);
                                createLayout.setVisibility(View.VISIBLE);
                                addressList.addAll(addresses);
                                addressListAdapter.notifyDataSetChanged();
                                return;
                            }
                        }
                        nodataLayout.setVisibility(View.VISIBLE);
                        addressLV.setVisibility(View.GONE);
                        createLayout.setVisibility(View.GONE);
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/address!list.do");
                if ("".equals(json)) {
                    handler.sendEmptyMessage(-1);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject == null) {
                        handler.sendEmptyMessage(-1);
                        return;
                    }

                    if (!jsonObject.has("data")) {
                        handler.sendEmptyMessage(-1);
                        return;
                    }

                    if(jsonObject.getInt("result") == 0){
                        handler.sendEmptyMessage(0);
                        return;
                    }

                    JSONArray array = jsonObject.getJSONObject("data").getJSONArray("addressList");
                    if (array == null) {
                        handler.sendEmptyMessage(-1);
                        return;
                    }

                    List<Address> addresses = new ArrayList<Address>();
                    for (int i = 0; i < array.length(); i++) {
                        addresses.add(Address.toAddress(array.getJSONObject(i)));
                    }

                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = addresses;
                    handler.sendMessage(message);
                } catch (Exception ex) {
                    Log.e("Load Addresses", ex.getMessage());
                }
            }
        }.start();
    }



}
