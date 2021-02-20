package com.websarva.wings.android.newsapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.util.Objects.requireNonNull;

public class HttpRequestClass extends Activity {
    private final Context con;
    private GetJSONNewsClass gjnc;
    private GetJSONWeatherClass gjwc;
    // APIキー
    private final static String YOUR_API_KEY = "YOUR_API_KEY";

    HttpRequestClass(Context context){
        this.con = context;
    }
    void httpRequest(@NotNull String url, ListView _ResultList, int api_flag) {
        // OkHttp3で通信を行う
        OkHttpClient client = new OkHttpClient();
        Request request;
        if (api_flag == 0) {
            gjnc = new GetJSONNewsClass(con);
            request = new Request.Builder()
                    .url(url)
                    .get()
                      .addHeader("x-rapidapi-host", "google-news.p.rapidapi.com")
                      .addHeader("x-rapidapi-key", YOUR_API_KEY)
                    .build();
        }else {
            gjwc = new GetJSONWeatherClass(con);
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
        }

        // 非同期処理
        client.newCall(requireNonNull(request))
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
                            gjnc.GetJSONNews(_ResultList, jsonStr);
                        } else {
                            gjwc.GetJSONWeather(jsonStr,_ResultList);
                        }
                    }
                });
    }
}
