package net.pashkin.vyborgpalas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class FilmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        WebView webby = (WebView) findViewById(R.id.webView);
        Intent intent=getIntent();
        String summary=intent.getStringExtra("htmlData");
        webby.loadDataWithBaseURL(null, summary,"text/html", "UTF-8", null);
    }
}
