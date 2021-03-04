package com.websarva.wings.android.newsapp;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;


import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetJSONWeatherClass extends Activity {
    private final Context con;
    private TranslateClass tc;
    private CodesClass cc;
    GetJSONWeatherClass(Context context){
        this.con = context;
    }

    void GetJSONWeather(final String jsonStr, ListView _ResultWeather,ProgressBar progressBar, int selected_id2){
        Log.d("test",jsonStr);
        final String[] FROM = {"prefecture","dateLabel","telop","max","min","describe"};
        final int[] TO ={R.id.get_prefecture,R.id.dateLabel,R.id.today_telop,R.id.today_max,R.id.today_min,R.id.weather_describe};
        List<Map<String,Object>> WeatherList = new ArrayList<>();

        try {
            JSONObject rootJSON = new JSONObject(jsonStr);
            JSONArray forecasts = rootJSON.getJSONArray("forecasts");
            JSONObject today = forecasts.getJSONObject(0);
            String dateLabel = today.getString("dateLabel");
            String telop = today.getString("telop");
            JSONObject temperature = today.getJSONObject("temperature");
            String max = "";
            Object max_object = temperature.get("max");
            if (max_object == JSONObject.NULL){
                max = con.getResources().getString(R.string.today_min);
            }else{
                max = temperature.getJSONObject("max").getString("celsius") +"℃";
            }
            String describe = rootJSON.getJSONObject("description").getString("text");
            String prefecture = rootJSON.getJSONObject("location").getString("prefecture");

            JSONObject tomorrow = forecasts.getJSONObject(1);
            String dateLabel_tomorrow = tomorrow.getString("dateLabel");
            String telop_tomorrow = tomorrow.getString("telop");
            JSONObject temperature_tomorrow = tomorrow.getJSONObject("temperature");
            String max_tomorrow = "";
            Object max_tomorrow_object = temperature_tomorrow.get("max");
            if (max_tomorrow_object == JSONObject.NULL){
                max_tomorrow = con.getResources().getString(R.string.today_min);
            }else{
                max_tomorrow = temperature_tomorrow.getJSONObject("max").getString("celsius") + "℃";
            }
            String min_tomorrow = "";
            Object min_tomorrow_object = temperature_tomorrow.get("min");
            if (min_tomorrow_object == JSONObject.NULL){
                min_tomorrow = con.getResources().getString(R.string.today_min);
            }else{
                min_tomorrow = temperature_tomorrow.getJSONObject("min").getString("celsius") + "℃";
            }

            JSONObject day_after_tomorrow = forecasts.getJSONObject(2);
            String dateLabel_day_after_tomorrow = day_after_tomorrow.getString("dateLabel");
            String telop_day_after_tomorrow = day_after_tomorrow.getString("telop");
            JSONObject temperature_day_after_tomorrow = day_after_tomorrow.getJSONObject("temperature");
            String max_day_after_tomorrow = "";
            Object max_day_after_tomorrow_object = temperature_day_after_tomorrow.get("max");
            if (max_day_after_tomorrow_object == JSONObject.NULL){
                max_day_after_tomorrow = con.getResources().getString(R.string.today_min);
            }else{
                max_day_after_tomorrow = temperature_tomorrow.getJSONObject("min").getString("celsius") + "℃";
            }
            String min_day_after_tomorrow = "";
            Object min_day_after_tomorrow_object = temperature_day_after_tomorrow.get("min");
            if (min_day_after_tomorrow_object == JSONObject.NULL){
                min_day_after_tomorrow = con.getResources().getString(R.string.today_min);
            }else{
                min_day_after_tomorrow = temperature_tomorrow.getJSONObject("min").getString("celsius") + "℃";
            }

            String prefecture_trans = prefecture;
            String telop_trans = telop;
            String telop_tomorrow_trans = telop_tomorrow;
            String telop_day_after_tomorrow_trans = telop_day_after_tomorrow;
            String describe_trans = describe;
            final int [] val = {0};
            if (selected_id2 !=0) {
                tc = new TranslateClass(con);
                cc = new CodesClass(con);
                progressBar.setMax(100);
                progressBar.setProgress(val[0]);
                String url = "https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/exec?text=" + prefecture + "&source=ja&target=" + cc.langCode(selected_id2);
                prefecture_trans = tc.GetTranslateWord(url);
                Log.d("url", prefecture_trans);
                val[0] += 20;
                progressBar.setProgress(val[0]);
                Log.d("url2", String.valueOf(selected_id2));
                String url2 = "https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/exec?text=" + telop + "&source=ja&target=" + cc.langCode(selected_id2);
                telop_trans = tc.GetTranslateWord(url2);
                Log.d("url", telop_trans);
                val[0] += 20;
                progressBar.setProgress(val[0]);

                String url3 = "https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/exec?text=" + describe + "&source=ja&target=" + cc.langCode(selected_id2);
                describe_trans = tc.GetTranslateWord(url3);
                Log.d("url2", describe_trans);
                val[0] += 20;
                progressBar.setProgress(val[0]);

                String url4 = "https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/exec?text=" + telop_tomorrow + "&source=ja&target=" + cc.langCode(selected_id2);
                telop_tomorrow_trans = tc.GetTranslateWord(url4);
                Log.d("url3", telop_tomorrow_trans);
                val[0] += 20;
                progressBar.setProgress(val[0]);

                String url5 = "https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/exec?text=" + telop_day_after_tomorrow + "&source=ja&target=" + cc.langCode(selected_id2);
                telop_day_after_tomorrow_trans = tc.GetTranslateWord(url5);
                Log.d("url3", telop_day_after_tomorrow_trans);
                val[0] += 20;
                progressBar.setProgress(val[0]);
            }

            Handler mainHandler = new Handler(Looper.getMainLooper());
            String finalMax = max;
            String finalMax_tomorrow = max_tomorrow;
            String finalMin_tomorrow = min_tomorrow;
            String finalMax_day_after_tomorrow = max_day_after_tomorrow;
            String finalMin_day_after_tomorrow = min_day_after_tomorrow;

            String finalPrefecture_trans = prefecture_trans;
            String finalTelop_trans = telop_trans;
            String finalDescribe_trans = describe_trans;
            String finalTelop_tomorrow_trans = telop_tomorrow_trans;
            String finalTelop_day_after_tomorrow_trans = telop_day_after_tomorrow_trans;
            mainHandler.post(() ->runOnUiThread(() ->{
                for(int i = 0; i<3; i++){
                    Map<String, Object> weather = new HashMap<>();
                    String describe_ui = "";
                    if(i==0){
                        weather.put("prefecture", finalPrefecture_trans);
                        weather.put("dateLabel","("+dateLabel+")：");
                        weather.put("telop", finalTelop_trans);
                        weather.put("max", finalMax);
                        weather.put("min",con.getResources().getString(R.string.today_min));
                        describe_ui = finalDescribe_trans;
                        weather.put("describe",describe_ui);
                        WeatherList.add(weather);
                    }else if (i==1){
                        weather.put("dateLabel","("+dateLabel_tomorrow+")：");
                        // prefectureは流用
                        weather.put("prefecture", finalPrefecture_trans);
                        weather.put("telop", finalTelop_tomorrow_trans);
                        weather.put("max", finalMax_tomorrow);
                        weather.put("min", finalMin_tomorrow);
                        describe_ui = "";
                        weather.put("describe",describe_ui);
                        WeatherList.add(weather);

                    }else{
                        weather.put("dateLabel","("+dateLabel_day_after_tomorrow+")：");
                        weather.put("prefecture", finalPrefecture_trans);
                        weather.put("telop", finalTelop_day_after_tomorrow_trans);
                        weather.put("max", finalMax_day_after_tomorrow);
                        weather.put("min", finalMin_day_after_tomorrow);
                        describe_ui = "";
                        weather.put("describe",describe_ui);
                        WeatherList.add(weather);

                        SimpleAdapter adapter = new SimpleAdapter(con,WeatherList,R.layout.row,FROM,TO);
                        adapter.notifyDataSetChanged();
                        _ResultWeather.setAdapter(adapter);
                        progressBar.setVisibility(ProgressBar.INVISIBLE);

                        val[0] = 0;
                        progressBar.setProgress(val[0]);
                    }
                }
            }));

        }catch (Exception e){
            Log.e("test",e.getMessage());
        }
    }
}
