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
        Iterator<?> keys = dataObject.keys();
        List<JSONObject> returnableData = new ArrayList<>();
        while(keys.hasNext()) {
            String key = (String) keys.next();
            List<RecyclerContent> parsedData = new ArrayList<>();
            Log.e("BLA", key);
            JSONArray dataArray = dataObject.getJSONArray(key);
            for (int i = 0; i < dataArray.length(); i++) {
                RecyclerContent newContent = new RecyclerContent();

            }

            Log.e("yo", dataArray.getClass().getName());
        }
//        returnableData.add(keyTerms);
        return null;
    }
}
