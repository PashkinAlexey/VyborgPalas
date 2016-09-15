package net.pashkin.vyborgpalas;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

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

    public ActAsync(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Object> doInBackground(String... url) {
        JSONParser parsy = new JSONParser();
        InputStream URLcontent = null;
        JSONObject jObjDL = parsy.getJSONFromUrl(url[0]);
        HashMap<String, Drawable> filmImgs = new HashMap<String, Drawable>();
        try {
            JSONObject films = jObjDL.getJSONObject("films");
            JSONArray moInds = films.names();
            for (int i = 0; i < moInds.length(); i++) {
                String filmId = moInds.getString(i);
                URLcontent = (InputStream) new URL("http://st.kinopoisk.ru/images/film/" + filmId + ".jpg").getContent();
                Drawable image = Drawable.createFromStream(URLcontent, "your source link");
                filmImgs.put(filmId, image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<Object> result = new ArrayList<Object>();
        result.add(jObjDL);
        result.add(filmImgs);
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<Object> result) {
        MainActivity.setjObj((JSONObject) result.get(0));
        MainActivity.setMovieImgs((HashMap<String, Drawable>) result.get(1));
        mainActivity.createFrags();
    }
}
