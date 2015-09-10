package project.com.pockethistory.interfaces;

import org.json.JSONException;

import java.util.List;
import project.com.pockethistory.RecyclerContent;

public interface DataAnalyzer {
    List<Object> dataParserAndOrganizer(String jsonString) throws JSONException;
}
