package com.websarva.wings.android.newsapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import static android.net.Uri.encode;
import static android.net.Uri.parse;

public class ShareTwitterActivity extends AppCompatActivity {
    EditText sentence_input;
    private String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_twitter);

        // 遷移前の画面から情報を取得
        Intent intent = getIntent();
        url = intent.getStringExtra("uri");

        // EditText内の文字数を140文字以内の制限
        InputFilter [] filters = new InputFilter[1];
        int maxLength = 140 - url.length();
        TextView length_view = findViewById(R.id.edit_length);
        length_view.setText("入力可能文字数は"+maxLength+"です");
        filters[0] = new InputFilter.LengthFilter(maxLength);
        sentence_input = findViewById(R.id.editText_share_message);
        sentence_input.setFilters(filters);
    }

    // Tweetボタンタップ時の処理
    public void Tweet(View view){
        // EditTextから文字列を取得
        String sentence = sentence_input.getText().toString();
        Log.d("test",sentence);
        // Twitterへ遷移する前処理
        Intent intent2 = new Intent(Intent.ACTION_VIEW);
        // 入力した文字をエンコード処理
        String sentence_enc = encode(sentence);
        // 暗黙的intentのURL生成
        intent2.setData(parse("twitter://post?message="+sentence_enc+url));
        Log.d("test",sentence_enc+url);

        // Activity開始
        try {
            startActivity(intent2);
        }catch (ActivityNotFoundException ex){
            // Twitter公式アプリが端末内に存在しない場合
            // インストールを促すSnackBarを生成
           final Snackbar snackbar = Snackbar.make(view,R.string.no_tweet_app, Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.tweet_app_install, view1 -> {
                        // GooglePlayStoreに遷移する前処理
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String install_url = getString(R.string.playStore_url);
                        Log.d("test",install_url);
                        intent.setData(parse(install_url));
                        try {
                            // 遷移開始
                            startActivity(intent);
                        }catch (ActivityNotFoundException ex1){
                            // 例外処理
                            Log.e("test", ex1.getMessage());
                        }
                    });
                    // SnackBarの表示
                    snackbar.show();
        }
    }
}