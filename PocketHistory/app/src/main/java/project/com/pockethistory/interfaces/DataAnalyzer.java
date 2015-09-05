package project.com.pockethistory.interfaces;

import java.util.List;
import project.com.pockethistory.RecyclerContent;

public interface DataAnalyzer {
    List<RecyclerContent> dataParserAndOrganizer(String jsonString);
}
