package project.com.pockethistory;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DateParse {

    public JSONObject dateParseImplementation(String jsonString) throws JSONException {
        JSONObject wholeContent = new JSONObject(jsonString);
        JSONObject dataObject = wholeContent.getJSONObject("data");
        String part_heading = wholeContent.getString("date");
        Iterator<?> keys = dataObject.keys();
        JSONObject returnableData = new JSONObject();

        while(keys.hasNext()) {
            String key = (String) keys.next();
            List<RecyclerContent> parsedData = new ArrayList<>();
            JSONArray dataArray = dataObject.getJSONArray(key);
            for (int i = 0; i < dataArray.length(); i++) {
                RecyclerContent newContent = new RecyclerContent();
                JSONObject obj = dataArray.getJSONObject(i);
                String heading = part_heading + ", " + obj.getString("year");
                String content = obj.getString("text");
                newContent.setPaletteContent(heading, content);
                parsedData.add(newContent);
            }

            returnableData.put(key, parsedData);
        }

        return returnableData;
    }
}
