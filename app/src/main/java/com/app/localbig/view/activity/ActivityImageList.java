package com.app.localbig.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.localbig.R;
import com.app.localbig.helper.util.Tools;
import com.app.localbig.model.Business;
import com.app.localbig.view.adapter.ImageGalleryAdapter;
import com.app.localbig.view.fragment.SlideshowDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ActivityImageList extends AbstractActivity {

    public static final String EXTRA_OBJCT_BUSINESS = "BUSINESS";
    private View parent_view;
    private Business business;
    private ProgressDialog pDialog;
    private ImageGalleryAdapter mAdapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        parent_view = findViewById(android.R.id.content);

        initActionbar();

        Tools.systemBarLolipop(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(EXTRA_OBJCT_BUSINESS)) {
                Gson gSon = new Gson();
                business = gSon.fromJson(bundle.getString(EXTRA_OBJCT_BUSINESS), new TypeToken<Business>() {
                }.getType());
            }
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        pDialog = new ProgressDialog(this);
        mAdapter = new ImageGalleryAdapter(getApplicationContext(), business.getBusinessPictures());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ImageGalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new ImageGalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance(business, position);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void initActionbar() {

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        TextView tittle = (TextView) findViewById(R.id.tvSearchToolBar_title);
        tittle.setText("BUSINESS IMAGES");
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_bg));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}