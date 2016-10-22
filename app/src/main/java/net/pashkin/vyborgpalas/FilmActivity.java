package net.pashkin.vyborgpalas;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class FilmActivity extends AppCompatActivity {
    ImageView bigImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        bigImageView = (ImageView) findViewById(R.id.bigImage);
        Intent intent=getIntent();
        String movieId=intent.getStringExtra("movieId");
        BigImageDl bigImageDl=new BigImageDl();
        bigImageDl.execute(movieId);
    }

    class BigImageDl extends AsyncTask<String, Void, Drawable> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Drawable doInBackground(String... movieId) {
            InputStream URLcontent = null;
            Drawable image=null;
            try {
                URLcontent = (InputStream) new URL("http://st.kinopoisk.ru/images/film_big/" + movieId[0] + ".jpg").getContent();
                image = Drawable.createFromStream(URLcontent, "your source link");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }
        @Override
        protected void onPostExecute(Drawable image) {
            bigImageView.setImageDrawable(image);
        }
    }
}
