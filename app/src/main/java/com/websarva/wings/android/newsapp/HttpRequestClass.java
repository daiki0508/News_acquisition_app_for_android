package com.websarva.wings.android.newsapp;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.util.Objects.requireNonNull;

public class HttpRequestClass{
    private ProgressBar progressBar;
    private GetJSONWeatherClass gjwc;
    private GetJSONNewsClass gjnc;
    private String jsonStr;

    // APIキー
    private final static String YOUR_API_KEY = "hogehoge";

    HttpRequestClass(Context context){
    }
    String httpRequest(@NotNull String url, int api_flag,int selected_id2) {
        // OkHttp3で通信を行う
        OkHttpClient client = new OkHttpClient();
        Request request;
        if (api_flag == 0) {
            try {
                //  gjnc = new GetJSONNewsClass(con);
                request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("x-rapidapi-host", "google-news.p.rapidapi.com")
                        .addHeader("x-rapidapi-key", YOUR_API_KEY)
                        .build();

                //  progressBar = (ProgressBar) ((com.websarva.wings.android.newsapp.NewsAppActivity) con).findViewById(R.id.progressbar);

                Response response = client.newCall(request).execute();
                jsonStr = response.body().string();
                //     gjnc.GetJSONNews(_ResultList, jsonStr, progressBar);
            } catch (IOException e) {
                Log.e("eroor_news", e.getMessage());
            }

        } else {
            try {
                //    gjwc = new GetJSONWeatherClass(con);
                request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                //     progressBar = (ProgressBar) ((com.websarva.wings.android.newsapp.WeatherAppActivity) con).findViewById(R.id.progressbar);
                Response response = client.newCall(request).execute();
                jsonStr = response.body().string();
            } catch (IOException e) {
                Log.e("eroor_news", e.getMessage());
            }
        }


        // 非同期処理
        /*client.newCall(requireNonNull(request))
                .enqueue(new Callback() {
                    // 失敗時の処理
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("test", e.getMessage());
                    }

                    // 成功時の処理
                    // レスポンスデータを処理
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        // JSONデータを取得する
                        final String jsonStr = requireNonNull(response.body()).string();
                        if (api_flag == 0) {
                            gjnc.GetJSONNews(_ResultList, jsonStr,progressBar);
                        } else {
                            gjwc.GetJSONWeather(jsonStr,_ResultList,progressBar,selected_id2);
                        }
                    }
                });*/
        return jsonStr;
    }
}
