package net.pashkin.vyborgpalas;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

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
public class PageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;
    ExpandableListView lvMain;

    // список атрибутов группы или элемента
    Map<String, Object> img,t,m;

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
            listCreator(MainActivity.getSchedule(),MainActivity.getMovieImgs(),MainActivity.getMovieData());
        } catch (JSONException e) {
            Toast toast = Toast.makeText(getContext(),"Ошибка загрузки данных", Toast.LENGTH_SHORT);
            toast.show();
        }
        return view;
    }

    public void listCreator(JSONObject jobj, HashMap<String,Drawable> movieImgs, final HashMap<String,JSONObject> movieData) throws JSONException {
        JSONArray dates=jobj.getJSONObject("seanses").names();
        JSONArray seansesArray= jobj.getJSONObject("seanses").getJSONArray(dates.getString(pageNumber));

        // создаем список ID фильмов, который будет соответствовать их позиции в списке
        final ArrayList<String> idList = new ArrayList<String>();

        // создаем коллекцию групп элементов
        final ArrayList<Map<String, Object>> movieCollection = new ArrayList<Map<String, Object>>();

        // создаем коллекцию для коллекций элементов
        ArrayList<ArrayList<Map<String, Object>>> timeData = new ArrayList<ArrayList<Map<String, Object>>>();

        // создаем коллекцию элементов для первой группы
        ArrayList<Map<String, Object>> timeDataItem;

        if (seansesArray != null) {
            for (int i=0;i<seansesArray.length();i++){
                JSONObject jObjTemp=(JSONObject)seansesArray.get(i);
                String movieId=jObjTemp.getString("f");
                String movieName=jobj.getJSONObject("films").getJSONObject(movieId).getString("fr");
                int movieIndex=-1;
                for (int j=0; j<movieCollection.size(); j++) {                            //Проход по именам фильмов и вычисление индекса если имя совпадает
                    if (movieCollection.get(j).get("movieName").equals(movieName)) {
                        movieIndex = j;
                        break;
                    }
                }
                if (movieIndex>=0){                                                 //Если фильм уже записан, то добавить новое время к нему по найденному индексу
                    t = new HashMap<String, Object>();
                    t.put("time", jObjTemp.getString("t")); // время
                    boolean is3d=(jObjTemp.getInt("is3d")==0)?false:true;
                    t.put("is3d", is3d);
                    timeData.get(movieIndex).add(t);
                }
                else {                                                              //Если фильм не записан, то добавить фильм и добавить новое время к нему
                    m = new HashMap<String, Object>();
                    m.put("movieName", movieName); //название фильма
                    m.put("movieImage", movieImgs.get(movieId)); //изображение фильма
                    m.put("movieData", movieData.get(movieId)); //информация по фильму
                    movieCollection.add(m);
                    idList.add(movieId); //добавляем ID фильма в отдельный список, для получения нужного HTML

                    t = new HashMap<String, Object>();
                    t.put("time", jObjTemp.getString("t")); // время
                    boolean is3d=(jObjTemp.getInt("is3d")==0)?false:true;    //В 3D сеанс или нет
                    t.put("is3d", is3d); //изображение сеанса
                    timeDataItem = new ArrayList<Map<String, Object>>();
                    timeDataItem.add(t);
                    // добавляем в коллекцию коллекций
                    timeData.add(timeDataItem);
                }
            }
        }

        // список атрибутов групп для чтения
        String movieFrom[] = new String[] {"movieName"};
        // список ID view-элементов, в которые будет помещены атрибуты групп
        int movieTo[] = new int[] {R.id.description};

        // список атрибутов элементов для чтения
        String timeFrom[] = new String[] {"time"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int timeTo[] = new int[] {R.id.description};

        String is3dFrom[] = new String[] {"is3d"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int is3dTo[] = new int[] {R.id.is3dFirst};

        String movieImgFrom[] = new String[] {"movieImage"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int movieImgTo[] = new int[] {R.id.movieImage};

        String genreFrom[] = new String[] {"movieData"};
        // список ID view-элементов, в которые будет помещены атрибуты элементов
        int genreTo[] = new int[] {R.id.genre};

        MySimpleExpandableListAdapter adapter = new MySimpleExpandableListAdapter(
                getContext(),
                movieCollection, R.layout.my_expandable_list_item,
                movieFrom, movieTo,
                timeData, R.layout.my_list_item,
                timeFrom, timeTo,
                is3dFrom, is3dTo,
                movieImgFrom, movieImgTo,
                genreFrom, genreTo
                 );

        lvMain.setAdapter(adapter);
        lvMain.setOnItemLongClickListener(new ExpandableListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                long packedPosition = lvMain.getExpandableListPosition(position);

                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition); //позиция нажатого пункта списка
                //int childPosition = ExpandableListView.getPackedPositionChild(packedPosition); //позиция нажатого пункта подсписка


        //  Нажатие на пункт списка
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP ) {
                    String movieId=idList.get(groupPosition);
                    Intent intent= new Intent(getContext(), FilmActivity.class);
                    intent.putExtra("movieId", movieId);
                    intent.putExtra("movieData", movieData.get(movieId).toString());
                    startActivity(intent);
                }

        /*  //Нажатие на пункт подсписка
                else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    Log.d("myLogs", Long.toString(childPosition));
                }*/

                return true;
            }
        });
    }
}
