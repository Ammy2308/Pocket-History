package project.com.pockethistory;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_coor);

        Intent intent = getIntent();
        String currentDateDataJSON = intent.getStringExtra("currentDateData");
        DataAnalyzer dataAnalyzer = new DateParserHelper();
        try {
            parsedData = dataAnalyzer.dataParserAndOrganizer(currentDateDataJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
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
        dpd.setYearRange(1,2100);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        TextView text = new TextView(this);
        text.setText(getResources().getString(R.string.title_string));
        text.setTextAppearance(this, android.R.style.TextAppearance_Material_Widget_ActionBar_Title_Inverse);
        mToolbar.addView(text);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.calendar) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, final int year, int monthOfYear, int dayOfMonth) {
        String month = String.valueOf(monthOfYear+1), day = String.valueOf(dayOfMonth);
        final String to_search;
        if(monthOfYear < 10)
            month = "0" + (monthOfYear + 1);
        if(dayOfMonth < 10)
            day = "0" + dayOfMonth;

        to_search = month + "/" + day;
        CharSequence[] choice_list = new CharSequence[2];
        choice_list[0] = "Day of month : " + to_search;
        choice_list[1] = "Year : " + year;
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
        public RunSearchTask(int type) {
            this.type = type;
        }

        @Override
        protected void onPreExecute() {

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
            }

            Log.e("TAG", urlToHit);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlToHit);
            ResponseHandler<String> mResponse = new BasicResponseHandler();
            try {
                response = httpClient.execute(httpGet, mResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(final String jsonString) {
            DataAnalyzer dataAnalyzer;
            JSONObject parsedData = null;
            switch (type) {
                case 0:
                    Log.e("TAG", jsonString);
                    dataAnalyzer = new DateParserHelper();
                    try {
                       parsedData = dataAnalyzer.dataParserAndOrganizer(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mAdapter = new YourPagerAdapter(getSupportFragmentManager(), parsedData);
                    mPager.setAdapter(mAdapter);
                    break;
                case 1:
                    dataAnalyzer = new YearParserHelper();
                    break;
            }
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

class YourRecyclerAdapter extends RecyclerView.Adapter<YourRecyclerAdapter.YourRecyclerViewHolder> {
    private List<Object> obj;
    private LayoutInflater inflater;

    public YourRecyclerAdapter(Context context, List<Object> obj) {
        inflater = LayoutInflater.from(context);
        this.obj = obj;
    }

    @Override
    public YourRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = inflater.inflate(R.layout.custom_row, viewGroup, false);
        YourRecyclerViewHolder holder = new YourRecyclerViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(YourRecyclerViewHolder yourRecyclerViewHolder, int i) {
        RecyclerContent content = (RecyclerContent) obj.get(i);

        yourRecyclerViewHolder.heading.setText(content.getPaletteHeading());
        yourRecyclerViewHolder.textView.setText(content.getPaletteContent());
    }

    @Override
    public int getItemCount() {
        return obj.size();
    }

    static class YourRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView textView, heading;

        public YourRecyclerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_superhero);
            heading = (TextView) itemView.findViewById(R.id.palette_heading);
        }
    }
}