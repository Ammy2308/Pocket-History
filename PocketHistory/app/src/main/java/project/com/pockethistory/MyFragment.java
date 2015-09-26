package project.com.pockethistory;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class MyFragment extends Fragment implements SearchView.OnQueryTextListener {
    public static final String ARG_OBJECT = "object";
    List<Object> obj;
    PaletteRecyclerAdapter paletteRecyclerAdapter;
    RecyclerView recyclerView;

    public MyFragment() {

    }

    public void setArgument(List<Object> obj) {
        this.obj = obj;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Bundle args = getArguments();
        int pagenumber = args.getInt(ARG_OBJECT);
        paletteRecyclerAdapter = new PaletteRecyclerAdapter(getActivity(), obj);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(paletteRecyclerAdapter);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("Tag", newText);
        return false;
    }
}
