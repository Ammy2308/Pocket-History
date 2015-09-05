package project.com.pockethistory;

import android.content.Intent;
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

        bookLoading.start();
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
