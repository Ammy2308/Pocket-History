package project.com.pockethistory.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import project.com.pockethistory.RecyclerContent;

public interface DataAnalyzer {
    JSONObject dataParserAndOrganizer(String jsonString) throws JSONException;
}
