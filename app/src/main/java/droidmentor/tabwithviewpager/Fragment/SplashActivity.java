package droidmentor.tabwithviewpager.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import droidmentor.tabwithviewpager.ViewPager.CustomTabActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, CustomTabActivity.class);
        startActivity(intent);
        finish();
    }
}
