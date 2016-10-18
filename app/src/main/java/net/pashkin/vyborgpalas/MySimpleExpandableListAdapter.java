package net.pashkin.vyborgpalas;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Алексей on 12.09.2016.
 */
public class MySimpleExpandableListAdapter extends SimpleExpandableListAdapter {
    private List<? extends List<? extends Map<String, ?>>> mChildData; //ОБЪЯВЛЕНИЕ массива данных для ПОДСПИСКОВ
    private String[] mImgFrom;                                       //ОБЪЯВЛЕНИЕ массива данных с индексами ПОДСПИСКОВ
    private int[] mImgTo;                                            //ОБЪЯВЛЕНИЕ массива с вьюхами для ПОДСПИСКОВ

    private List<? extends Map<String, ?>> mGroupData;                 //ОБЪЯВЛЕНИЕ массива данных для СПИСКА
    private String[] mMovieImgFrom;                                       //ОБЪЯВЛЕНИЕ массива данных с индексами СПИСКА
    private int[] mMovieImgTo;                                            //ОБЪЯВЛЕНИЕ массива с вьюхами для СПИСКА

    public MySimpleExpandableListAdapter(Context context,
                                         List<? extends Map<String, ?>> groupData, int groupLayout,
                                         String[] groupFrom, int[] groupTo,
                                         List<? extends List<? extends Map<String, ?>>> childData, int childLayout,
                                         String[] childFrom, int[] childTo,
                                         String[] imgFrom, int[]imgTo,
                                         String[] movieImgFrom, int[]movieImgTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo); //Вызов конструктора суперкласса

        mChildData = childData;    //массив данных для ПОДСПИСКОВ
        mImgFrom = imgFrom;      //массив данных с индексами ПОДСПИСКОВ
        mImgTo = imgTo;          //массив с вьюхами для ПОДСПИСКОВ

        mGroupData = groupData;    //массив данных для СПИСКОВ
        mMovieImgFrom = movieImgFrom; //массив данных с индексами СПИСКОВ
        mMovieImgTo = movieImgTo;     //массив с вьюхами для СПИСКОВ
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
        groupBindView(v, mGroupData.get(groupPosition), mMovieImgFrom, mMovieImgTo);                                                //вызов своего метода формирования СПИСКА

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
    private void groupBindView(View view, Map<String, ?> data, String[] from, int[] to) { //СВОЙ метод формирования СПИСКА
        int len = to.length;
        for (int i = 0; i < len; i++) {                                                   //проход по элементам СПИСКА
            ImageView v = (ImageView)view.findViewById(to[i]);                            //установка каждому фильму соответствующего изображения
            if (v != null) {
                v.setImageDrawable((Drawable)data.get(from[i]));
            }
        }
    }
}