package project.com.pockethistory;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.victor.loading.book.BookLoading;
import com.victor.loading.newton.NewtonCradleLoading;
import com.victor.loading.rotate.RotateLoading;

public class SplashActivity extends ActionBarActivity {
    private BookLoading bookLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bookLoading = (BookLoading) findViewById(R.id.bookloading);

        bookLoading.start();
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                Intent main_activity_intent = new Intent(SplashActivity.this, MainActivity.class);
//                SplashActivity.this.startActivity(main_activity_intent);
//                SplashActivity.this.finish();
//            }
//        }, 1000);
    }
}
