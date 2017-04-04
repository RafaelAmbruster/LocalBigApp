package com.app.localbig.view.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.localbig.R;
import com.app.localbig.helper.geolocation.Geolocation;
import com.app.localbig.helper.geolocation.GeolocationListener;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.helper.util.Tools;
import com.app.localbig.model.ApplicationUser;
import com.app.localbig.model.Coupon;
import com.app.localbig.rest.ResponseLoadCallBack;
import com.app.localbig.rest.ResponseObjectCallBack;
import com.app.localbig.rest.task.coupon.CouponTask;
import com.app.localbig.view.adapter.CouponAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ActivityCouponList extends AbstractActivity implements
        ResponseLoadCallBack,
        ResponseObjectCallBack,
        SwipeRefreshLayout.OnRefreshListener,
        GeolocationListener, View.OnClickListener {

    public static final String EXTRA_OBJCT_USER = "User";
    private static final long TIMER_DELAY = 60000l;
    private View parent_view;
    private RecyclerView recyclerView;
    private CouponAdapter adapter;
    private ApplicationUser us;
    public static ArrayList<Coupon> events;
    private RelativeLayout ryt_empty, ryt_error, ryt_connection, ryt_progressbar;
    private SearchView searchView;
    private SwipeRefreshLayout refresh;
    private static View view;
    private Geolocation mGeolocation = null;
    private Location mLocation = null;
    private Handler mTimerHandler;
    private Runnable mTimerRunnable;
    private Button action_retry, error_retry;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        parent_view = findViewById(android.R.id.content);

        initActionbar();

        setupTimer();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(EXTRA_OBJCT_USER)) {
                Gson gSon = new Gson();
                us = gSon.fromJson(bundle.getString(EXTRA_OBJCT_USER), new TypeToken<ApplicationUser>() {
                }.getType());
            }
        }

        Load();
        Tools.systemBarLolipop(this);
    }

    public void initActionbar() {

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        TextView tittle = (TextView) findViewById(R.id.tvSearchToolBar_title);
        tittle.setText("COUPONS");
        toolbar.setBackgroundColor(getResources().getColor(R.color.global_color_green_primary_dark));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void Load() {

        ryt_progressbar = (RelativeLayout) findViewById(R.id.layout_progress);
        ryt_empty = (RelativeLayout) findViewById(R.id.layout_empty);
        ryt_error = (RelativeLayout) findViewById(R.id.layout_error);
        ryt_connection = (RelativeLayout) findViewById(R.id.layout_connection);

        error_retry = (Button) findViewById(R.id.action_error_retry);
        error_retry.setOnClickListener(this);

        action_retry = (Button) findViewById(R.id.action_retry);
        action_retry.setOnClickListener(this);

        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        refresh.setColorSchemeColors(Color.parseColor("#ffc107"));


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(ActivityCouponList.this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        SetupAdapter();

        Run(false);
    }

    private void SetupAdapter() {
        events = new ArrayList<>();
        adapter = new CouponAdapter(ActivityCouponList.this, recyclerView, new CouponAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                int pos = adapter.getPosition(position);
                Coupon coupon = events.get(pos);
                view = v;
                ryt_progressbar.setVisibility(View.VISIBLE);
                OpenDetail(coupon);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void OpenDetail(Coupon coupon) {
        new CouponTask((ResponseObjectCallBack) this).CallService(2, coupon.getId(), null);
    }

    protected void Run(boolean process) {
        if (Tools.isOnline(ActivityCouponList.this, true, false)) {
            ryt_connection.setVisibility(View.GONE);
            Execute(process);
        } else {
            ryt_connection.setVisibility(View.VISIBLE);
        }
    }

    protected void Execute(Boolean manual) {
        if (!manual)
            ryt_progressbar.setVisibility(View.VISIBLE);

        new CouponTask((ResponseLoadCallBack) this).CallService(1, "", null);
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (us != null) {
            getMenuInflater().inflate(R.menu.menu_search, menu);
            final MenuItem searchItem = menu.findItem(R.id.action_search);
            searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setIconified(false);
            searchView.setQueryHint("Search...");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    try {
                        adapter.getFilter().filter(s);
                    } catch (Exception e) {
                    }
                    return true;
                }
            });

            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setItemsVisibility(menu, searchItem, false);
                }
            });


            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    setItemsVisibility(menu, searchItem, true);
                    adapter.notifyDataSetChanged();
                    return false;
                }
            });
            searchView.onActionViewCollapsed();

        } else {
            getMenuInflater().inflate(R.menu.menu_search_offline, menu);
            final MenuItem searchItem = menu.findItem(R.id.action_search);
            searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setIconified(false);
            searchView.setQueryHint("Search...");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    try {
                        adapter.getFilter().filter(s);
                    } catch (Exception e) {
                    }
                    return true;
                }
            });

            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setItemsVisibility(menu, searchItem, false);
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    setItemsVisibility(menu, searchItem, true);
                    adapter.notifyDataSetChanged();
                    return false;
                }
            });
            searchView.onActionViewCollapsed();
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponseLoadCallBack(ArrayList list) {
        ryt_error.setVisibility(View.GONE);
        ryt_empty.setVisibility(View.GONE);
        ryt_connection.setVisibility(View.GONE);
        refresh.setRefreshing(false);
        ryt_progressbar.setVisibility(View.GONE);

        try {
            events.addAll(list);
            adapter.AddItems(events);
            adapter.setLoaded();

            if (events.size() > 0) {
                ryt_empty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.GONE);
                ryt_empty.setVisibility(View.VISIBLE);
            }

        } catch (Exception ex) {
        }
    }

    @Override
    public void onResponseObjectCallBack(Object object) {
        LogManager.getInstance().info("onResponseObjectCallBack", "called");
        ryt_progressbar.setVisibility(View.GONE);
        startCouponDetail((Coupon) object);
    }

    private void startCouponDetail(Coupon object) {
        Intent intent = new Intent(ActivityCouponList.this, ActivityCouponDetails.class);
        Bundle b = new Bundle();
        Gson gSon = new Gson();
        b.putString(ActivityCouponDetails.EXTRA_OBJCT_COUPON, gSon.toJson(object));
        intent.putExtras(b);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onError(String message, Integer code) {
        refresh.setRefreshing(false);
        ryt_progressbar.setVisibility(View.GONE);
        ryt_error.setVisibility(View.VISIBLE);
        events = new ArrayList<>();
        recyclerView.setVisibility(View.GONE);
        LogManager.getInstance().error("Error: ", code + " : " + message);
    }

    @Override
    public void onRefresh() {
        if (Tools.isOnline(ActivityCouponList.this, true, false)) {
            events.clear();
            adapter.clearList();
            Run(true);
        } else {
            refresh.setRefreshing(false);
        }
    }

    @Override
    public void onGeolocationRespond(Geolocation geolocation, final Location location) {
        runTaskCallback(new Runnable() {
            public void run() {
                mLocation = location;
                if (adapter != null && mLocation != null && events != null && events.size() > 0)
                    adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onGeolocationFail(Geolocation geolocation) {

    }

    private void startTimer() {
        mTimerHandler.postDelayed(mTimerRunnable, 0);
    }

    private void setupTimer() {
        mTimerHandler = new Handler();
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {

                mGeolocation = null;
                mGeolocation = new Geolocation((LocationManager) getSystemService(Context.LOCATION_SERVICE), ActivityCouponList.this);

                mTimerHandler.postDelayed(this, TIMER_DELAY);
            }
        };
    }

    private void stopTimer() {
        mTimerHandler.removeCallbacks(mTimerRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
        if (mGeolocation != null) mGeolocation.stop();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.action_retry) {
            Run(false);
        } else if (view.getId() == R.id.action_error_retry) {
            Run(false);
        }
    }
}
