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
import com.app.localbig.model.LocalEvent;
import com.app.localbig.view.widget.ViewState;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Calendar;
import java.util.Date;

public class EventDetailFragment extends BaseFragment implements GeolocationListener {

    private static final long TIMER_DELAY = 60000l;
    private static final int MAP_ZOOM = 14;
    public static final String EXTRA_OBJCT_EVENT = "EVENT";
    private ViewState mViewState = null;
    private Geolocation mGeolocation = null;
    private Location mLocation = null;
    private Handler mTimerHandler;
    private Runnable mTimerRunnable;
    private LocalEvent event;
    private View parent_view;

    TextView tvDateS, tvTimeS, tvDateE, tvTimeE;

    private int size = (int) Math.ceil(Math.sqrt(BitmapTransform.MAX_WIDTH * BitmapTransform.MAX_HEIGHT));

    public static EventDetailFragment newInstance(LocalEvent event) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(EXTRA_OBJCT_EVENT, gSon.toJson(event));
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
        parent_view = inflater.inflate(R.layout.activity_event_information, container, false);
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey(EXTRA_OBJCT_EVENT)) {
                Gson gSon = new Gson();
                event = gSon.fromJson(bundle.getString(EXTRA_OBJCT_EVENT), new TypeToken<LocalEvent>() {
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

    private void renderDate() {

        PaintStartDate(event.getStartDate());
        PaintEndDate(event.getEndDate());

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

        if (event.getTitle() != null && !event.getTitle().trim().equals("")) {
            introTextView.setText(event.getTitle());
            introTextView.setVisibility(View.VISIBLE);
        } else {
            introTextView.setVisibility(View.GONE);
        }

        if (event.getFormattedAddress() != null && !event.getFormattedAddress().trim().equals("")) {
            addressTextView.setText(event.getFormattedAddress());
            addressTextView.setVisibility(View.VISIBLE);
            addressTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startMapActivity(event);
                }
            });
        } else {
            addressTextView.setVisibility(View.GONE);
        }

        if (mLocation != null) {
            LatLng myLocation = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
            LatLng poiLocation = new LatLng(Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude()));
            String distance = LocationUtility.getDistanceString(LocationUtility.getDistance(myLocation, poiLocation), LocationUtility.isMetricSystem());
            distanceTextView.setText(distance);
            distanceTextView.setVisibility(View.VISIBLE);
            distanceTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNavigateActivity(Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude()));
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
        String url = getStaticMapUrl(key, Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude()), MAP_ZOOM);

        Picasso.with(getActivity())
                .load(url).resize(size, size)
                .placeholder(R.drawable.placeholder_map)
                .error(R.drawable.placeholder_map)
                .transform(new BitmapTransform(BitmapTransform.MAX_WIDTH, BitmapTransform.MAX_HEIGHT)).
                centerInside().into(imageView);

        wrapViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMapActivity(event);
            }
        });
    }

    private void renderViewDescription() {
        TextView descriptionTextView = (TextView) parent_view.findViewById(R.id.fragment_poi_detail_description_text);
        descriptionTextView.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        if (event.getDescription() != null && !event.getDescription().trim().equals("")) {
            descriptionTextView.setText(event.getDescription());
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
                if (event != null)
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
                mGeolocation = new Geolocation((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE), EventDetailFragment.this);

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

   private String getPoiText() {
        StringBuilder builder = new StringBuilder();
        builder.append(event.getTitle());
        builder.append("\n\n");
        if (event.getFormattedAddress() != null && !event.getFormattedAddress().trim().equals("")) {
            builder.append(event.getFormattedAddress());
            builder.append("\n\n");
        }
        if (event.getDescription() != null && !event.getDescription().trim().equals("")) {
            builder.append(event.getDescription());
            builder.append("\n\n");
        }

        return builder.toString();
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

    private void startMapActivity(LocalEvent business) {
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
}
