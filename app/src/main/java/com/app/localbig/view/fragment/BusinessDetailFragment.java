package com.app.localbig.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.localbig.R;
import com.app.localbig.data.AppDatabaseManager;
import com.app.localbig.data.dao.BusinessDAO;
import com.app.localbig.data.dao.IOperationDAO;
import com.app.localbig.helper.geolocation.Geolocation;
import com.app.localbig.helper.geolocation.GeolocationListener;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.helper.manager.ConfigurationManager;
import com.app.localbig.helper.util.BitmapTransform;
import com.app.localbig.helper.util.CustomDateFormat;
import com.app.localbig.helper.util.FontTypefaceUtils;
import com.app.localbig.helper.util.LocationUtility;
import com.app.localbig.model.ApplicationUser;
import com.app.localbig.model.Business;
import com.app.localbig.view.widget.ViewState;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;


public class BusinessDetailFragment extends BaseFragment implements GeolocationListener {

    private static final long TIMER_DELAY = 60000l;
    private static final int MAP_ZOOM = 14;
    public static final String EXTRA_OBJCT_BUSINESS = "BUSINESS";
    public static final String EXTRA_OBJCT_USER = "USER";
    private Geolocation mGeolocation = null;
    private Location mLocation = null;
    private Handler mTimerHandler;
    private Runnable mTimerRunnable;
    private Business business;
    private ApplicationUser user;
    private View parent_view;

    private TextView tvAmPm;
    private TextView tvDay;
    private TextView tvDayOfWeek;
    private TextView tvHour;
    private TextView tvMinute;
    private TextView tvMonthAndYear;

    private int size = (int) Math.ceil(Math.sqrt(BitmapTransform.MAX_WIDTH * BitmapTransform.MAX_HEIGHT));

    public static BusinessDetailFragment newInstance(Business business, ApplicationUser user) {
        BusinessDetailFragment fragment = new BusinessDetailFragment();
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
        startTimer();
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent_view = inflater.inflate(R.layout.activity_business_information, container, false);
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

        renderView();
        return parent_view;
    }

    private void renderView() {
        renderDate();
        renderViewInfo();
        renderViewMap();
        renderViewDescription();
    }

    private void PaintDate(String date) {
        org.joda.time.DateTime dateTime_Utc = new DateTime(date, DateTimeZone.UTC);
        Date created = dateTime_Utc.toDate();

        tvDayOfWeek.setText(CustomDateFormat.format(created, "EEEE"));
        tvDay.setText(CustomDateFormat.format(created, "d"));
        tvMonthAndYear.setText(CustomDateFormat.format(created, "MMMMMM yyyy"));
        tvHour.setText(CustomDateFormat.format(created, "h"));
        tvMinute.setText(CustomDateFormat.format(created, "mm"));
        tvAmPm.setText(CustomDateFormat.format(created, "a"));
    }

    private void renderDate() {
        tvDayOfWeek = (TextView) parent_view.findViewById(R.id.widget_details_header_dayofweek);
        tvDayOfWeek.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        tvDay = (TextView) parent_view.findViewById(R.id.widget_details_header_day);
        tvMonthAndYear = (TextView) parent_view.findViewById(R.id.widget_details_header_monthandyear);
        tvMonthAndYear.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        tvHour = (TextView) parent_view.findViewById(R.id.widget_details_header_hour);
        tvMinute = (TextView) parent_view.findViewById(R.id.widget_details_header_minute);
        tvAmPm = (TextView) parent_view.findViewById(R.id.widget_details_header_ampm);

        PaintDate(business.getSinceDate());
    }

