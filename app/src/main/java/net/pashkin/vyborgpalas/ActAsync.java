package net.pashkin.vyborgpalas;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Алексей on 16.09.2016.
 */
class ActAsync extends AsyncTask<String, Void, ArrayList<Object>> {

    private MainActivity mainActivity;
    static final String TAG = "myLogs";

    public ActAsync(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Object> doInBackground(String... url) {
        JSONObject jObjDL = null;
        HashMap<String, Drawable> movieImgs = null; //картино
        try {
            jObjDL = JSONParser.getJSONFromUrl(url[0]);
            try {
                movieImgs = new HashMap<String, Drawable>();
                JSONObject films = jObjDL.getJSONObject("films");
                JSONArray movieInds = films.names();    //массив идентификаторов фильмов
                for (int i = 0; i < movieInds.length(); i++) { //прохож по фильмам и загрузка картинок для каждого
                    String filmId = movieInds.getString(i);
                    InputStream URLcontent = (InputStream) new URL("http://st.kinopoisk.ru/images/film/" + filmId + ".jpg").getContent();
                    Drawable image = Drawable.createFromStream(URLcontent, "your source link");
                    movieImgs.put(filmId, image);
                }
            } catch (IOException e) {
                Log.d(TAG, "Ошибка загрузки изображений для фильмов");
            }
        } catch (IOException e) {
            Log.d(TAG, "Ошибка загрузки данных");
        } catch (JSONException e) {
            Log.d(TAG, "Ошибка распознавания Json");
        }
        ArrayList<Object> result = new ArrayList<Object>();
        result.add(jObjDL);
        result.add(movieImgs);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(ArrayList<Object> result) {
        JSONObject jObj;
        HashMap<String,Drawable> movieImgs;
        jObj=(JSONObject)result.get(0);
        movieImgs=(HashMap<String, Drawable>)result.get(1);

        if (jObj!=null) {
            MainActivity.setjObj(jObj);
        }
        if (movieImgs!=null) {
            MainActivity.setMovieImgs(movieImgs);
        }

        try {
            mainActivity.createFrags();
        } catch (JSONException e) {
            Toast toast = Toast.makeText(mainActivity, "Ошибка данных", Toast.LENGTH_SHORT);
            toast.show();
        } catch (NullPointerException e){
            Toast toast = Toast.makeText(mainActivity, "Ошибка загрузки", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
