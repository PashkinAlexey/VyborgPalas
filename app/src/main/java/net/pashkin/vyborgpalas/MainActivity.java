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

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private int dayCount=3;
    private static JSONObject jObj=null;
    private static HashMap<String,Drawable> movieImgs=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        pager = (ViewPager) findViewById(R.id.pager);
        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip); //заголовок фрагмента
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setTextColor(ContextCompat.getColor(this, R.color.mainFont));
        pagerTabStrip.setTabIndicatorColor(ContextCompat.getColor(this, R.color.mainFont));

        String url = "http://kinopasta.ru/export/widget.php?c=913&k=ed4d7ad8af&_=1472499720151";
        ActAsync jsonDl=new ActAsync(this);
        jsonDl.execute(url);
    }

    public static void setjObj(JSONObject newjObj){
        jObj=newjObj;
    }
    public static JSONObject getjObj(){
        return jObj;
    }
    public static void setMovieImgs(HashMap<String,Drawable> newMovieImgs){
        movieImgs=newMovieImgs;
    }
    public static HashMap<String,Drawable> getMovieImgs(){
        return movieImgs;
    }

    public void createFrags() throws JSONException {
            dayCount=jObj.getJSONObject("seanses").length();
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