package project.com.pockethistory;


import android.content.Context;
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

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class MyFragment extends Fragment implements SearchView.OnQueryTextListener {
    public static final String ARG_OBJECT = "object";
    private List<Object> obj;
    private PaletteRecyclerAdapter paletteRecyclerAdapter;
    private RecyclerView recyclerView;
    private Context context;
    private boolean is_descending = false;

    public MyFragment() {

    }

    public void setArgument(List<Object> obj) {
        this.obj = obj;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Bundle args = getArguments();
        int pagenumber = args.getInt(ARG_OBJECT);

        context = getActivity();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        paletteRecyclerAdapter = new PaletteRecyclerAdapter(getActivity(), obj);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(paletteRecyclerAdapter);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_asc:
                if(is_descending) {
                    is_descending = false;
                    Collections.reverse(obj);
                }
                break;
            case R.id.action_sort_desc:
                if(!is_descending) {
                    is_descending = true;
                    Collections.reverse(obj);
                }
                break;
        }

        paletteRecyclerAdapter = new PaletteRecyclerAdapter(context, obj);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(paletteRecyclerAdapter);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Object> filteredRecList = new ArrayList<>();
        filteredRecList.addAll(filter(obj, newText));

        paletteRecyclerAdapter = new PaletteRecyclerAdapter(context, filteredRecList);
        if(newText.equals("")) {
            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(paletteRecyclerAdapter);
            alphaAdapter.setFirstOnly(true);
            recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        } else {
            recyclerView.setAdapter(new ScaleInAnimationAdapter(paletteRecyclerAdapter));
        }

        return true;
    }

    private List<Object> filter(List<Object> models, String query) {
        query = query.toLowerCase();

        final List<Object> filteredRecList = new ArrayList<>();
        for (Object model : models) {
            RecyclerContent content = (RecyclerContent) model;
            final String contentToBeSearched = content.getPaletteHeading().toLowerCase() +
                                      " " + content.getPaletteContent().toLowerCase();

            if (contentToBeSearched.contains(query)) {
                filteredRecList.add(model);
            }
        }
        return filteredRecList;
    }
}
