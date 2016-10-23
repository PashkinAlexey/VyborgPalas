package net.pashkin.vyborgpalas;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
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
    private JSONObject movieData;
    private TextView movieNameRu;
    private TextView description;
    private TextView country;
    private TextView genre;
    private TextView duration;
    private TextView director;
    private TextView cast;
    private TextView producer;

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
        movieNameRu = (TextView) findViewById(R.id.movieNameRu);
        description = (TextView) findViewById(R.id.description);
        country = (TextView) findViewById(R.id.country);
        genre = (TextView) findViewById(R.id.genre);
        duration = (TextView) findViewById(R.id.duration);
        director = (TextView) findViewById(R.id.director);
        cast = (TextView) findViewById(R.id.cast);
        producer = (TextView) findViewById(R.id.producer);

        Intent intent=getIntent();
        String movieId=intent.getStringExtra("movieId");
        BigImageDl bigImageDl=new BigImageDl();
        bigImageDl.execute(movieId);
        getData(intent);
        setData();
    }

    private void getData(Intent intent){
        try {
            movieData=new JSONObject(intent.getStringExtra("movieData"));
        } catch (JSONException e) {
            Log.d(TAG, "Ошибка данных Интента");
            Toast toast = Toast.makeText(this,"Ошибка данных", Toast.LENGTH_SHORT);
            toast.show();
            this.finish();
        }
    }

    private void setData(){
        try {
            movieNameRu.setText(movieData.getString("nameRU"));
            description.setText(movieData.getString("description"));
            country.setText(movieData.getString("country"));
            genre .setText(movieData.getString("genre"));
            duration.setText(movieData.getString("filmLength"));
            JSONArray directors=(JSONArray)movieData.getJSONArray("creators").get(0); //режиссеры
            JSONArray actors=(JSONArray)movieData.getJSONArray("creators").get(1); //актеры
            JSONArray producers=(JSONArray)movieData.getJSONArray("creators").get(2); //продюссер
            director.setText(parseCreators(directors));
            cast.setText(parseCreators(actors));
            producer.setText(parseCreators(producers));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String parseCreators(JSONArray creators) throws JSONException {
        String creatorStr="";
        for (int i=0; i<creators.length(); i++){
            JSONObject currCreator= (JSONObject) creators.get(i);
            if (i>0) {creatorStr+=", ";}
            creatorStr+=currCreator.getString("nameRU");
        }
        return creatorStr;
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
