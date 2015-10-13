package project.com.pockethistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import project.com.pockethistory.RecyclerClickListener.RecyclerItemClickListener;

public class FileChooserActivity extends AppCompatActivity {
    private File[] stored_filenames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        stored_filenames = Utils.getSavedFiles(getApplicationContext());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.file_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        FilesRecyclerAdapter filesRecyclerAdapter = new FilesRecyclerAdapter(this, stored_filenames);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(filesRecyclerAdapter);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));

        recyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String filename = stored_filenames[position].getName();
                    String pattern = "([a-zA-Z]+)_([a-zA-Z0-9\\s_-]+).dat";

                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(filename);

                    if(m.find()) {
                        if(m.group(1).contains("Date")) {
                            try {
                                String dateData = FileUtils.readFileToString(stored_filenames[position]);

                                Intent main_activity_intent = new Intent(FileChooserActivity.this, MainActivity.class);
                                main_activity_intent.putExtra("dateData", dateData);
                                FileChooserActivity.this.startActivity(main_activity_intent);
                                FileChooserActivity.this.finish();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            try {
                                String yearData = FileUtils.readFileToString(stored_filenames[position]);

                                Intent main_activity_intent = new Intent(FileChooserActivity.this, MainActivity.class);
                                main_activity_intent.putExtra("yearData", yearData);
                                FileChooserActivity.this.startActivity(main_activity_intent);
                                FileChooserActivity.this.finish();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            })
        );
    }

}
