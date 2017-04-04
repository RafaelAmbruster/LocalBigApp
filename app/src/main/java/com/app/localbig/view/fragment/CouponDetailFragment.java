package com.app.localbig.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.app.localbig.helper.geolocation.Geolocation;
import com.app.localbig.helper.geolocation.GeolocationListener;
import com.app.localbig.helper.util.BitmapTransform;
import com.app.localbig.helper.util.CustomDateFormat;
import com.app.localbig.helper.util.DataFormatter;
import com.app.localbig.helper.util.FontTypefaceUtils;
import com.app.localbig.helper.util.LocationUtility;
import com.app.localbig.model.Business;
import com.app.localbig.model.Coupon;
import com.app.localbig.rest.ResponseObjectCallBack;
import com.app.localbig.rest.task.business.BusinessTask;
import com.app.localbig.view.tagview.TagView;
import com.app.localbig.view.widget.ViewState;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Calendar;
import java.util.Date;

public class CouponDetailFragment extends BaseFragment implements GeolocationListener, ResponseObjectCallBack {

    private static final long TIMER_DELAY = 60000l;
    private static final int MAP_ZOOM = 14;
    public static final String EXTRA_OBJCT_COUPON = "COUPON";
    private ViewState mViewState = null;
    private Geolocation mGeolocation = null;
    private Location mLocation = null;
    private Handler mTimerHandler;
    private Runnable mTimerRunnable;
    private Coupon coupon;
    private View parent_view;

    private static Business owner;

    TextView tvDateS, tvTimeS, tvDateE, tvTimeE;

    private int size = (int) Math.ceil(Math.sqrt(BitmapTransform.MAX_WIDTH * BitmapTransform.MAX_HEIGHT));

