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
    static final String TAG = "myLogs";
    ImageView bigImageView;

    /*String description="";
    String description="";
    String description="";
    String description="";
    String description="";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        bigImageView = (ImageView) findViewById(R.id.bigImage);
        Intent intent=getIntent();
        String movieId=intent.getStringExtra("movieId");
        BigImageDl bigImageDl=new BigImageDl();
        bigImageDl.execute(movieId);
        getData(intent);
    }

    private void getData(Intent intent){
        try {
            JSONObject movieData=new JSONObject(intent.getStringExtra("movieData"));
        } catch (JSONException e) {
            Log.d(TAG, "Ошибка данных Интента");
            Toast toast = Toast.makeText(this,"Ошибка данных", Toast.LENGTH_SHORT);
            toast.show();
            this.finish();
        }
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
            if (image!=null)bigImageView.setImageDrawable(image);
        }
    }
}
