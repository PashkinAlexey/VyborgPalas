package net.pashkin.vyborgpalas;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

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
        InputStream URLcontent = null;
        JSONObject jObjDL = null;
        HashMap<String, Drawable> filmImgs = new HashMap<String, Drawable>();
        try {
            jObjDL = JSONParser.getJSONFromUrl(url[0]);
            try {
                JSONObject films = jObjDL.getJSONObject("films");
                JSONArray movieInds = films.names();
                for (int i = 0; i < movieInds.length(); i++) {
                    String filmId = movieInds.getString(i);
                    URLcontent = (InputStream) new URL("http://st.kinopoisk.ru/images/film/" + filmId + ".jpg").getContent();
                    Drawable image = Drawable.createFromStream(URLcontent, "your source link");
                    filmImgs.put(filmId, image);
                }
            } catch (IOException e) {
                Log.d(TAG, "ПЕРВВЫЙ");
            }
        } catch (IOException e) {
            Log.d(TAG, "ВТОРОЙ");
        } catch (JSONException e) {
            Log.d(TAG, "ТРЕТИЙ");
        }
        ArrayList<Object> result = new ArrayList<Object>();
        result.add(jObjDL);
        result.add(filmImgs);
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<Object> result) {
        mainActivity.setjObj((JSONObject) result.get(0));
        mainActivity.setMovieImgs((HashMap<String, Drawable>) result.get(1));
        mainActivity.createFrags();
    }
}
