package com.app.localbig.view.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.app.localbig.R;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.model.ApplicationUser;
import com.app.localbig.model.Business;
import com.app.localbig.model.Coupon;
import com.app.localbig.rest.ResponseObjectCallBack;
import com.app.localbig.rest.task.coupon.CouponTask;
import com.app.localbig.view.activity.ActivityCouponDetails;
import com.app.localbig.view.adapter.CouponAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Ambruster on 29/06/2016.
 */

public class BusinessCouponsFragment extends BaseFragment implements View.OnClickListener, ResponseObjectCallBack {

    private CouponAdapter adapter;
    private ArrayList<Coupon> coupons;
    public static final String EXTRA_OBJCT_BUSINESS = "BUSINESS";
    public static final String EXTRA_OBJCT_USER = "USER";
    public Business business;
    private ApplicationUser user;
    private RecyclerView recycler;
    private ProgressBar progressbar;
    private RelativeLayout ryt_empty, ryt_error;
    private Button action_retry, error_retry;
    private static View view;
    private View parent_view;

    public static BusinessCouponsFragment newInstance(Business business, ApplicationUser user) {
        BusinessCouponsFragment fragment = new BusinessCouponsFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(EXTRA_OBJCT_BUSINESS, gSon.toJson(business));
        args.putString(EXTRA_OBJCT_USER, gSon.toJson(user));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        getActivity().invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent_view = inflater.inflate(R.layout.activity_business_coupons, container, false);
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey(EXTRA_OBJCT_BUSINESS)) {
                Gson gSon = new Gson();
                business = gSon.fromJson(bundle.getString(EXTRA_OBJCT_BUSINESS), new TypeToken<Business>() {
                }.getType());
            }


            if (bundle.containsKey(EXTRA_OBJCT_USER)) {
                Gson gSon = new Gson();
                user = gSon.fromJson(bundle.getString(EXTRA_OBJCT_USER), new TypeToken<ApplicationUser>() {
                }.getType());
            }
        }

        Load();

        return parent_view;
    }

    protected void Load() {
        getActivity().invalidateOptionsMenu();
        progressbar = (ProgressBar) parent_view.findViewById(R.id.progressbar);
        ryt_empty = (RelativeLayout) parent_view.findViewById(R.id.layout_empty);
        ryt_error = (RelativeLayout) parent_view.findViewById(R.id.layout_error);
        error_retry = (Button) parent_view.findViewById(R.id.action_error_retry);
        error_retry.setOnClickListener(this);

        recycler = (RecyclerView) parent_view.findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        coupons = new ArrayList<>();
        adapter = new CouponAdapter(getActivity(), recycler, new CouponAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                int pos = adapter.getPosition(position);
                Coupon coupon = coupons.get(pos);
                view = v;
                progressbar.setVisibility(View.VISIBLE);
                OpenDetail(coupon);
            }
        });
        recycler.setAdapter(adapter);
        Execute();
    }

    protected void Execute() {
        ryt_error.setVisibility(View.GONE);
        progressbar.setVisibility(View.GONE);
        try {
            if (business.getCoupons() != null) {

                runTaskCallback(new Runnable() {
                    public void run() {
                        coupons.addAll(business.getCoupons());
                        adapter.AddItems(coupons);
                        adapter.setLoaded();

                        if (coupons.size() > 0) {
                            ryt_empty.setVisibility(View.GONE);
                            recycler.setVisibility(View.VISIBLE);
                        } else {
                            recycler.setVisibility(View.GONE);
                            ryt_empty.setVisibility(View.VISIBLE);
                        }
                    }
                });
            } else {
                recycler.setVisibility(View.GONE);
                ryt_empty.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.action_retry) {
            Execute();
        } else if (v.getId() == R.id.action_error_retry) {
            Execute();
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void OpenDetail(Coupon coupon) {
        new CouponTask(this).CallService(2, coupon.getId(), null);
    }

    @Override
    public void onResponseObjectCallBack(Object object) {
        progressbar.setVisibility(View.GONE);
        startCouponDetail((Coupon) object);
    }

    private void startCouponDetail(Coupon object) {
        Intent intent = new Intent(getActivity(), ActivityCouponDetails.class);
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
        progressbar.setVisibility(View.GONE);
        ryt_error.setVisibility(View.VISIBLE);
        coupons = new ArrayList<>();
        recycler.setVisibility(View.GONE);
        LogManager.getInstance().error("Error: ", code + " : " + message);
    }
}