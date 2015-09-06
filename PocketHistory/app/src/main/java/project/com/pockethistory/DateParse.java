package project.com.pockethistory;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class DateParse {

    public List<RecyclerContent> dateParseImplementation(String jsonString) throws JSONException {
        JSONObject dataObject = new JSONObject(jsonString).getJSONObject("data");
        Iterator<?> keys = dataObject.keys();
        while(keys.hasNext()) {
            String key = (String) keys.next();
            Log.e("BLA", key);
//            if(dataObject.get(key) instanceof JSONObject ) {
//
//            }
        }

        return null;
    }
}
