package com.websarva.wings.android.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherAppActivity extends AppCompatActivity {
    // 他クラスのコンテキストの定義
    private HttpRequestClass hrc;
    private GetJSONWeatherClass gjwc;
    private CodesClass cc;
    // リストフィールドの定義
    private ListView _ResultWeather;
    // Intentの定義
    private Intent intent;
    // その他
    private int selected_id;
    private int selected_id2;
    private String jsonStr;
    // ProgressBarの定義
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_app);

        // 他クラスのコンテキストの初期化
        hrc = new HttpRequestClass(this);
        cc = new CodesClass(this);
        gjwc = new GetJSONWeatherClass(this);

        // 地域の取得リスナの設定
        Spinner con1_list = findViewById(R.id.search_conditions1_list);
        String[] spinnerItems = getResources().getStringArray(R.array.prefectures);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        con1_list.setAdapter(adapter);

        // 天気結果の表示フィールドの初期化
        _ResultWeather = findViewById(R.id.weather_list);

        // ProgressBarの初期化
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        con1_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selected_id = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 翻訳言語の取得のリスナの設定
        Spinner choose_language = findViewById(R.id.use_langiage);
        String[] spinnerItems2 = getResources().getStringArray(R.array.use_language);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,spinnerItems2);
        choose_language.setAdapter(adapter1);

        choose_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selected_id2 = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // 検索ボタンが押された時の処理
    public void weatherExecute(View view){
            progressBar.setVisibility(ProgressBar.VISIBLE);

            // Handlerの定義
            final Handler handler = new Handler();
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // バックグラウンド処理
                    jsonStr = hrc.httpRequest(null,"https://weather.tsukumijima.net/api/forecast?city="+cc.cityCode(selected_id),1);

                    SimpleAdapter adapter = gjwc.GetJSONWeather(jsonStr,selected_id2);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // UI更新処理
                            adapter.notifyDataSetChanged();
                            _ResultWeather.setAdapter(adapter);
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                        }
                    });
                }
            });

    }

    // オプションメニューの表示処理
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // メニューインフレートの取得
        MenuInflater inflater = getMenuInflater();
        // メニュー用.xmlのインフレート
        inflater.inflate(R.menu.menu_options_menu_list,menu);
        // 親クラスの同値メソッドを返す
        return super.onCreateOptionsMenu(menu);
    }
    // オプションメニュー選択時の処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // itemIDを取得
        int itemId = item.getItemId();
        // Newsが選択された場合のみIntentする。
        if (itemId == R.id.NewsScreen){
            intent = new Intent(WeatherAppActivity.this,NewsAppActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}