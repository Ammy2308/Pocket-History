package project.com.pockethistory;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent main_activity_intent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(main_activity_intent);
                SplashActivity.this.finish();
            }
        }, 1000);
    }
}
