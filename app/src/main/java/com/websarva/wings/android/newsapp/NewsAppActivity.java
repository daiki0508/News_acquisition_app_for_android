package com.websarva.wings.android.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;



import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.net.Uri.*;

public class NewsAppActivity extends AppCompatActivity {
    // リストフィル―ドの定義
    private ListView _ResultNews;
    private Uri uri;
    private String url ="";
    private String jsonStr;
    private String[] aes_data;
    // 別クラスの定義
    private CodesClass cc;
    private HttpRequestClass hrc;
    private GetJSONNewsClass gjnc;
    // Intentの定義
    private Intent intent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_app_activity);
        // リスナを設定
        _ResultNews = findViewById(R.id.result_news_text2);
        if (_ResultNews == null){
            _ResultNews = findViewById(R.id.result_news_text2_tablet);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        _ResultNews.setOnItemClickListener(new ListItemClickListener());

        // Contextの設定
        Context context = getApplicationContext();
        cc = new CodesClass(context);
        hrc = new HttpRequestClass(this);
        gjnc = new GetJSONNewsClass(this);

        // ProgressBarの初期化
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

    }

    // タップした記事のURLを保持し、Dialogを起動する関数に処理を渡す
    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?>parent, View view, int position, long id){
            Map<String,String> urlStr = (Map<String, String>) parent.getItemAtPosition(position);
            url = urlStr.get("url");
            uri = parse(url);
            SelectDialogFragment dialogFragment = new SelectDialogFragment();
            dialogFragment.show(getSupportFragmentManager(),"SelectDialogFragment");
        }
    }

    // SelectDialogFragmentクラスで渡された結果が格納される
    public void onFragmentResult(boolean selectFlag){
      //  Log.d("test3", String.valueOf(selectFlag));
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

        String outputLang = getString(R.string.app_name);
      //  Log.d("test",outputLang);

        // 検索用語に何も入力されていなかったら...
        // 言語によって処理を分岐
        if (word.equals("")){

            // CodeClassに定義してある関数の呼び出し
            cc.NoWordError(outputLang);

            // 検索用語欄に入力されたら...
        }else{
            progressBar.setVisibility(ProgressBar.VISIBLE);
            // サブクラスHttpRequestClassの独自関数(httpRequest)に処理を飛ばす
            // 第１引数にURL、第２引数に結果を表示する場所を指定
            final Handler handler = new Handler();
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // バッググラウンド処理
                    /*
                    aes_data[0]...エンコードデータ
                    aes_data[1]...秘密鍵
                    aes_data[2]...iv
                     */

                    aes_data = new String[3];
                    aes_data[0] = "IhS1F0FNEbRrhsUEtQbu3k3j3mEzgM67P+YHcxLbbbfFYf30qEO1YCcB/h3k+7IpZUmz24o0Nv+2t0J8Z05jWg==";
                    aes_data[1] = "dlZyN0dEbW1xTWtDWUhKZA==";
                    aes_data[2] = "aVPbUQDZZ7gJ2y5452rY9w==";

                    jsonStr = hrc.httpRequest(aes_data,null,0);

                    SimpleAdapter adapter = gjnc.GetJSONNews(jsonStr);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // UI更新処理
                            // ListView更新時に内部的通知を行う
                            adapter.notifyDataSetChanged();
                            // _ResultNewsにSimpleAdapterを設定
                            _ResultNews.setAdapter(adapter);
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                        }
                    });
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // メニューインフレ―ターの取得
        MenuInflater inflater = getMenuInflater();
        // オプションメニュー用.xmlファイルのをインフレート
        inflater.inflate(R.menu.menu_options_menu_list,menu);
        // 親クラスの同値メソッドを呼び出し戻り値を返却
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();

        if (itemId == R.id.WeatherScreen){
            intent = new Intent(NewsAppActivity.this,WeatherAppActivity.class);
            startActivity(intent);
            finish();
        }else if (itemId == R.id.LicenseScreen){
            intent = new Intent(this,ViewLicenseActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}