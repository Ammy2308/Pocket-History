package project.com.pockethistory;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DateParse {

    public List<Object> dateParseImplementation(String jsonString) throws JSONException {
        JSONObject dataObject = new JSONObject(jsonString).getJSONObject("data");
        String part_heading = dataObject.getString("date");
        Iterator<?> keys = dataObject.keys();
        List<JSONObject> returnableData = new ArrayList<>();
        while(keys.hasNext()) {
            String key = (String) keys.next();
            List<RecyclerContent> parsedData = new ArrayList<>();
            Log.e("BLA", key);
            JSONArray dataArray = dataObject.getJSONArray(key);
            for (int i = 0; i < dataArray.length(); i++) {
                RecyclerContent newContent = new RecyclerContent();
                JSONObject obj = dataArray.getJSONObject(i);
                String heading = part_heading + obj.getString("year");
                Log.e("yo", obj.toString());
            }

            Log.e("yo", dataArray.getClass().getName());
        }
        return null;
    }
}