    private void renderViewInfo() {

        TextView introTextView = (TextView) parent_view.findViewById(R.id.fragment_poi_detail_info_intro);
        introTextView.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        TextView addressTextView = (TextView) parent_view.findViewById(R.id.fragment_poi_detail_info_address);
        addressTextView.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        TextView distanceTextView = (TextView) parent_view.findViewById(R.id.fragment_poi_detail_info_distance);
        distanceTextView.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        TextView linkTextView = (TextView) parent_view.findViewById(R.id.fragment_poi_detail_info_link);
        linkTextView.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        TextView phoneTextView = (TextView) parent_view.findViewById(R.id.fragment_poi_detail_info_phone);
        phoneTextView.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));

        if (business.getTitle() != null && !business.getTitle().trim().equals("")) {
            introTextView.setText(business.getTitle());
            introTextView.setVisibility(View.VISIBLE);
        } else {
            introTextView.setVisibility(View.GONE);
        }

        if (business.getFormattedAddress() != null && !business.getFormattedAddress().trim().equals("")) {
            addressTextView.setText(business.getFormattedAddress());
            addressTextView.setVisibility(View.VISIBLE);
            addressTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startMapActivity(business);
                }
            });
        } else {
            addressTextView.setVisibility(View.GONE);
        }

        if (mLocation != null) {
            LatLng myLocation = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            LatLng poiLocation = new LatLng(Double.parseDouble(business.getLatitude()), Double.parseDouble(business.getLongitude()));
            String distance = LocationUtility.getDistanceString(LocationUtility.getDistance(myLocation, poiLocation), LocationUtility.isMetricSystem());
            distanceTextView.setText(distance);
            distanceTextView.setVisibility(View.VISIBLE);
            distanceTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNavigateActivity(Double.parseDouble(business.getLatitude()), Double.parseDouble(business.getLongitude()));
                }
            });
        } else {
            distanceTextView.setVisibility(View.GONE);
        }

        if (business.getWebSiteUrl() != null && !business.getWebSiteUrl().trim().equals("")) {
            linkTextView.setText(business.getWebSiteUrl());
            linkTextView.setVisibility(View.VISIBLE);
            linkTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startWebActivity(business.getWebSiteUrl());
                }
            });
        } else {
            linkTextView.setVisibility(View.GONE);
        }

        if (business.getPhoneNumber() != null && !business.getPhoneNumber().trim().equals("")) {
            phoneTextView.setText(business.getPhoneNumber());
            phoneTextView.setVisibility(View.VISIBLE);
            phoneTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCallActivity(business.getPhoneNumber());
                }
            });
        } else {
            phoneTextView.setVisibility(View.GONE);
        }
    }

    private void renderViewMap() {
        final ImageView imageView = (ImageView) parent_view.findViewById(R.id.fragment_poi_detail_map_image);
        final ViewGroup wrapViewGroup = (ViewGroup) parent_view.findViewById(R.id.fragment_poi_detail_map_image_wrap);

        String key = getString(R.string.google_maps_key);
        String url = getStaticMapUrl(key, Double.parseDouble(business.getLatitude()), Double.parseDouble(business.getLongitude()), MAP_ZOOM);
        Picasso.with(getActivity())
                .load(url).resize(size, size)
                .placeholder(R.drawable.placeholder_map)
                .error(R.drawable.placeholder_map)
                .transform(new BitmapTransform(BitmapTransform.MAX_WIDTH, BitmapTransform.MAX_HEIGHT)).
                centerInside().into(imageView);

        wrapViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMapActivity(business);
            }
        });
    }

    private void renderViewDescription() {
        TextView descriptionTextView = (TextView) parent_view.findViewById(R.id.fragment_poi_detail_description_text);
        descriptionTextView.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        if (business.getDescription() != null && !business.getDescription().trim().equals("")) {
            descriptionTextView.setText(business.getDescription());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupTimer();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
        if (mGeolocation != null) mGeolocation.stop();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onGeolocationRespond(Geolocation geolocation, final Location location) {
        runTaskCallback(new Runnable() {
            public void run() {
                mLocation = location;
                if (business != null)
                    renderViewInfo();
            }
        });
    }

    @Override
    public void onGeolocationFail(Geolocation geolocation) {
    }

    private void setupTimer() {
        mTimerHandler = new Handler();
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {
                mGeolocation = null;
                mGeolocation = new Geolocation((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE), BusinessDetailFragment.this);

                mTimerHandler.postDelayed(this, TIMER_DELAY);
            }
        };
    }

    private void startTimer() {
        mTimerHandler.postDelayed(mTimerRunnable, 0);
    }

    private void stopTimer() {
        mTimerHandler.removeCallbacks(mTimerRunnable);
    }

    private String getStaticMapUrl(String key, double lat, double lon, int zoom) {
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        int markerColor = typedValue.data;
        String markerColorHex = String.format("0x%06x", (0xffffff & markerColor));

        StringBuilder builder = new StringBuilder();
        builder.append("https://maps.googleapis.com/maps/api/staticmap");
        builder.append("?key=");
        builder.append(key);
        builder.append("&size=320x320");
        builder.append("&scale=2");
        builder.append("&maptype=roadmap");
        builder.append("&zoom=");
        builder.append(zoom);
        builder.append("&center=");
        builder.append(lat);
        builder.append(",");
        builder.append(lon);
        builder.append("&markers=color:");
        builder.append(markerColorHex);
        builder.append("%7C");
        builder.append(lat);
        builder.append(",");
        builder.append(lon);
        return builder.toString();
    }

    private void startMapActivity(Business business) {
        //Intent intent = MapActivity.newIntent(getActivity(), poi.getId(), poi.getLatitude(), poi.getLongitude());
        //startActivity(intent);
    }

    private void startWebActivity(String url) {
        try {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
        }
    }

    private void startCallActivity(String phoneNumber) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("tel:");
            builder.append(phoneNumber);

            Intent intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse(builder.toString()));
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            // can't start activity
        }
    }

    private void startNavigateActivity(double lat, double lon) {
        try {
            String uri = String.format("http://maps.google.com/maps?daddr=%s,%s", Double.toString(lat), Double.toString(lon));
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
        }
    }

}
