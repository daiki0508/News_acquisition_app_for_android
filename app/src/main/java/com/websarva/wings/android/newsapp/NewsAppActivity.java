package com.websarva.wings.android.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.Map;

import static android.net.Uri.*;

public class NewsAppActivity extends AppCompatActivity {
    // リストフィル―ドの定義
    ListView _ResultNews;
    private Uri uri;
    private String url ="";
    // 別クラスの定義
    private CodesClass cc;
    private HttpRequestClass hrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_app_activity);
        // リスナを設定
        _ResultNews = findViewById(R.id.result_news_text2);
        _ResultNews.setOnItemClickListener(new ListItemClickListener());

        // Contextの設定
        Context context = getApplicationContext();
        cc = new CodesClass(context);
        Context context1 = getApplicationContext();
        hrc = new HttpRequestClass(context1);

    }

    // タップした記事のURLを保持し、Dialogを起動する関数に処理を渡す
    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?>parent, View view, int position, long id){
            Map<String,String> urlStr = (Map<String, String>) parent.getItemAtPosition(position);
            url = urlStr.get("link");
            uri = parse(url);
            SelectDialogFragment dialogFragment = new SelectDialogFragment();
            dialogFragment.show(getSupportFragmentManager(),"SelectDialogFragment");
        }
    }

    // SelectDialogFragmentクラスで渡された結果が格納される
    public void onFragmentResult(boolean selectFlag){
        Log.d("test3", String.valueOf(selectFlag));
        Intent intent;
        // flagの値によって処理を分岐
        if (selectFlag){
            intent = new Intent(Intent.ACTION_VIEW, uri);
        }else{
            intent = new Intent(NewsAppActivity.this, ShareTwitterActivity.class);
            intent.putExtra("uri",url);
        }
        startActivity(intent);
    }

    // ボタンタップ時の処理
    public void executeButton(View view){
        EditText input = findViewById(R.id.search_conditions3_edit);
        String word = input.getText().toString();
        // プルダウンリストのアイテムを取得
        Spinner con1_list = findViewById(R.id.search_conditions1_list);
        String area = (String) con1_list.getSelectedItem();
        Spinner con2_list = findViewById(R.id.search_conditions2_list);
        String lang = (String) con2_list.getSelectedItem();
        String [] codes;
        String outputLang = getString(R.string.app_name);
        Log.d("test",outputLang);

        // サブクラスCodesClassのCode関数に処理を渡す
        codes = cc.Code(outputLang,area,lang);

        // 検索用語に何も入力されていなかったら...
        // 言語によって処理を分岐
        if (word.equals("")){

            // CodeClassに定義してある関数の呼び出し
            cc.NoWordError(outputLang);

            // 検索用語欄に入力されたら...
        }else{
            try {
                // サブクラスHttpRequestClassの独自関数(httpRequest)に処理を飛ばす
                // 第１引数にURL、第２引数に結果を表示する場所を指定
                hrc.httpRequest("https://google-news.p.rapidapi.com/v1/topic_headlines?country="+codes[0]+"&lang="+codes[1]+"&topic="+word,_ResultNews);
                Log.d("test",codes[0]);
                Log.d("test",codes[1]);
            }catch (Exception e){
                Log.e("test",e.getMessage());
            }
        }
    }
}