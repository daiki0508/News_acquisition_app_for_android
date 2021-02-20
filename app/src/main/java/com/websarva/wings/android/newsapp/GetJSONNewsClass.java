package com.websarva.wings.android.newsapp;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetJSONNewsClass extends Activity {
    private final Context con;

    GetJSONNewsClass(Context context){
        this.con = context;
    }
    void GetJSONNews(ListView _ResultNews, final String jsonStr){
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

                // ループ最後の処理のみflagをtrueにする
                if (i ==ListNum-1){
                    flag=true;
                }

                // UIスレッドを操作
                Handler mainHandler = new Handler(Looper.getMainLooper());
                boolean finalFlag = flag;
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
                    if (finalFlag){
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
}
