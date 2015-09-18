package project.com.pockethistory;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.victor.loading.book.BookLoading;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SplashActivity extends AppCompatActivity {
    private BookLoading bookLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bookLoading = (BookLoading) findViewById(R.id.bookloading);

        new CurrentDateAsyncTask().execute();
    }

    public class CurrentDateAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            bookLoading.start();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            DateFormat dateFmt = new SimpleDateFormat("MM/dd");
            dateFmt.setTimeZone(TimeZone.getDefault());

            dateFmt.format(new Date());
            String urlToHit = Utils.DATE_URL + dateFmt.format(new Date());
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
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    bookLoading.stop();
                    Intent main_activity_intent = new Intent(SplashActivity.this, MainActivity.class);
                    main_activity_intent.putExtra("currentDateData", jsonString);
                    SplashActivity.this.startActivity(main_activity_intent);
                    SplashActivity.this.finish();
                }
            }, 1000);
        }
    }
}