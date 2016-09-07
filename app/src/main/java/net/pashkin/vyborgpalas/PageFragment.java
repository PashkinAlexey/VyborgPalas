package net.pashkin.vyborgpalas;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;
    int backColor;
    ExpandableListView lvMain;

    // коллекция для групп
    ArrayList<Map<String, String>> movieData;

    // коллекция для элементов одной группы
    ArrayList<Map<String, String>> timeDataItem;

    // общая коллекция для коллекций элементов
    ArrayList<ArrayList<Map<String, String>>> timeData;
    // в итоге получится childData = ArrayList<childDataItem>

    // список атрибутов группы или элемента
    Map<String, String> m,t;

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
        JSONArray dates=jobj.getJSONObject("seanses").names();
        JSONArray jArray= jobj.getJSONObject("seanses").getJSONArray(dates.getString(pageNumber));
        // создаем коллекцию групп элементов
        movieData = new ArrayList<Map<String, String>>();
        // создаем коллекцию для коллекций элементов
        timeData = new ArrayList<ArrayList<Map<String, String>>>();
        // создаем коллекцию элементов для первой группы
        timeDataItem = new ArrayList<Map<String, String>>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                JSONObject jObjTemp=(JSONObject)jArray.get(i);
                String movieId=jObjTemp.getString("f");
                String movieName=jobj.getJSONObject("films").getJSONObject(movieId).getString("fr");
                int movieIndex=-1;
                for (int j=0; j<movieData.size(); j++) {
                    if (movieData.get(j).get("movieName").equals(movieName)) {
                        movieIndex = j;
                        break;
                    }
                }
                if (movieIndex>=0){
                    t = new HashMap<String, String>();
                    t.put("time", jObjTemp.getString("t")); // время
                    timeData.get(movieIndex).add(t);
                }
                else {
                    m = new HashMap<String, String>();
                    m.put("movieName", movieName); //название фильма
                    movieData.add(m);

                    timeDataItem = new ArrayList<Map<String, String>>();
                    t = new HashMap<String, String>();
                    t.put("time", jObjTemp.getString("t")); // время
                    timeDataItem.add(t);
                    // добавляем в коллекцию коллекций
                    timeData.add(timeDataItem);
                }
            }
        }

        // список атрибутов групп для чтения
        String groupFrom[] = new String[] {"movieName"};
        // список ID view-элементов, в которые будет помещены атрибуты групп
        int groupTo[] = new int[] {android.R.id.text1};

        // список атрибутов элементов для чтения
        String childFrom[] = new String[] {"time"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int childTo[] = new int[] {android.R.id.text1};

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                getContext(),
                movieData, android.R.layout.simple_expandable_list_item_1,
                groupFrom, groupTo,
                timeData, android.R.layout.simple_list_item_1,
                childFrom, childTo);

        lvMain.setAdapter(adapter);
    }
}
