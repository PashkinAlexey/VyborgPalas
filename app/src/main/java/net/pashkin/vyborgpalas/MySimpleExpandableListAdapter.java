package net.pashkin.vyborgpalas;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Алексей on 12.09.2016.
 */
public class MySimpleExpandableListAdapter extends SimpleExpandableListAdapter {
    static final String TAG = "myLogs";

    private List<? extends List<? extends Map<String, ?>>> mChildData; //ОБЪЯВЛЕНИЕ массива данных для ПОДСПИСКОВ
    private String[] mImgFrom;                                       //ОБЪЯВЛЕНИЕ массива данных с индексами ПОДСПИСКОВ
    private int[] mImgTo;                                            //ОБЪЯВЛЕНИЕ массива с вьюхами для ПОДСПИСКОВ

    private List<? extends Map<String, ?>> mGroupData;                 //ОБЪЯВЛЕНИЕ массива данных для СПИСКА
    private String[] mMovieImgFrom;                                       //ОБЪЯВЛЕНИЕ массива данных с индексом изображений СПИСКА
    private int[] mMovieImgTo;                                            //ОБЪЯВЛЕНИЕ массива с вьюхами изображений для СПИСКА
    private String[] mGenreFrom;                                       //ОБЪЯВЛЕНИЕ массива данных с индексом жанра СПИСКА
    private int[] mGenreTo;                                            //ОБЪЯВЛЕНИЕ массива с вьюхами жанра для СПИСКА

    public MySimpleExpandableListAdapter(Context context,
                                         List<? extends Map<String, ?>> groupData, int groupLayout,
                                         String[] groupFrom, int[] groupTo,
                                         List<? extends List<? extends Map<String, ?>>> childData, int childLayout,
                                         String[] childFrom, int[] childTo,
                                         String[] imgFrom, int[]imgTo,
                                         String[] movieImgFrom, int[]movieImgTo,
                                         String[] genreFrom, int[]genreTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo); //Вызов конструктора суперкласса

        mChildData = childData;    //массив данных для ПОДСПИСКОВ
        mImgFrom = imgFrom;      //массив данных с индексами ПОДСПИСКОВ
        mImgTo = imgTo;          //массив с вьюхами для ПОДСПИСКОВ

        mGroupData = groupData;    //массив данных для СПИСКОВ
        mMovieImgFrom = movieImgFrom; //массив данных с индексами СПИСКОВ
        mMovieImgTo = movieImgTo;     //массив с вьюхами для СПИСКОВ
        mGenreFrom = genreFrom; //массив данных с индексом жанра СПИСКА
        mGenreTo = genreTo;     //массив с вьюхами жанра для СПИСКА
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) { //ОБЩИЙ метод формирования ПОДСПИСКА
        View v= super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);                           //получение вьюхи ПОДСПИСКА после суперметода
        childBindView(v, mChildData.get(groupPosition).get(childPosition), mImgFrom, mImgTo);                             //вызов своего метода формирования ПОДСПИСКА
        if (isLastChild){                                                                                                   //установка фона ПОДСПИСКА
            v.setBackgroundResource(R.drawable.child_last);
        }
        else {
            v.setBackgroundResource(R.drawable.child);
        }
        return  v;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {                     //ОБЩИЙ метод формирования СПИСКА
        View v= super.getGroupView(groupPosition, isExpanded, convertView, parent);                                           //получение вьюхи СПИСКА после суперметод
        if (isExpanded)                                                                                                         //установка селектора фона СПИСКА
        {
            v.setBackgroundResource(R.drawable.expselector);
        }
        else{
            v.setBackgroundResource(R.drawable.collselector);
        }
        groupBindView(v, mGroupData.get(groupPosition));                                                //вызов своего метода формирования СПИСКА

        return v;
    }

    private void childBindView(View view, Map<String, ?> data, String[] from, int[] to) { //СВОЙ метод формирования ПОДСПИСКА
        int len = to.length;
        for (int i = 0; i < len; i++) {                                                   //проход по элементам ПОДСПИСКА
            ImageView v = (ImageView)view.findViewById(to[i]);                            //установка каждому элементу соответствующего изображения 3d или no3d
            if (v != null) {
                v.setImageDrawable((Drawable)data.get(from[i]));
            }
        }
    }
    private void groupBindView(View view, Map<String, ?> data) { //СВОЙ метод формирования СПИСКА
        int len = mMovieImgTo.length;
        for (int i = 0; i < len; i++) {                                                   //проход по элементам СПИСКА
            ImageView imgView = (ImageView)view.findViewById(mMovieImgTo[i]);                            //установка каждому фильму соответствующего изображения
            TextView genreView = (TextView)view.findViewById(mGenreTo[i]);                            //установка каждому фильму соответствующего изображения
            if (imgView != null) {
                imgView.setImageDrawable((Drawable)data.get(mMovieImgFrom[i]));
            }
            if (genreView != null) {
                JSONObject movieData=(JSONObject)data.get(mGenreFrom[i]);
                try {
                    genreView.setText(movieData.getString("genre"));
                } catch (JSONException e) {
                    Log.d(TAG, "Нет графы \"Жанр\"");
                    genreView.setText("");
                }
            }
        }
    }
}