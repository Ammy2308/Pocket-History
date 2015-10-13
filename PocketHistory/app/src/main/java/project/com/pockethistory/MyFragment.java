package project.com.pockethistory;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class MyFragment extends Fragment implements SearchView.OnQueryTextListener {
    public static final String ARG_OBJECT = "object";
    private List<Object> obj;
    private PaletteRecyclerAdapter paletteRecyclerAdapter;
    private RecyclerView recyclerView;
    private Context context;
    private File storagePath;
    private boolean is_descending;

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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("descending", is_descending);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        is_descending = savedInstanceState != null && savedInstanceState.getBoolean("descending", false);
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
                    paletteRecyclerAdapter = new PaletteRecyclerAdapter(context, obj);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(paletteRecyclerAdapter);
                    recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                }
                break;
            case R.id.action_sort_desc:
                if(!is_descending) {
                    is_descending = true;
                    Collections.reverse(obj);
                    paletteRecyclerAdapter = new PaletteRecyclerAdapter(context, obj);
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(paletteRecyclerAdapter);
                    recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                }
                break;
            case R.id.action_saved_pages:
                Intent file_activity = new Intent(getActivity(), FileChooserActivity.class);
                getActivity().startActivity(file_activity);
                getActivity().finish();
                break;
            case R.id.action_save:
                showSaveDialogBox();
                break;
        }
        return true;
    }

    public void showSaveDialogBox() {
        new MaterialDialog.Builder(getActivity())
                .title("Give it a name?")
                .content("Name the content you are saving. Makes it easy to fetch later!")
                .theme(Theme.DARK)
                .autoDismiss(false)
                .positiveText("Save")
                .inputRangeRes(3, 30, R.color.colorAccent)
                .input("Name the file", null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String state = Environment.getExternalStorageState();
                        if (Environment.MEDIA_MOUNTED.equals(state))
                            storagePath = new File(Environment.getExternalStorageDirectory() + "/PocketHistory/", Utils.CURRENT_TYPE + "_" + input + ".dat");
                        else
                            storagePath = new File(context.getFilesDir() + "/PocketHistory/", Utils.CURRENT_TYPE + "_" + input + ".dat");

                        try {
                            Log.e("PATH", storagePath.getPath());
                            FileUtils.writeStringToFile(storagePath, Utils.CURRENT_SEARCH);
                            dialog.dismiss();
                            Toast.makeText(context, "Saved this page", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).show();
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
