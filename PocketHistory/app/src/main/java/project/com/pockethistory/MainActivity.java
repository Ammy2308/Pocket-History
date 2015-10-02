package project.com.pockethistory;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import project.com.pockethistory.DataParsers.DateParserHelper;
import project.com.pockethistory.DataParsers.YearParserHelper;
import project.com.pockethistory.interfaces.DataAnalyzer;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private CoordinatorLayout mCoordinator;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private ViewPager mPager;
    private YourPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private JSONObject parsedData;
    private Context context;
    private MaterialDialog progressDialog;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_coor);

        context = this;
        Intent intent = getIntent();
        String currentDateDataJSON = intent.getStringExtra("currentDateData");
        DataAnalyzer dataAnalyzer = new DateParserHelper();
        try {
            parsedData = dataAnalyzer.dataParserAndOrganizer(currentDateDataJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mAdapter = new YourPagerAdapter(getSupportFragmentManager(), parsedData);
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        Calendar now = Calendar.getInstance();
        final DatePickerDialog dpd = DatePickerDialog.newInstance(
                MainActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setYearRange(1, now.get(Calendar.YEAR) + 20);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        if(Build.VERSION.SDK_INT < 21) {
            TextView text = new TextView(this);
            text.setText(getResources().getString(R.string.title_string));
            text.setTextColor(Color.WHITE);
            text.setTextSize(20);
            text.setTypeface(null, Typeface.BOLD);
            mToolbar.addView(text);
        } else {
            TextView text = new TextView(this);
            text.setText(getResources().getString(R.string.title_string));
            text.setTextAppearance(this, android.R.style.TextAppearance_Material_Widget_ActionBar_Title_Inverse);
            mToolbar.addView(text);
        }

        setupProgressBox();
    }

    public void setupProgressBox() {
        progressDialog = new MaterialDialog.Builder(context)
                .content("Reading history books")
                .theme(Theme.DARK)
                .progress(true, 0)
                .autoDismiss(false)
                .cancelable(false)
                .build();
    }

    @Override
    public void onDateSet(DatePickerDialog view, final int year, int monthOfYear, int dayOfMonth) {
        String month = String.valueOf(monthOfYear+1), day = String.valueOf(dayOfMonth);
        final String to_search;
        if((monthOfYear+1) < 10)
            month = "0" + (monthOfYear + 1);
        if(dayOfMonth < 10)
            day = "0" + dayOfMonth;

        to_search = month + "/" + day;
        CharSequence[] choice_list = new CharSequence[3];
        choice_list[0] = "Day of month : " + to_search;
        choice_list[1] = "Year : " + year;
        choice_list[2] = "Entire Date";
        new MaterialDialog.Builder(this)
                .title("What to search?")
                .theme(Theme.DARK)
                .items(choice_list)
                .autoDismiss(false)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        Log.e("TAG", String.valueOf(which));
                        switch (which) {
                            case 0:
                                dialog.dismiss();
                                new RunSearchTask(which).execute(to_search);
                                break;
                            case 1:
                                dialog.dismiss();
                                new RunSearchTask(which).execute(String.valueOf(year));
                                break;
                            case 2:
                                dialog.dismiss();
                                new RunSearchTask(which, String.valueOf(year)).execute(to_search);
                                break;
                            default:
                                Snackbar.make(mCoordinator, "Select something to search for", Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();
                                break;
                        }
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();
    }

    public class RunSearchTask extends AsyncTask<String, Void, String> {
        private int type;
        private String year;
        public RunSearchTask(int type) {
            this.type = type;
        }
        public RunSearchTask(int type, String year) {
            this.type = type;
            this.year = year;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            String urlToHit = null;

            switch (type) {
                case 0:
                    urlToHit= Utils.DATE_URL + params[0];
                    break;
                case 1:
                    urlToHit= Utils.YEAR_URL + params[0];
                    break;
                case 2:
                    urlToHit= Utils.DATE_URL + params[0];
                    break;
            }

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urlToHit);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();

                if(responseCode == 200){
                    response = Utils.readStream(urlConnection.getInputStream());
                }else{
                    Log.v("TAG", "Response code:"+ responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }

            return response;
        }

        @Override
        protected void onPostExecute(final String jsonString) {
            DataAnalyzer dataAnalyzer;
            JSONObject parsedData = null;
            switch (type) {
                case 0:
                    dataAnalyzer = new DateParserHelper();
                    try {
                        Utils.CURRENT_SEARCH = jsonString;
                        Utils.CURRENT_TYPE = "Date";
                       parsedData = dataAnalyzer.dataParserAndOrganizer(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    dataAnalyzer = new YearParserHelper();
                    try {
                        Utils.CURRENT_SEARCH = jsonString;
                        Utils.CURRENT_TYPE = "Year";
                        parsedData = dataAnalyzer.dataParserAndOrganizer(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    dataAnalyzer = new DateParserHelper();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        String newJsonString = Utils.parseForEntireDate(jsonObject, year);
                        Utils.CURRENT_SEARCH = newJsonString;
                        Utils.CURRENT_TYPE = "Date";
                        parsedData = dataAnalyzer.dataParserAndOrganizer(newJsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            mAdapter = new YourPagerAdapter(getSupportFragmentManager(), parsedData);
            mPager.setAdapter(mAdapter);
            mTabLayout.setTabsFromPagerAdapter(mAdapter);

            progressDialog.dismiss();
        }
    }
}

class YourPagerAdapter extends FragmentStatePagerAdapter {
    JSONObject parsedData;
    List<Object> all_list = new ArrayList<>();
    List<String> keys = new ArrayList<>();

    public YourPagerAdapter(FragmentManager fm, JSONObject parsedData) {
        super(fm);
        this.parsedData = parsedData;
        Iterator keysToCopyIterator = parsedData.keys();
        while (keysToCopyIterator.hasNext()) {
            String key = (String) keysToCopyIterator.next();
            keys.add(key);
            try {
                all_list.add(parsedData.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        MyFragment myFragment = new MyFragment();
        myFragment.setArgument((List<Object>) all_list.get(position));
        Bundle args = new Bundle();
        args.putInt(MyFragment.ARG_OBJECT, position);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public int getCount() {
        return parsedData.length();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return keys.get(position);
    }
}