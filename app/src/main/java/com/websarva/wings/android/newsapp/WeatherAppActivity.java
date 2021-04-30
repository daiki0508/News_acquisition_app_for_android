package com.websarva.wings.android.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class WeatherAppActivity extends AppCompatActivity {
    private Intent intent;
    private HttpRequestClass hrc;
    private CodesClass cc;
    private int selected_id;
    private int selected_id2;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_app);

        hrc = new HttpRequestClass(this);
        cc = new CodesClass(this);

        Spinner con1_list = findViewById(R.id.search_conditions1_list);
        String[] spinnerItems = getResources().getStringArray(R.array.prefectures);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        con1_list.setAdapter(adapter);

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

    public void weatherExecute(View view){
        try {
            ListView weather_list = findViewById(R.id.weather_list);
            progressBar.setVisibility(ProgressBar.VISIBLE);

            hrc.httpRequest("https://weather.tsukumijima.net/api/forecast?city="+cc.cityCode(selected_id),1,selected_id2);
        }catch (Exception e){
            Log.e("test",e.getMessage());
        }
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