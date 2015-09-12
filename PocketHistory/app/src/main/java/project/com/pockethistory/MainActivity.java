package project.com.pockethistory;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import project.com.pockethistory.DataParsers.DateParserHelper;
import project.com.pockethistory.DataParsers.YearParserHelper;
import project.com.pockethistory.interfaces.DataAnalyzer;


public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout mCoordinator;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private ViewPager mPager;
    private YourPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private JSONObject parsedData;

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

    public static class MyFragment extends Fragment {
        public static final java.lang.String ARG_PAGE = "arg_page";

        public MyFragment() {

        }

        public static MyFragment newInstance(int pageNumber) {
            MyFragment myFragment = new MyFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber + 1);
            myFragment.setArguments(arguments);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int pageNumber = arguments.getInt(ARG_PAGE);
            RecyclerView recyclerView = new RecyclerView(getActivity());
            recyclerView.setAdapter(new YourRecyclerAdapter(getActivity()));
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return recyclerView;
        }
    }
}

class YourPagerAdapter extends FragmentStatePagerAdapter {
    JSONObject parsedData;
    List<String> keys = new ArrayList<>();
    public YourPagerAdapter(FragmentManager fm, JSONObject parsedData) {
        super(fm);
        this.parsedData = parsedData;
        Iterator keysToCopyIterator = parsedData.keys();
        while(keysToCopyIterator.hasNext()) {
            String key = (String) keysToCopyIterator.next();
            keys.add(key);
        }
    }

    @Override
    public Fragment getItem(int position) {
        MainActivity.MyFragment myFragment = MainActivity.MyFragment.newInstance(position);
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
    private ArrayList<String> list = new ArrayList<>();
    private LayoutInflater inflater;

    public YourRecyclerAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        list.add("A-Bomb (HAS)");
        list.add("A.I.M.");
        list.add("Abe");
        list.add("Abin");
        list.add("Abomination");
        list.add("Abraxas");
        list.add("Absorbing");
        list.add("Adam");
        list.add("Agent Bob");
        list.add("Agent Zero");
        list.add("Air Walker");
        list.add("Ajax");
        list.add("Alan Scott");
        list.add("Alex Mercer");
        list.add("Alex Woolsly");
        list.add("Alfred Pennyworth");
        list.add("Allan Quartermain");
        list.add("Amazo");
        list.add("Ammo Ando");
        list.add("Masahashi Angel");
        list.add("Angel Dust");
        list.add("Angel Salvadore");
        list.add("A-Bomb");
        list.add("Abe");
        list.add("Abin");
        list.add("Abomination");
        list.add("Abraxas");
        list.add("Absorbing");
        list.add("Adam");
        list.add("Agent Bob");
        list.add("Agent Zero");
        list.add("Air Walker");
        list.add("Ajax");
        list.add("Alan Scott");
        list.add("Alex Mercer");
        list.add("Alex Woolsly");
        list.add("Alfred Pennyworth");
        list.add("Allan Quartermain");
        list.add("Amazo");
        list.add("Ammo Ando");
        list.add("Masahashi Angel");
        list.add("Angel Dust");
        list.add("Angel Salvadore");

    }

    @Override
    public YourRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = inflater.inflate(R.layout.custom_row, viewGroup, false);
        YourRecyclerViewHolder holder = new YourRecyclerViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(YourRecyclerViewHolder yourRecyclerViewHolder, int i) {
        yourRecyclerViewHolder.textView.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class YourRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public YourRecyclerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_superhero);
        }
    }
}