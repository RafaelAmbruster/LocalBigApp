package com.app.localbig.view.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.app.localbig.R;
import com.app.localbig.helper.util.BitmapTransform;
import com.app.localbig.helper.util.Tools;
import com.app.localbig.model.LocalEvent;
import com.app.localbig.view.fragment.EventDetailFragment;
import com.app.localbig.view.widget.CustomFontTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ActivityEventsDetails extends AppCompatActivity {

    public static final String EXTRA_OBJCT_EVENT = "EVENT";
    private ViewPager mViewPager;
    private EventDetailFragment frag_detail;
    private LocalEvent event;
    private int size = (int) Math.ceil(Math.sqrt(BitmapTransform.MAX_WIDTH * BitmapTransform.MAX_HEIGHT));
    private Toolbar toolbar;
    private ActionBar actionBar;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(EXTRA_OBJCT_EVENT)) {
                Gson gSon = new Gson();
                event = gSon.fromJson(bundle.getString(EXTRA_OBJCT_EVENT), new TypeToken<LocalEvent>() {
                }.getType());
            }
        }

        if (event == null) {
            finish();
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (event != null) {
            collapsingToolbarLayout.setTitle(event.getTitle());
        }

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Init();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void Init() {

        ImageView ivImage = (ImageView) findViewById(R.id.ivImage);
        CustomFontTextView tittle = (CustomFontTextView) findViewById(R.id.tittle);
        CustomFontTextView text = (CustomFontTextView) findViewById(R.id.text);

        tittle.setText(event.getTitle().equals("") ? "EVENT" : event.getTitle());
        text.setText(event.getFreeEntrance() ? "FREE ENTRANCE" : "PAID ENTRANCE");

        String url = null;

        if (event.getImagePath() != null) {
            url = event.getImagePath();
            Picasso.with(ActivityEventsDetails.this)
                    .load(url).resize(size, size)
                    .transform(new BitmapTransform(BitmapTransform.MAX_WIDTH, BitmapTransform.MAX_HEIGHT))
                    .centerInside().into(ivImage);
        }

        if (url == null) {
            Picasso.with(this).load("file:///android_asset/placeholder.png").into(ivImage);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (Tools.getAPIVerison() >= 5.0) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void setupViewPager(ViewPager mViewPager) {
        BusinessPagerAdapter adapter = new BusinessPagerAdapter(getSupportFragmentManager());

        if (frag_detail == null) {
            frag_detail = new EventDetailFragment().newInstance(event);
        }

        adapter.addFragment(frag_detail, "Description");

        mViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    static class BusinessPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public BusinessPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
