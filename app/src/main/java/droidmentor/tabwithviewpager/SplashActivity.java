package droidmentor.tabwithviewpager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import droidmentor.tabwithviewpager.Fragment.CallsFragment;
import droidmentor.tabwithviewpager.ViewPager.CustomTabActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, CustomTabActivity.class);
        startActivity(intent);
    }
}