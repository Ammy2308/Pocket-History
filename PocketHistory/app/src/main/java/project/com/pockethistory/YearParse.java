package project.com.pockethistory;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class YearParse {

    public JSONObject yearParseImplementation(String jsonString) throws JSONException {
        JSONObject wholeContent = new JSONObject(jsonString);
        JSONObject dataObject = wholeContent.getJSONObject("data");
        String part_heading = wholeContent.getString("year");
        Iterator<?> keys = dataObject.keys();
        JSONObject returnableData = new JSONObject();

        while(keys.hasNext()) {
            String key = (String) keys.next();
            List<RecyclerContent> parsedData = new ArrayList<>();
            JSONObject keyObject = dataObject.getJSONObject(key);
            Iterator<?> monthKeys = keyObject.keys();
            while(monthKeys.hasNext()) {
                String monthKeyName = (String) monthKeys.next();
                JSONArray requiredArray = keyObject.getJSONArray(monthKeyName);
                for (int i = 0; i < requiredArray.length(); i++) {
                    RecyclerContent newContent = new RecyclerContent();
                    JSONObject obj = requiredArray.getJSONObject(i);
                    Iterator<?> date = obj.keys();
                    String dateString = date.next().toString();
                    String heading = dateString+ ", " + part_heading;
                    String content = obj.getString(dateString);
                    newContent.setPaletteContent(heading, content);
                    parsedData.add(newContent);
                }
            }

            returnableData.put(key, parsedData);
        }

        return returnableData;
    }
}
