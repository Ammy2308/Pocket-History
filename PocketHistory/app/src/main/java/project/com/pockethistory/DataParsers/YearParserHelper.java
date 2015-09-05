package project.com.pockethistory.DataParsers;

import java.util.List;

import project.com.pockethistory.RecyclerContent;
import project.com.pockethistory.YearParse;
import project.com.pockethistory.interfaces.DataAnalyzer;

public class YearParserHelper implements DataAnalyzer {
    private YearParse yearParse = new YearParse();

    @Override
    public List<RecyclerContent> dataParserAndOrganizer(String jsonString) {
        return yearParse.yearParseImplementation(jsonString);
    }
}
