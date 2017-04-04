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
import com.app.localbig.model.Coupon;
import com.app.localbig.view.fragment.CouponDetailFragment;
import com.app.localbig.view.tagview.TagView;
import com.app.localbig.view.widget.CustomFontTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ActivityCouponDetails extends AppCompatActivity {

    public static final String EXTRA_OBJCT_COUPON = "COUPON";
    private ViewPager mViewPager;
    private CouponDetailFragment frag_detail;
    private Coupon coupon;
    private int size = (int) Math.ceil(Math.sqrt(BitmapTransform.MAX_WIDTH * BitmapTransform.MAX_HEIGHT));

    private Toolbar toolbar;
    private ActionBar actionBar;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(EXTRA_OBJCT_COUPON)) {
                Gson gSon = new Gson();
                coupon = gSon.fromJson(bundle.getString(EXTRA_OBJCT_COUPON), new TypeToken<Coupon>() {
                }.getType());
            }
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (coupon != null) {
            collapsingToolbarLayout.setTitle("Coupon " + coupon.getDiscount() + (coupon.getInPercent() ? "%" : ""));
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

        String url = null;
        if (coupon.getImagePath() != null) {
            url = coupon.getImagePath();
            Picasso.with(ActivityCouponDetails.this).load(url).resize(size, size).transform(new BitmapTransform(BitmapTransform.MAX_WIDTH, BitmapTransform.MAX_HEIGHT)).centerInside().into(ivImage);
        }

        if (url == null) {
            Picasso.with(this).load("file:///android_asset/placeholder.png").into(ivImage);
        }

        CustomFontTextView tittle = (CustomFontTextView) findViewById(R.id.tittle);
        CustomFontTextView text = (CustomFontTextView) findViewById(R.id.text);

        tittle.setText(coupon.getPlaceOrBusiness() == null ? "BUSINESS" : coupon.getPlaceOrBusiness());
        text.setText("Coupon " + coupon.getDiscount() + (coupon.getInPercent() ? "%" : ""));

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        TagView text_active = (TagView) findViewById(R.id.text_active);
        if (!coupon.getActive()) {
            text_active.setText("Inactive");
            text_active.setTagColor(0xFFFF0000);
        } else {
            text_active.setText("Active");
            text_active.setTagColor(0xFFFFBB33);
        }

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
            frag_detail = new CouponDetailFragment().newInstance(coupon);
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
