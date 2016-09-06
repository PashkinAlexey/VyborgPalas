package net.pashkin.vyborgpalas;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONParser {

    private JSONObject jobj = null;

    public JSONParser() {
    }

    public JSONObject getJSONFromUrl(String url) {

        StringBuilder builder = new StringBuilder(); //класс построения строки
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet); //Класс ответа HTTP
            StatusLine statusLine = response.getStatusLine(); //Статус ответа
            int statusCode = statusLine.getStatusCode(); //полсения статуса ответа
            if (statusCode == 200) { //Если все впорядке
                HttpEntity entity = response.getEntity(); //Получаем сущность
                InputStream content = entity.getContent(); //вытаскиваем из нее информацию
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line); //добавяем построчно полученный контент
                }
            } else {
                Log.e("Ошибка", "Невозможно загрузить файл");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            String str=builder.toString();
            jobj = new JSONObject(str.substring(19,str.length()-1));
        } catch (JSONException e) {
            Log.e("Ошибка", "Ошибка парсинга " + e.toString());
        }
        //передаем Json-массив
        /*try {
            Log.d("MyLog",jobj.getJSONObject("seanses").getJSONArray("2016-09-07").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        return jobj;
        //String str=builder.toString();
        //return str.substring(28,str.length()-4819);

    }
}
