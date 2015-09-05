package project.com.pockethistory.DataParsers;

import java.util.List;

import project.com.pockethistory.DateParse;
import project.com.pockethistory.RecyclerContent;
import project.com.pockethistory.interfaces.DataAnalyzer;

public class DateParserHelper implements DataAnalyzer {
    private DateParse dateParse = new DateParse();

    @Override
    public List<RecyclerContent> dataParserAndOrganizer(String jsonString) {
        return dateParse.dateParseImplementation(jsonString);
    }
}
