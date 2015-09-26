package project.com.pockethistory;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;

public class MyFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    List<Object> obj;

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

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(new PaletteRecyclerAdapter(getActivity(), obj));
        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }
}
