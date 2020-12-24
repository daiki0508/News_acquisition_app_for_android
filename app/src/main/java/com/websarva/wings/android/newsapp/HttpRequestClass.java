package com.websarva.wings.android.newsapp;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpRequestClass extends Activity {
    private final Context con;
    // ループフラフ
    private boolean flag = false;
    // 読み込む記事の数
    private final int ListNum = 6;

    HttpRequestClass(Context context){
        this.con = context;
    }
    void httpRequest(String url, ListView _ResultNews) throws IOException {
        // OkHttp3で通信を行う
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                //   .get()
                //   .addHeader("x-rapidapi-host", "google-news.p.rapidapi.com")
                //   .addHeader("x-rapidapi-key", "YOUR_API_KEY")
                .build();

        // 非同期処理
        client.newCall(request)
                .enqueue(new Callback() {
                    // 失敗時の処理
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("test",e.getMessage());
                    }

                    // 成功時の処理
                    // レスポンスデータを処理
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        // JSONデータを取得する
                        final String jsonStr = response.body().string();
                        // Listにより画面表示を行う準備
                        List<Map<String,String>> NewsList = new ArrayList<>();

                        Log.d("test","jsonStr="+jsonStr);
                        for (int i=0; i < ListNum; i++){
                            try {
                                JSONObject rootJSON = new JSONObject(jsonStr);
                                // articlesというキー名の中のJSONを取得
                                JSONArray articlesJSON = rootJSON.getJSONArray("articles");
                                // 一つ格納
                                JSONObject article = articlesJSON.getJSONObject(i);

                                // ループ最後の処理のみflagをtrueにする
                                if (i ==ListNum-1){
                                    flag=true;
                                }

                                // UIスレッドを操作
                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                mainHandler.post(() -> runOnUiThread(() -> {
                                    String titles = "";
                                    String words = "";
                                    try {
                                        // articlesの必要要素を変数に代入
                                        titles = article.getString("title");
                                        words = article.getString("link");
                                    }catch (Exception ex){
                                        Log.e("test", ex.getMessage());
                                    }

                                    Log.d("test",titles);
                                    Map<String,String> news = new HashMap<>();
                                    news.put("title",titles);
                                    news.put("link",words);
                                    NewsList.add(news);
                                    if (flag){
                                        // SimpleAdapterの第４引数を定義
                                        String[] from = {"title","link"};
                                        // SimpleAdapterの第５引数を定義
                                        int[] to = {android.R.id.text1,android.R.id.text2};
                                        // SimpleAdapterを生成
                                        SimpleAdapter adapter = new SimpleAdapter(con,NewsList, android.R.layout.simple_list_item_2,from,to);
                                        // ListView更新時に内部的通知を行う
                                        adapter.notifyDataSetChanged();
                                        // _ResultNewsにSimpleAdapterを設定
                                        _ResultNews.setAdapter(adapter);
                                    }
                                }));
                            }catch (Exception ex){
                                Log.e("test",ex.getMessage());
                            }
                        }
                    }
                });
    }
}
