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
        JSONObject schedule = null;
        HashMap<String, Drawable> movieImgs = new HashMap<String, Drawable>(); //картино
        HashMap<String, JSONObject> movieData = new HashMap<String, JSONObject>(); //данные фильмов
        try {
            schedule = JSONParser.getSubJSONFromUrl(url[0]);
            JSONObject films = schedule.getJSONObject("films");
            JSONArray movieInds = films.names();    //массив идентификаторов фильмов
            for (int i = 0; i < movieInds.length(); i++) {  //проход по фильмам и загрузка картинок для каждого
                try {
                    String filmId = movieInds.getString(i);
                    InputStream URLcontent = (InputStream) new URL("http://st.kinopoisk.ru/images/film/" + filmId + ".jpg").getContent();
                    Drawable image = Drawable.createFromStream(URLcontent, "your source link");
                    movieImgs.put(filmId, image);
                } catch (IOException e) {
                    Log.d(TAG, "Ошибка загрузки изображения для фильма. Нет связи");
                } catch (JSONException e) {
                    Log.d(TAG, "Ошибка данных при загрузке изображения фильма");
                }
            }
            for (int i = 0; i < movieInds.length(); i++) {  //проход по фильмам и загрузка инфы для каждого
                try {
                    String filmId = movieInds.getString(i);
                    JSONObject data = JSONParser.getJSONFromUrl(url[1]+filmId);
                    movieData.put(filmId, data);
                } catch (IOException e) {
                    Log.d(TAG, "Ошибка загрузки данных для фильма. Нет связи");
                } catch (JSONException e) {
                    Log.d(TAG, "Ошибка данных фильма");
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "Ошибка загрузки данных");
        } catch (JSONException e) {
            Log.d(TAG, "Ошибка распознавания Json");
        }
        ArrayList<Object> result = new ArrayList<Object>();
        result.add(schedule);
        result.add(movieImgs);
        result.add(movieData);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(ArrayList<Object> result) {
        JSONObject schedule=(JSONObject)result.get(0);
        HashMap<String,Drawable> movieImgs=(HashMap<String, Drawable>)result.get(1);
        HashMap<String,JSONObject> movieData=(HashMap<String, JSONObject>)result.get(2);

        if (schedule!=null) {
            MainActivity.setSchedule(schedule);
        }
        if (!movieImgs.isEmpty()) {
            MainActivity.setMovieImgs(movieImgs);
        }
        if (!movieData.isEmpty()) {
            MainActivity.setMovieData(movieData);
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
