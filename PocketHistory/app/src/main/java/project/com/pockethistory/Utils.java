package project.com.pockethistory;

import android.content.Context;
import android.content.res.TypedArray;

public class Utils {
    public static final String BASE_URL = "http://api.todevs.com/history/";
    public static final String DATE_URL = BASE_URL + "date/";
    public static final String YEAR_URL = BASE_URL + "year/";

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return toolbarHeight;
    }
}
