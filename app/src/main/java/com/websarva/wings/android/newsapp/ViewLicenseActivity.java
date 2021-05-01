package com.websarva.wings.android.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ViewLicenseActivity extends AppCompatActivity {
    private String[] aes_data;
    private byte[] iv_decode = null;
    private byte[] en2 = null;
    private SecretKeySpec key;
    private byte[] bytes =null;
    private byte[] keys = null;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_license);

        aes_data = new String[3];
        aes_data[0] = "lXXCz9x9sdiXtCT9odTy24UEh6/OzRMdMn5Xp/64n8GG/zP9SOK6qRuaP9bfdADmjckSeRDn1GlJ2B1CBp0nzA==";
        aes_data[1] = "QWJiR3ZEQjNKdnE4ZHhqRw==";
        aes_data[2] = "5l2CZ8lVrd/DnJ5jngJD5Q==";

        bytes = new byte[256 / 8];
        keys = Base64.decode(aes_data[1],Base64.DEFAULT);

        for (int i = 0; i < new String(keys).length(); i++){
            if (i >= bytes.length){
                break;
            }
            bytes[i] = keys[i];
        }
        key = new SecretKeySpec(bytes,"AES");

        iv_decode = Base64.decode(aes_data[2],Base64.DEFAULT);

        try {
            IvParameterSpec ips = new IvParameterSpec(iv_decode);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,key,ips);
            en2 = cipher.doFinal(Base64.decode(aes_data[0].getBytes(StandardCharsets.UTF_8),Base64.DEFAULT));

            result = new String(en2,StandardCharsets.US_ASCII);
        }catch (Exception e){

        }

        WebView webView = findViewById(R.id.wev_view);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowFileAccessFromFileURLs(false);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(false);
        webView.getSettings().setAllowContentAccess(false);

        webView.loadUrl(result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_options_menu_list,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // itemIDを取得
        int itemId = item.getItemId();
        // Newsが選択された場合のみIntentする。
        Intent intent;

        // NewsとWeatherが選択された場合のみIntentする
        if (itemId == R.id.NewsScreen){
            intent = new Intent(this,NewsAppActivity.class);
            startActivity(intent);
            finish();
        }else if (itemId == R.id.WeatherScreen){
            intent = new Intent(this,WeatherAppActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}