    public static CouponDetailFragment newInstance(Coupon event) {
        CouponDetailFragment fragment = new CouponDetailFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(EXTRA_OBJCT_COUPON, gSon.toJson(event));
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
        parent_view = inflater.inflate(R.layout.activity_coupon_information, container, false);
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey(EXTRA_OBJCT_COUPON)) {
                Gson gSon = new Gson();
                coupon = gSon.fromJson(bundle.getString(EXTRA_OBJCT_COUPON), new TypeToken<Coupon>() {
                }.getType());
            }
        }

        LoadBusiness();
        renderView();
        return parent_view;
    }

    private void LoadBusiness() {
        new BusinessTask(this).CallService(2, coupon.getBusinessId(), null, null);
    }

    private void renderView() {
        renderDate();
        renderViewDescription();
    }

    private void renderDate() {
        PaintStartDate(coupon.getStartDate());
        PaintEndDate(coupon.getEndDate());
    }

    private void PaintStartDate(String date) {
        tvDateS = (TextView) parent_view.findViewById(R.id.tvEvent_details_event_date);
        tvDateS.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        tvTimeS = (TextView) parent_view.findViewById(R.id.tvEvent_details_event_time);
        tvTimeS.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));

        org.joda.time.DateTime dateTime_Utc = new DateTime(date, DateTimeZone.UTC);
        Date created = dateTime_Utc.toDate();

        if (created != null) {

            String month = CustomDateFormat.format(created, "MMMMMM yyyy");
            String day = CustomDateFormat.format(created, "d");
            String day_of_week = CustomDateFormat.format(created, "EEEE");
            String hour = CustomDateFormat.format(created, "h");
            String minute = CustomDateFormat.format(created, "mm");
            String am_pm = CustomDateFormat.format(created, "a");

            tvDateS.setText(day_of_week +", " + day + " " + month);

            tvTimeS.setText(hour + ":" + minute + " " + am_pm);
        }
    }

    private void PaintEndDate(String date) {

        tvDateE = (TextView) parent_view.findViewById(R.id.tvEvent_details_event_date_e);
        tvDateE.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        tvTimeE = (TextView) parent_view.findViewById(R.id.tvEvent_details_event_time_e);
        tvTimeE.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));

        org.joda.time.DateTime dateTime_Utc = new DateTime(date, DateTimeZone.UTC);
        Date created = dateTime_Utc.toDate();

        if (created != null) {

            String month = CustomDateFormat.format(created, "MMMMMM yyyy");
            String day = CustomDateFormat.format(created, "d");
            String day_of_week = CustomDateFormat.format(created, "EEEE");
            String hour = CustomDateFormat.format(created, "h");
            String minute = CustomDateFormat.format(created, "mm");
            String am_pm = CustomDateFormat.format(created, "a");

            tvDateE.setText(day_of_week +", " + day + " " + month);
            tvTimeE.setText(hour + ":" + minute + " " + am_pm);
        }
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

        if (coupon.getPlaceOrBusiness() != null && !coupon.getPlaceOrBusiness().trim().equals("")) {
            introTextView.setText(coupon.getPlaceOrBusiness());
            introTextView.setVisibility(View.VISIBLE);
        } else {
            introTextView.setVisibility(View.GONE);
        }

        if (owner.getFormattedAddress() != null && !owner.getFormattedAddress().trim().equals("")) {
            addressTextView.setText(owner.getFormattedAddress());
            addressTextView.setVisibility(View.VISIBLE);
            addressTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startMapActivity(owner);
                }
            });
        } else {
            addressTextView.setVisibility(View.GONE);
        }

        if (mLocation != null) {
            LatLng myLocation = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            LatLng poiLocation = new LatLng(Double.parseDouble(owner.getLatitude()), Double.parseDouble(owner.getLongitude()));
            String distance = LocationUtility.getDistanceString(LocationUtility.getDistance(myLocation, poiLocation), LocationUtility.isMetricSystem());
            distanceTextView.setText(distance);
            distanceTextView.setVisibility(View.VISIBLE);
            distanceTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNavigateActivity(Double.parseDouble(owner.getLatitude()), Double.parseDouble(owner.getLongitude()));
                }
            });
        } else {
            distanceTextView.setVisibility(View.GONE);
        }

        linkTextView.setVisibility(View.GONE);
        phoneTextView.setVisibility(View.GONE);

    }

    private void renderViewMap() {
        final ImageView imageView = (ImageView) parent_view.findViewById(R.id.fragment_poi_detail_map_image);
        final ViewGroup wrapViewGroup = (ViewGroup) parent_view.findViewById(R.id.fragment_poi_detail_map_image_wrap);

        String key = getString(R.string.google_maps_key);
        String url = getStaticMapUrl(key, Double.parseDouble(owner.getLatitude()), Double.parseDouble(owner.getLongitude()), MAP_ZOOM);
        //mImageLoader.displayImage(url, imageView, mDisplayImageOptions, mImageLoadingListener);

        Picasso.with(getActivity())
                .load(url).resize(size, size)
                .placeholder(R.drawable.placeholder_map)
                .error(R.drawable.placeholder_map)
                .transform(new BitmapTransform(BitmapTransform.MAX_WIDTH, BitmapTransform.MAX_HEIGHT)).
                centerInside().into(imageView);


        wrapViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMapActivity(owner);
            }
        });
    }

    private void renderViewDescription() {
        TextView descriptionTextView = (TextView) parent_view.findViewById(R.id.fragment_poi_detail_description_text);
        descriptionTextView.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        if (coupon.getDescription() != null && !coupon.getDescription().trim().equals("")) {
            descriptionTextView.setText(coupon.getDescription());
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
                //if (coupon != null)
                    //renderViewInfo();
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
                mGeolocation = new Geolocation((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE), CouponDetailFragment.this);

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

   /* private String getPoiText() {
        StringBuilder builder = new StringBuilder();
        builder.append(mPoi.getName());
        builder.append("\n\n");
        if (mPoi.getAddress() != null && !mPoi.getAddress().trim().equals("")) {
            builder.append(mPoi.getAddress());
            builder.append("\n\n");
        }
        if (mPoi.getIntro() != null && !mPoi.getIntro().trim().equals("")) {
            builder.append(mPoi.getIntro());
            builder.append("\n\n");
        }
        if (mPoi.getDescription() != null && !mPoi.getDescription().trim().equals("")) {
            builder.append(mPoi.getDescription());
            builder.append("\n\n");
        }
        if (mPoi.getLink() != null && !mPoi.getLink().trim().equals("")) {
            builder.append(mPoi.getLink());
        }
        return builder.toString();
    }*/

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

    private void startShareActivity(String subject, String text) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
        }
    }

    private void startWebActivity(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
        }
    }

    private void startCallActivity(String phoneNumber) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("tel:");
            builder.append(phoneNumber);

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(builder.toString()));
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            // can't start activity
        }
    }

    private void startEmailActivity(String email) {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("mailto:");
            builder.append(email);

            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(builder.toString()));
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            // can't start activity
        }
    }

    private void startNavigateActivity(double lat, double lon) {
        try {
            String uri = String.format("http://maps.google.com/maps?daddr=%s,%s", Double.toString(lat), Double.toString(lon));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
        }
    }

    @Override
    public void onResponseObjectCallBack(Object object) {
        owner = (Business) object;
        renderViewInfo();
        renderViewMap();
    }

    @Override
    public void onError(String message, Integer code) {

    }
}
