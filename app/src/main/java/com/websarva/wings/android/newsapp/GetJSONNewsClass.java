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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GetJSONNewsClass extends NewsAppActivity {
    private final Context con;
    private TranslateClass tc;
    private final String API_Key = "hogehoge";
    private String titles;
    private String words;
    private SimpleAdapter adapter;

    GetJSONNewsClass(Context context) {
        this.con = context;
    }


    SimpleAdapter GetJSONNews(final String jsonStr) {
        // ループフラグ
        boolean flag = false;
        // 読み込む記事の数
        final int ListNum = 6;

        List<Map<String, String>> NewsList = new ArrayList<>();
        ProgressBar progressBar = (ProgressBar) ((Activity)con).findViewById(R.id.progressbar);
        Log.d("test", "jsonStr=" + jsonStr);
        final int[] val = {0};
        progressBar.setMax(100);
        progressBar.setProgress(val[0]);
        for (int i = 0; i < ListNum; i++) {
            try {
                JSONObject rootJSON = new JSONObject(jsonStr);
                // articlesというキー名の中のJSONを取得
                JSONArray articlesJSON = rootJSON.getJSONArray("articles");
                // 一つ格納
                JSONObject article = articlesJSON.getJSONObject(i);

                titles = "";
                words = "";
                // articlesの必要要素を変数に代入
                tc = new TranslateClass(con);
                String words_b = "";
                titles = article.getString("title");
                words_b = article.getString("link");
                String url = "https://api-ssl.bitly.com/v3/shorten?access_token=" + API_Key + "&longUrl=" + words_b;
                Log.d("url", url);
                words = tc.GetShortenedUrl(url);
                Log.d("test_words",words);
                val[0] += 100 / ListNum;
                progressBar.setProgress(val[0]);

                // ループ最後の処理のみflagをtrueにする
                if (i == ListNum - 1) {
                    flag = true;
                }
            } catch (JSONException e) {
                Log.e("eroor_getJSON", e.getMessage());
            }

            // UIスレッドを操作
            boolean finalFlag = flag;
            Map<String, String> news = new HashMap<>();
            news.put("title", titles);
            news.put("link", words);
            NewsList.add(news);

            if (finalFlag){
                    val[0] = 100 - val[0];
                    progressBar.setProgress(val[0]);
                // SimpleAdapterの第４引数を定義
                String[] from = {"title", "link"};
                // SimpleAdapterの第５引数を定義
                int[] to = {android.R.id.text1, android.R.id.text2};
                // SimpleAdapterを生成
                adapter = new SimpleAdapter(con, NewsList, android.R.layout.simple_list_item_2, from, to);
            }
        }
        return adapter;
    }
}
