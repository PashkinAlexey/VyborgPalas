package net.pashkin.vyborgpalas;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Created by Алексей on 12.09.2016.
 **/
public class  PageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;
    ExpandableListView lvMain;

    // список атрибутов группы или элемента
    Map<String, Object> img,t;
    Map<String, String> m;

    View view;

    static PageFragment newInstance(int page) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.pagefragment, null);
        lvMain = (ExpandableListView) view.findViewById(R.id.lvMain);

        try {
            listCreator(MainActivity.getJobject());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    public void listCreator(JSONObject jobj) throws JSONException {
        Drawable glass = ResourcesCompat.getDrawable(getResources(), R.drawable.glass, null);
        Drawable noGlass = ResourcesCompat.getDrawable(getResources(), R.drawable.noglass, null);
        JSONArray dates=jobj.getJSONObject("seanses").names();
        JSONArray jArray= jobj.getJSONObject("seanses").getJSONArray(dates.getString(pageNumber));
        // создаем список ID фильмов, который будет соответствовать их позиции в списке
        final ArrayList<String> idList = new ArrayList<String>();
        // создаем коллекцию групп элементов
        final ArrayList<Map<String, String>> movieData = new ArrayList<Map<String, String>>();
        // создаем коллекцию для коллекций элементов
        ArrayList<ArrayList<Map<String, Object>>> timeData = new ArrayList<ArrayList<Map<String, Object>>>();
        // создаем коллекцию элементов для первой группы
        ArrayList<Map<String, Object>> timeDataItem = new ArrayList<Map<String, Object>>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                JSONObject jObjTemp=(JSONObject)jArray.get(i);
                String movieId=jObjTemp.getString("f");
                String movieName=jobj.getJSONObject("films").getJSONObject(movieId).getString("fr");
                int movieIndex=-1;
                for (int j=0; j<movieData.size(); j++) {                            //Проход по именам фильмов, и вычисление индекса если имя совпадает
                    if (movieData.get(j).get("movieName").equals(movieName)) {
                        movieIndex = j;
                        break;
                    }
                }
                if (movieIndex>=0){                                                 //Если фильм уже записан то добавить новое время к нему по найденному индексу
                    t = new HashMap<String, Object>();
                    t.put("time", jObjTemp.getString("t")); // время
                    Drawable image=(jObjTemp.getInt("is3d")==0)?noGlass:glass;
                    t.put("img", image);
                    timeData.get(movieIndex).add(t);
                }
                else {                                                              //Если фильм не записан то добавить фильм и добавить новое время к нему
                    m = new HashMap<String, String>();
                    m.put("movieName", movieName); //название фильма
                    movieData.add(m);
                    idList.add(movieId);

                    t = new HashMap<String, Object>();
                    t.put("time", jObjTemp.getString("t")); // время
                    Drawable image=(jObjTemp.getInt("is3d")==0)?noGlass:glass;
                    t.put("img", image);

                    timeDataItem.add(t);
                    // добавляем в коллекцию коллекций
                    timeData.add(timeDataItem);
                }
            }
        }

        // список атрибутов групп для чтения
        String movieFrom[] = new String[] {"movieName"};
        // список ID view-элементов, в которые будет помещены атрибуты групп
        int movieTo[] = new int[] {R.id.text1};

        // список атрибутов элементов для чтения
        String timeFrom[] = new String[] {"time"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int timeTo[] = new int[] {R.id.text1};

        String imgFrom[] = new String[] {"img"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int imgTo[] = new int[] {R.id.imageView1};

        MySimpleExpandableListAdapter adapter = new MySimpleExpandableListAdapter(
                getContext(),
                movieData, R.layout.my_expandable_list_item,
                movieFrom, movieTo,
                timeData, R.layout.my_list_item,
                timeFrom, timeTo,
                imgFrom, imgTo
                 );

        lvMain.setAdapter(adapter);
        lvMain.setOnItemLongClickListener(new ExpandableListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                long packedPosition = lvMain.getExpandableListPosition(position);

                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);


        /*  if group item clicked */
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {

                    Log.d("myLogs", idList.get(groupPosition));
                    //onGroupLongClick(groupPosition);
                }

        /*  if child item clicked */
                else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    Log.d("myLogs", Long.toString(childPosition));
                    //onChildLongClick(groupPosition, childPosition);
                }

                return true;
            }
        });
    }
}
