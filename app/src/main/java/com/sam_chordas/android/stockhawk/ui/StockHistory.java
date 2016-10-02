package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StockHistory extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd", Locale.ENGLISH);
        String endDate = df.format(c.getTime());
        c.add(Calendar.MONTH, -3);
        String startDate= df.format(c.getTime());
     //   Log.e("here",formattedDate);
        Intent intent=getIntent();
        String symbol= intent.getStringExtra("Symbol");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22"+symbol+"%22%20and%20startDate%20%3D%20%2220"+startDate+"%22%20and%20endDate%20%3D%20%2220"+endDate+"%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                   // Log.e("this",responseData);
                    JSONObject query= json.getJSONObject("query");
                    JSONObject result=query.getJSONObject("results");
                    JSONArray quote= result.getJSONArray("quote");
                    float[] price = new float[quote.length()];
                    String[] labels=new String[quote.length()];
                    for(int i=0;i<quote.length();i++){
                        JSONObject res=quote.getJSONObject(quote.length()-i-1);
                        price[i]=Float.parseFloat(res.getString("Close"));
                       // Log.e("cled",price[i]);
                        labels[i]=""+i+1;
                    }

                    //final String owner = json.getString("name");
                    LineChartView mChart;
                    mChart = (LineChartView) findViewById(R.id.linechart);
                    LineSet dataset = new LineSet(labels, price);
                    mChart.addData(dataset);
                    mChart.show();
                }
                catch (JSONException e) {

                }
            }

        });

    }

}
