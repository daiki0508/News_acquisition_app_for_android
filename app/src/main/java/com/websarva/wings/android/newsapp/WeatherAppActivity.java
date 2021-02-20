package com.websarva.wings.android.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import android.widget.Spinner;

public class WeatherAppActivity extends AppCompatActivity {
    private Intent intent;
    private HttpRequestClass hrc;
    private CodesClass cc;
    private int selected_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_app);

        Context context = getApplicationContext();
        hrc = new HttpRequestClass(context);
        cc = new CodesClass(context);

        Spinner con1_list = findViewById(R.id.search_conditions1_list);
        String[] spinnerItems = getResources().getStringArray(R.array.prefectures);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        con1_list.setAdapter(adapter);

        con1_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selected_id = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void weatherExecute(View view){
        try {
            ListView weather_list = findViewById(R.id.weather_list);
            hrc.httpRequest("https://weather.tsukumijima.net/api/forecast?city="+cc.cityCode(selected_id),weather_list,1);
      //      Log.d("test",area);
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