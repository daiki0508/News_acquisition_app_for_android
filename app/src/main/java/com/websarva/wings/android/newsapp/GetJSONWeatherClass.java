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
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetJSONWeatherClass extends WeatherAppActivity {
    private final Context con;
    private TranslateClass tc;
    private CodesClass cc;
    private String[] todayData;
    private String[] tomorrowData;
    private String[] dayaftertomorrowData;
    private String[] transData;
    private SimpleAdapter adapter;

    GetJSONWeatherClass(Context context){
        this.con = context;
    }

    SimpleAdapter GetJSONWeather(final String jsonStr, int selected_id2) {
        Log.d("test", jsonStr);
        final String[] FROM = {"prefecture", "dateLabel", "telop", "max", "min", "describe"};
        todayData = new String[5];
        tomorrowData = new String[4];
        dayaftertomorrowData = new String[4];
        final int[] TO = {R.id.get_prefecture, R.id.dateLabel, R.id.today_telop, R.id.today_max, R.id.today_min, R.id.weather_describe};
        List<Map<String, Object>> WeatherList = new ArrayList<>();

        try {
            JSONObject rootJSON = new JSONObject(jsonStr);
            JSONArray forecasts = rootJSON.getJSONArray("forecasts");
            JSONObject today = forecasts.getJSONObject(0);
            todayData[1] = today.getString("dateLabel");
            todayData[2] = today.getString("telop");
            JSONObject temperature = today.getJSONObject("temperature");
            Object max_object = temperature.get("max");
            if (max_object == JSONObject.NULL) {
                todayData[3] = con.getResources().getString(R.string.today_min);
            } else {
                todayData[3] = temperature.getJSONObject("max").getString("celsius") + "℃";
            }
            todayData[4] = rootJSON.getJSONObject("description").getString("text");
            todayData[0] = rootJSON.getJSONObject("location").getString("prefecture");

            JSONObject tomorrow = forecasts.getJSONObject(1);
            tomorrowData[0] = tomorrow.getString("dateLabel");
            tomorrowData[1] = tomorrow.getString("telop");
            JSONObject temperature_tomorrow = tomorrow.getJSONObject("temperature");
            Object max_tomorrow_object = temperature_tomorrow.get("max");
            if (max_tomorrow_object == JSONObject.NULL) {
                tomorrowData[2] = con.getResources().getString(R.string.today_min);
            } else {
                tomorrowData[2] = temperature_tomorrow.getJSONObject("max").getString("celsius") + "℃";
            }
            Object min_tomorrow_object = temperature_tomorrow.get("min");
            if (min_tomorrow_object == JSONObject.NULL) {
                tomorrowData[3] = con.getResources().getString(R.string.today_min);
            } else {
                tomorrowData[3] = temperature_tomorrow.getJSONObject("min").getString("celsius") + "℃";
            }

            JSONObject day_after_tomorrow = forecasts.getJSONObject(2);
            dayaftertomorrowData[0] = day_after_tomorrow.getString("dateLabel");
            dayaftertomorrowData[1] = day_after_tomorrow.getString("telop");
            JSONObject temperature_day_after_tomorrow = day_after_tomorrow.getJSONObject("temperature");
            Object max_day_after_tomorrow_object = temperature_day_after_tomorrow.get("max");
            if (max_day_after_tomorrow_object == JSONObject.NULL) {
                dayaftertomorrowData[2] = con.getResources().getString(R.string.today_min);
            } else {
                dayaftertomorrowData[2] = temperature_tomorrow.getJSONObject("min").getString("celsius") + "℃";
            }
            Object min_day_after_tomorrow_object = temperature_day_after_tomorrow.get("min");
            if (min_day_after_tomorrow_object == JSONObject.NULL) {
                dayaftertomorrowData[3] = con.getResources().getString(R.string.today_min);
            } else {
                dayaftertomorrowData[3] = temperature_tomorrow.getJSONObject("min").getString("celsius") + "℃";
            }
        } catch (JSONException e) {
            Log.e("eroor_getJSON", e.getMessage());
        }
        transData = new String[5];
        /*
        0...prefecture
        1...todayTelop
        2...tomorrowTelop
        3...dayaftertomorrowTelop
        4...describe
         */
        transData[0] = todayData[0];
        transData[1] = todayData[2];
        transData[2] = tomorrowData[1];
        transData[3] = tomorrowData[1];
        transData[4] = todayData[4];
        final int[] val = {0};
        if (selected_id2 != 0) {
            tc = new TranslateClass(con);
            cc = new CodesClass(con);
            ProgressBar progressBar = (ProgressBar) ((Activity) con).findViewById(R.id.progressbar);
            progressBar.setMax(100);
            progressBar.setProgress(val[0]);
            String url = "https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/exec?text=" + todayData[0] + "&source=ja&target=" + cc.langCode(selected_id2);
            transData[0] = tc.GetTranslateWord(url);
            Log.d("url", transData[0]);
            val[0] += 20;
            progressBar.setProgress(val[0]);
            Log.d("url2", String.valueOf(selected_id2));
            String url2 = "https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/exec?text=" + todayData[2] + "&source=ja&target=" + cc.langCode(selected_id2);
            transData[1] = tc.GetTranslateWord(url2);
            Log.d("url", transData[1]);
            val[0] += 20;
            progressBar.setProgress(val[0]);

            String url3 = "https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/exec?text=" + todayData[4] + "&source=ja&target=" + cc.langCode(selected_id2);
            transData[4] = tc.GetTranslateWord(url3);
            Log.d("url2", transData[4]);
            val[0] += 20;
            progressBar.setProgress(val[0]);

            String url4 = "https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/exec?text=" + tomorrowData[1] + "&source=ja&target=" + cc.langCode(selected_id2);
            transData[2] = tc.GetTranslateWord(url4);
            Log.d("url3", transData[2]);
            val[0] += 20;
            progressBar.setProgress(val[0]);

            String url5 = "https://script.google.com/macros/s/AKfycbzZtvOvf14TaMdRIYzocRcf3mktzGgXvlFvyczo/exec?text=" + tomorrowData[1] + "&source=ja&target=" + cc.langCode(selected_id2);
            transData[3] = tc.GetTranslateWord(url5);
            Log.d("url3", transData[3]);
            val[0] += 20;
            progressBar.setProgress(val[0]);
        }

        for (int i = 0; i < 3; i++) {
            Map<String, Object> weather = new HashMap<>();
            String describe_ui;

            if (i == 0) {
                weather.put("prefecture", transData[0]);
                weather.put("dateLabel", "(" + todayData[1] + ")：");
                weather.put("telop", transData[1]);
                weather.put("max", todayData[3]);
                weather.put("min", con.getResources().getString(R.string.today_min));
                describe_ui = transData[4];
                weather.put("describe", describe_ui);
                WeatherList.add(weather);
            } else if (i == 1) {
                weather.put("dateLabel", "(" + tomorrowData[0] + ")：");
                // prefectureは流用
                weather.put("prefecture", transData[0]);
                weather.put("telop", transData[2]);
                weather.put("max", tomorrowData[2]);
                weather.put("min", tomorrowData[3]);
                describe_ui = "";
                weather.put("describe", describe_ui);
                WeatherList.add(weather);

            } else {
                weather.put("dateLabel", "(" + dayaftertomorrowData[0] + ")：");
                weather.put("prefecture", transData[0]);
                weather.put("telop", transData[3]);
                weather.put("max", dayaftertomorrowData[2]);
                weather.put("min", dayaftertomorrowData[3]);
                describe_ui = "";
                weather.put("describe", describe_ui);
                WeatherList.add(weather);

                adapter = new SimpleAdapter(con, WeatherList, R.layout.row, FROM, TO);
            }
        }
        return adapter;
    }
}
