package com.app.localbig.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.app.localbig.R;



public class ActivityMap extends ActionBarActivity {
    public static final String EXTRA_POI_ID = "poi_id";
    public static final String EXTRA_POI_LATITUDE = "poi_latitude";
    public static final String EXTRA_POI_LONGITUDE = "poi_longitude";

    public static Intent newIntent(Context context) {
        return new Intent(context, ActivityMap.class);
    }

    public static Intent newIntent(Context context, long poiId, double poiLatitude, double poiLongitude) {
        Intent intent = new Intent(context, ActivityMap.class);
        intent.putExtra(EXTRA_POI_ID, poiId);
        intent.putExtra(EXTRA_POI_LATITUDE, poiLatitude);
        intent.putExtra(EXTRA_POI_LONGITUDE, poiLongitude);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setupActionBar();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
    }
}
