package project.com.pockethistory;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends ActionBarActivity {

    private View date_dialog_view;
    public MaterialDialog date_dialog;
    public Context context = this;
    private EditText day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        date_dialog = getDateBox(context);
        date_dialog_view = date_dialog.getCustomView();

    }

    public MaterialDialog getDateBox(Context context) {
        return new MaterialDialog.Builder(context)
                .customView(R.layout.date_dialog, true)
                .cancelable(false)
                .content("")
                .positiveText("Fetch it")
                .autoDismiss(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                    }
                })
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.calendar) {

        }
        return super.onOptionsItemSelected(item);
    }
}
