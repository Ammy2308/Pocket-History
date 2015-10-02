package project.com.pockethistory;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Utils {
    public static final String BASE_URL = "http://api.todevs.com/history/";
    public static final String DATE_URL = BASE_URL + "date/";
    public static final String YEAR_URL = BASE_URL + "year/";
    public static String CURRENT_SEARCH, CURRENT_TYPE;

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return toolbarHeight;
    }

    public static String parseForEntireDate(JSONObject object, String year) throws JSONException {
        JSONObject dataObject = object.getJSONObject("data");
        Iterator<?> keys = dataObject.keys();
        JSONObject returnableData = new JSONObject();

        while(keys.hasNext()) {
            String key = (String) keys.next();
            JSONArray dataArray = dataObject.getJSONArray(key);
            JSONArray requiredData = new JSONArray();
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject obj = dataArray.getJSONObject(i);
                if(obj.getString("year").equals(year))
                    requiredData.put(obj);
            }
            returnableData.put(key, requiredData);
        }

        object.put("data", returnableData);
        Log.e("TAG", object.toString());
        return object.toString();
    }

    public static String readStream(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public static File[] getSavedFiles(Context context) {
        File storagePath;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state))
            storagePath = new File(Environment.getExternalStorageDirectory() + "/PocketHistory/");
        else
            storagePath = new File(context.getFilesDir() + "/PocketHistory/");

        return storagePath.listFiles();
    }
}
