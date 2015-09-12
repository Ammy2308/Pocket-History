package project.com.pockethistory.DataParsers;

import org.json.JSONObject;

import java.util.List;

import project.com.pockethistory.RecyclerContent;
import project.com.pockethistory.YearParse;
import project.com.pockethistory.interfaces.DataAnalyzer;

public class YearParserHelper implements DataAnalyzer {
    private YearParse yearParse = new YearParse();

    @Override
    public JSONObject dataParserAndOrganizer(String jsonString) {
        return yearParse.yearParseImplementation(jsonString);
    }
}
