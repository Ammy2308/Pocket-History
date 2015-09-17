package project.com.pockethistory;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import project.com.pockethistory.DataParsers.DateParserHelper;
import project.com.pockethistory.interfaces.DataAnalyzer;


public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout mCoordinator;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private ViewPager mPager;
    private YourPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private JSONObject parsedData;
    private List<Object> all_list;
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
            parsedData =  dataAnalyzer.dataParserAndOrganizer(currentDateDataJSON);
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

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mCoordinator, "FAB Clicked", Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();
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
}

class YourPagerAdapter extends FragmentStatePagerAdapter {
    JSONObject parsedData;
    List<Object> all_list = new ArrayList<>();
    List<String> keys = new ArrayList<>();
    public YourPagerAdapter(FragmentManager fm, JSONObject parsedData) {
        super(fm);
        this.parsedData = parsedData;
        Iterator keysToCopyIterator = parsedData.keys();
        while(keysToCopyIterator.hasNext()) {
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