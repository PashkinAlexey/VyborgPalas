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
    private List<? extends List<? extends Map<String, ?>>> mChildData;
    private String[] mChildFrom;
    private int[] mChildTo;

    private List<? extends Map<String, ?>> mGroupData;
    private String[] mGroupFrom;
    private int[] mGroupTo;

    public MySimpleExpandableListAdapter(Context context,
                                         List<? extends Map<String, ?>> groupData, int groupLayout,
                                         String[] groupFrom, int[] groupTo,
                                         List<? extends List<? extends Map<String, ?>>> childData, int childLayout,
                                         String[] childFrom, int[] childTo,
                                         String[] imgFrom, int[]imgTo,
                                         String[] movieImgFrom, int[]movieImgTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
        mChildData = childData;
        mChildFrom = imgFrom;
        mChildTo = imgTo;

        mGroupData = groupData;
        mGroupFrom = movieImgFrom;
        mGroupTo = movieImgTo;
    }
    private void childBindView(View view, Map<String, ?> data, String[] from, int[] to) {
        int len = to.length;
        for (int i = 0; i < len; i++) {
            ImageView v = (ImageView)view.findViewById(to[i]);
            if (v != null) {
                v.setImageDrawable((Drawable)data.get(from[i]));
            }
        }
    }
    private void groupBindView(View view, Map<String, ?> data, String[] from, int[] to) {
        int len = to.length;
        for (int i = 0; i < len; i++) {
            ImageView v = (ImageView)view.findViewById(to[i]);
            if (v != null) {
                v.setImageDrawable((Drawable)data.get(from[i]));
            }
        }
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v= super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
        childBindView(v, mChildData.get(groupPosition).get(childPosition), mChildFrom, mChildTo);
        return  v;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v= super.getGroupView(groupPosition, isExpanded, convertView, parent);
        groupBindView(v, mGroupData.get(groupPosition), mGroupFrom, mGroupTo);
        return v;
    }
}