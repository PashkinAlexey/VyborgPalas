package net.pashkin.vyborgpalas;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.dreams.DreamService;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "myLogs";
    static final int PAGE_COUNT = 7;

    ViewPager pager;
    PagerAdapter pagerAdapter;
    private static JSONObject jObj;
    private static HashMap<String,Drawable> movieImgs;
    private static int dayCount=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTextColor(ContextCompat.getColor(this, R.color.mainFont));
        pagerTabStrip.setTabIndicatorColor(ContextCompat.getColor(this, R.color.mainFont));

        String url = "http://kinopasta.ru/export/widget.php?c=913&k=ed4d7ad8af&callback=jsonp1472499720148&_=1472499720151";
        MyTask tasky=new MyTask();
        tasky.execute(url);
    }
    public static JSONObject getjObj(){
        return jObj;
    }
    public static HashMap<String,Drawable> getMovieImgs(){
        return movieImgs;
    }

    public void createFrags(){
        try {
            dayCount=jObj.getJSONObject("seanses").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            try {
                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                String inputDateStr=getjObj().getJSONObject("seanses").names().getString(position);
                Date date = inputFormat.parse(inputDateStr);
                return  outputFormat.format(date);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "Title " + position;
        }

        @Override
        public int getCount() {
            return dayCount;
        }

    }

    class MyTask extends AsyncTask<String, Void, ArrayList<Object>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Object> doInBackground(String... url) {
            JSONParser parsy = new JSONParser();
            InputStream URLcontent = null;
            JSONObject jObjDL=parsy.getJSONFromUrl(url[0]);
            HashMap<String,Drawable> filmImgs=new HashMap<String,Drawable>();
            try {
                JSONObject films=jObjDL.getJSONObject("films");
                JSONArray moInds=films.names();
                for (int i=0; i<moInds.length(); i++){
                    String filmId=moInds.getString(i);
                    URLcontent = (InputStream) new URL("http://st.kinopoisk.ru/images/film/"+filmId+".jpg").getContent();
                    Drawable image = Drawable.createFromStream(URLcontent, "your source link");
                    filmImgs.put(filmId,image);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayList<Object> result=new ArrayList<Object>();
            result.add(jObjDL);
            result.add(filmImgs);
            return result;
        }
        @Override
        protected void onPostExecute(ArrayList<Object> result) {
            jObj=(JSONObject)result.get(0);
            movieImgs=(HashMap<String,Drawable>)result.get(1);
            createFrags();
        }
    }
}