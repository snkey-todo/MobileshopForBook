package com.ictuniv.dataanalysis;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.WindowManager;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ictuniv.dataanalysis.entity.CategoryEntity;
import com.ictuniv.dataanalysis.entity.MonthEntity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private PieChart mPieChart;
    private LineChart mChart;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    private int BUFFER_SIZE=1024;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        mPieChart = (PieChart) findViewById(R.id.pineChart);
        mChart = (LineChart) findViewById(R.id.lineChart);
        //初始化饼图
        initPieChart();
        //初始化折线图
        initLineChart();
        //获取分类数据
        getCategoryData2();
        //获取年度数据
        getYearData2();
    }

    private void initPieChart() {
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        mPieChart.setCenterTextTypeface(mTfLight);
        //mChart.setCenterText(generateCenterSpannableText());

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mPieChart.setOnChartValueSelectedListener(this);

        /**
         * 设置数据
         */
        //setPieChartData(4, 100);
        //setPieChartData();

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTypeface(mTfRegular);
        mPieChart.setEntryLabelTextSize(12f);
    }

    private void initLineChart() {
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);

        // add data
        //setLineChartData();

        mChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(mTfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
//        l.setYOffset(11f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(Color.RED);
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTypeface(mTfLight);
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(200f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(true);
    }

    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);

        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);

        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private void setPieChartData(List<CategoryEntity> datas) {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        // NOTE: The order of the entries when being added to the entries array determines their position around the
        // center of
        // the chart.
        /*for (int i = 0; i < count; i++) {
            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), mParties[i % mParties.length]));
        }*/
        float totalAmount = 0.0f;
        for (int i = 0; i < datas.size(); i++) {
            CategoryEntity entity = datas.get(i);
            totalAmount += Float.parseFloat(entity.getPayment());
        }
        for (int i = 0; i < datas.size(); i++) {
            CategoryEntity entity = datas.get(i);
            float percentage = Float.parseFloat(entity.getPayment()) / totalAmount;
            String category = entity.getCategory();
            entries.add(new PieEntry(percentage, category));
        }
      /*  entries.add(new PieEntry(40, "clothes"));
        entries.add(new PieEntry(30, "food"));
        entries.add(new PieEntry(20, "gifts"));
        entries.add(new PieEntry(10, "cookie"));*/

        PieDataSet dataSet = new PieDataSet(entries, "类别");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        /*for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);*/
        /*for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
*/
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        mPieChart.setData(data);

        // undo all highlights
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }

    private void setLineChartData(List<MonthEntity> monthDatas) {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < monthDatas.size(); i++) {
            MonthEntity entity = monthDatas.get(i);
            yVals1.add(new Entry(i, Float.parseFloat(entity.getPayment())));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "月份");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(Color.BLACK);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(true);
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);
            // set data
            mChart.setData(data);
        }
    }

    /**
     * 网络数据
     */
    private void getCategoryData() {
        String url = "http://192.168.8.11:8080/ConsumptionAnalysis/cat/payment";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("username", "tom")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String jsonString = obj.getString("data");
                            switch (obj.getInt("status")) {
                                case 0:
                                    Gson gson = new Gson();
                                    List<CategoryEntity> list = gson.fromJson(jsonString, new
                                            TypeToken<List<CategoryEntity>>() {
                                            }.getType());

                                    setPieChartData(list);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private void getCategoryData2(){
        InputStream is = null;
        try {
            is = getAssets().open("category.json");
            String json = InputStreamTOString(is);
            //解析
            JSONObject obj = new JSONObject(json);
            switch (obj.getInt("status")) {
                case 0:
                    Gson gson = new Gson();
                    String jsonString = obj.getString("data");
                    List<CategoryEntity> list = gson.fromJson(jsonString, new
                            TypeToken<List<CategoryEntity>>() {
                            }.getType());

                    setPieChartData(list);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 本地数据
     */
    private void getYearData2() {
        InputStream is = null;
        try {
            is = getAssets().open("month.json");
            String json = InputStreamTOString(is);
            //解析
            JSONObject obj = new JSONObject(json);
            switch (obj.getInt("status")) {
                case 0:
                    Gson gson = new Gson();
                    String jsonString = obj.getString("data");
                    List<MonthEntity> list = gson.fromJson(jsonString, new
                            TypeToken<List<MonthEntity>>() {
                            }.getType());
                    for(MonthEntity entity: list){
                        Log.i("MainActivity",entity.getPayment());
                    }
                    setLineChartData(list);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 将InputStream转换成String
     * @param in InputStream
     * @return String
     * @throws Exception
     *
     */
    public String InputStreamTOString(InputStream in) throws Exception{

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(),"ISO-8859-1");
    }

    /**
     * 网络数据
     */
    private void getYearData() {
        String url = "http://192.168.8.11:8080/ConsumptionAnalysis/month/payment/tom";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String jsonString = obj.getString("data");
                            switch (obj.getInt("status")) {
                                case 0:
                                    Gson gson = new Gson();
                                    List<MonthEntity> list = gson.fromJson(jsonString, new
                                            TypeToken<List<MonthEntity>>() {
                                            }.getType());
                                    setLineChartData(list);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
