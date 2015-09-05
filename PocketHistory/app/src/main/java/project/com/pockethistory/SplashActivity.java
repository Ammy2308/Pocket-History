package project.com.pockethistory;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.victor.loading.book.BookLoading;

public class SplashActivity extends AppCompatActivity {
    private BookLoading bookLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bookLoading = (BookLoading) findViewById(R.id.bookloading);

        new CurrentDateAsyncTask().execute();
    }

    public class CurrentDateAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            bookLoading.start();
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    bookLoading.stop();
                    Intent main_activity_intent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(main_activity_intent);
                    SplashActivity.this.finish();
                }
            }, 5000);
        }
    }
}