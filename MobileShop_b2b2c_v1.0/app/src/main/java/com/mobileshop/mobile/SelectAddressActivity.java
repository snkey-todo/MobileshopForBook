package com.mobileshop.mobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobileshop.mobile.adapter.SelectAddressAdapter;
import com.mobileshop.mobile.model.Region;
import com.mobileshop.mobile.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawei on 5/11/15.
 */
public class SelectAddressActivity extends Activity {

    private ProgressDialog progressDialog;

    private LinearLayout provinceLayout;
    private LinearLayout cityLayout;
    private LinearLayout countyLayout;
    private LinearLayout selectAddressLayout;

    private TextView selectAddressTV;

    private ListView provinceLV;
    private ListView cityLV;
    private ListView countyLV;

    private List<Region> provinceList = new ArrayList<>();
    private List<Region> cityList = new ArrayList<>();
    private List<Region> countyList = new ArrayList<>();

    private SelectAddressAdapter provinceAdapter;
    private SelectAddressAdapter cityAdapter;
    private SelectAddressAdapter countyAdapter;

    private Region province;
    private Region city;
    private Region county;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        //地区布局
        provinceLayout = (LinearLayout) findViewById(R.id.select_list_province_layout);
        cityLayout = (LinearLayout) findViewById(R.id.select_list_city_layout);
        countyLayout = (LinearLayout) findViewById(R.id.select_list_county_layout);
        selectAddressLayout = (LinearLayout) findViewById(R.id.select_address_layout);

        selectAddressTV = (TextView) findViewById(R.id.select_address_content);

        provinceLV = (ListView) findViewById(R.id.list_province);
        provinceLV.setDividerHeight(1);
        provinceLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                province = (Region) parent.getAdapter().getItem(position);
                updateAddress();
                loadCity(province);
            }
        });

        cityLV = (ListView) findViewById(R.id.list_city);
        cityLV.setDividerHeight(1);
        cityLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = (Region) parent.getAdapter().getItem(position);
                updateAddress();
                loadCounty(city);
            }
        });

        countyLV = (ListView) findViewById(R.id.list_county);
        countyLV.setDividerHeight(1);
        countyLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                county = (Region) parent.getAdapter().getItem(position);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("province", province);
                returnIntent.putExtra("city", city);
                returnIntent.putExtra("county", county);
                setResult(RESULT_OK, returnIntent);
                finish();

            }
        });

        provinceAdapter = new SelectAddressAdapter(this, provinceList);
        provinceLV.setAdapter(provinceAdapter);

        cityAdapter = new SelectAddressAdapter(this, cityList);
        cityLV.setAdapter(cityAdapter);

        countyAdapter = new SelectAddressAdapter(this, countyList);
        countyLV.setAdapter(countyAdapter);

        //载入数据
        loadProvince();
    }

    private void updateAddress() {
        if (province == null)
            return;
        selectAddressLayout.setVisibility(View.VISIBLE);
        String address = province.getLocal_name();
        if (city != null) {
            address += city.getLocal_name();
        }
        selectAddressTV.setText(address);
    }


    /**
     * 载入省
     */
    private void loadProvince() {
        progressDialog = ProgressDialog.show(this, null, "载入中...");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        Toast.makeText(SelectAddressActivity.this, "载入失败，请您重试！", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        provinceList.clear();
                        if (msg.obj != null) {
                            List<Region> regionList = (List<Region>) msg.obj;
                            provinceList.addAll(regionList);
                        }
                        provinceAdapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };
        loadData(handler, 0);
    }

    /**
     * 载入市
     */
    private void loadCity(Region parentRegion) {
        provinceLayout.setVisibility(View.GONE);
        cityLayout.setVisibility(View.VISIBLE);

        progressDialog = ProgressDialog.show(this, null, "载入中...");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        Toast.makeText(SelectAddressActivity.this, "载入失败，请您重试！", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        cityList.clear();
                        if (msg.obj != null) {
                            List<Region> regionList = (List<Region>) msg.obj;
                            cityList.addAll(regionList);
                        }
                        cityAdapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };
        loadData(handler, parentRegion.getRegion_id());
    }

    /**
     * 载入区
     */
    private void loadCounty(Region parentRegion) {
        provinceLayout.setVisibility(View.GONE);
        cityLayout.setVisibility(View.GONE);
        countyLayout.setVisibility(View.VISIBLE);

        progressDialog = ProgressDialog.show(this, null, "载入中...");
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                switch (msg.what) {
                    case 0:
                        Toast.makeText(SelectAddressActivity.this, "载入失败，请您重试！", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        countyList.clear();
                        if (msg.obj != null) {
                            List<Region> regionList = (List<Region>) msg.obj;
                            countyList.addAll(regionList);
                        }
                        countyAdapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };
        loadData(handler, parentRegion.getRegion_id());
    }

    /**
     * 载入数据
     *
     * @param handler
     * @param parentId
     */
    private void loadData(final Handler handler, final int parentId) {
        new Thread() {
            @Override
            public void run() {
                String json = HttpUtils.getJson("/api/mobile/address!listApp.do?parentid=" + parentId);
                if ("".equals(json)) {
                    handler.sendEmptyMessage(0);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject == null) {
                        handler.sendEmptyMessage(0);
                        return;
                    }

                    if (!jsonObject.has("data")) {
                        handler.sendEmptyMessage(0);
                        return;
                    }

                    JSONArray array = jsonObject.getJSONObject("data").getJSONArray("list");
                    if (array == null) {
                        handler.sendEmptyMessage(0);
                        return;
                    }

                    List<Region> regionList = new ArrayList<Region>();
                    for (int i = 0; i < array.length(); i++) {
                        regionList.add(Region.toRegion(array.getJSONObject(i)));
                    }

                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = regionList;
                    handler.sendMessage(message);
                } catch (Exception ex) {
                    Log.e("Load Region", ex.getMessage());
                }
            }
        }.start();
    }


}
