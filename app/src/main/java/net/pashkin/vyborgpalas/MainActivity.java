package net.pashkin.vyborgpalas;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
        ActAsync tasky=new ActAsync(this);
        tasky.execute(url);
    }
    public static JSONObject getjObj(){
        return jObj;
    }
    public static HashMap<String,Drawable> getMovieImgs(){
        return movieImgs;
    }

    public static void setjObj(JSONObject newjObj){
        jObj=newjObj;
    }
    public static void setMovieImgs(HashMap<String,Drawable> newMovieImgs){
        movieImgs=newMovieImgs;
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

}