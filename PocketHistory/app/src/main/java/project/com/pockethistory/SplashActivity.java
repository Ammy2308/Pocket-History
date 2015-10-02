package project.com.pockethistory;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.victor.loading.book.BookLoading;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SplashActivity extends AppCompatActivity {
    private BookLoading bookLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bookLoading = (BookLoading) findViewById(R.id.bookloading);

        File storagePath;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
            storagePath = new File(Environment.getExternalStorageDirectory() + "/PocketHistory/");
        else
            storagePath = new File(getFilesDir() + "/PocketHistory/");

        if(!storagePath.exists())
            storagePath.mkdirs();

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

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urlToHit);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();

                if(responseCode == 200){
                    response = Utils.readStream(urlConnection.getInputStream());
                    Utils.CURRENT_SEARCH = response;
                    Utils.CURRENT_TYPE = "Date";
                }else{
                    Log.v("TAG", "Response code:"+ responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }

            return response;
        }

        @Override
        protected void onPostExecute(final String jsonString) {
            bookLoading.stop();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent main_activity_intent = new Intent(SplashActivity.this, MainActivity.class);
                    main_activity_intent.putExtra("currentDateData", jsonString);
                    SplashActivity.this.startActivity(main_activity_intent);
                    SplashActivity.this.finish();
                }
            }, 1000);
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//                    Intent main_activity_intent = new Intent(SplashActivity.this, FileChooserActivity.class);
//                    SplashActivity.this.startActivity(main_activity_intent);
//                    SplashActivity.this.finish();
//                }
//            }, 1000);
        }
    }
}