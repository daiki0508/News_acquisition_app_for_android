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

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GetJSONNewsClass extends Activity {
    private final Context con;
    private TranslateClass tc;
    private final String API_Key = "YOUR_API_KEY";

    GetJSONNewsClass(Context context){
        this.con = context;
    }
    void GetJSONNews(ListView _ResultNews, final String jsonStr, ProgressBar progressBar){
        // ループフラグ
        boolean flag = false;
        // 読み込む記事の数
        final int ListNum = 6;

        List<Map<String,String>> NewsList = new ArrayList<>();
        Log.d("test","jsonStr="+jsonStr);
        for (int i=0; i < ListNum; i++){
            try {
                JSONObject rootJSON = new JSONObject(jsonStr);
                // articlesというキー名の中のJSONを取得
                JSONArray articlesJSON = rootJSON.getJSONArray("articles");
                // 一つ格納
                JSONObject article = articlesJSON.getJSONObject(i);

                String titles = "";
                String words = "";
                final int[] val = {0};
                progressBar.setMax(100);
                progressBar.setProgress(val[0]);
                try {
                    // articlesの必要要素を変数に代入
                    tc = new TranslateClass(con);
                    String words_b ="";
                    titles = article.getString("title");
                    words_b = article.getString("link");
                    String url = "https://api-ssl.bitly.com/v3/shorten?access_token=" + API_Key + "&longUrl=" + words_b;
                    Log.d("url",url);
                    words = tc.GetShortenedUrl(url);
                    val[0] += 100 / ListNum;
                    progressBar.setProgress(val[0]);
                }catch (Exception ex){
                    Log.e("test", ex.getMessage());
                }

                // ループ最後の処理のみflagをtrueにする
                if (i ==ListNum-1){
                    flag=true;
                }

                // UIスレッドを操作
                Handler mainHandler = new Handler(Looper.getMainLooper());
                boolean finalFlag = flag;
                String finalTitles = titles;
                String finalWords = words;
                mainHandler.post(() -> runOnUiThread(() -> {

                    Log.d("test", finalTitles);
                    Map<String,String> news = new HashMap<>();
                    news.put("title", finalTitles);
                    news.put("link", finalWords);
                    NewsList.add(news);
                    if (finalFlag){
                        val[0] = 100 - val[0];
                        progressBar.setProgress(val[0]);
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
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                        val[0] = 0;
                        progressBar.setProgress(val[0]);
                    }
                }));
            }catch (Exception ex){
                Log.e("test",ex.getMessage());
            }
        }
    }
}
