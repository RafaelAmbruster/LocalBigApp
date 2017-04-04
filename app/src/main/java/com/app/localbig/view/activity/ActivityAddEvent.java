package com.app.localbig.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.app.localbig.R;
import com.app.localbig.helper.geolocation.Geolocation;
import com.app.localbig.helper.geolocation.GeolocationListener;
import com.app.localbig.helper.util.BitmapTransform;
import com.app.localbig.helper.util.CustomDateFormat;
import com.app.localbig.helper.util.DataFormatter;
import com.app.localbig.helper.util.FontTypefaceUtils;
import com.app.localbig.helper.util.Tools;
import com.app.localbig.model.ApplicationUser;
import com.app.localbig.model.LocalEvent;
import com.app.localbig.rest.ResponseObjectCallBack;
import com.app.localbig.rest.task.event.EventTask;
import com.app.localbig.view.image.Patio;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;
import com.vistrav.ask.Ask;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityAddEvent extends AbstractActivity implements
        View.OnClickListener, Patio.PatioCallbacks, GeolocationListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,
        ResponseObjectCallBack {

    public static final String EXTRA_OBJCT_USER = "USER";
    public static final int REQUEST_CODE_TAKE_PICTURE = 1000;
    public static final int REQUEST_CODE_ATTACH_PICTURE = 2000;
    private static final long TIMER_DELAY = 60000l;
    private static final int MAP_ZOOM = 14;
    private static final int PLACE_PICKER_REQUEST = 1;

    private Toolbar toolbar;
    private ActionBar actionBar;
    private EditText tittle, description, direction;
    private View parent_view;
    private Patio mPatio;
    private LocalEvent event;
    private ApplicationUser us;
    private SwitchCompat free_entrance;
    private TextInputLayout tittleWrapper;
    private TextInputLayout descriptionWrapper;
    private TextInputLayout directionWrapper;
    private MaterialDialog progress;
    private Geolocation mGeolocation = null;
    private Location mLocation = null;
    private Handler mTimerHandler;
    private Runnable mTimerRunnable;
    private TextView event_description, event_information, event_picture, event_free_entrance, event_start_date_dec, event_start_date_dec_end, event_address_description;
    private TextView tvAmPm;
    private TextView tvDay;
    private TextView tvDayOfWeek;
    private TextView tvHour;
    private TextView tvMinute;
    private TextView tvMonthAndYear;
    private TextView tvAmPmE;
    private TextView tvDayE;
    private TextView tvDayOfWeekE;
    private TextView tvHourE;
    private TextView tvMinuteE;
    private TextView tvMonthAndYearE;
    private LinearLayout pick_start_date, pick_start_time, pick_end_date, pick_end_time;
    private Boolean datestart, dateend, timestart, timeend;
    private int size = (int) Math.ceil(Math.sqrt(BitmapTransform.MAX_WIDTH * BitmapTransform.MAX_HEIGHT));
    private Place place;
    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private CharSequence name, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        setupTimer();
        initActionbar();
        Load();
        Tools.systemBarLolipop(this);


    }

    private void Load() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(EXTRA_OBJCT_USER)) {
                Gson gSon = new Gson();
                us = gSon.fromJson(bundle.getString(EXTRA_OBJCT_USER), new TypeToken<ApplicationUser>() {
                }.getType());
            }
        }

        parent_view = findViewById(android.R.id.content);
        event_address_description = (TextView) findViewById(R.id.event_address_description);
        event_address_description.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        event_description = (TextView) findViewById(R.id.event_description);
        event_description.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        event_information = (TextView) findViewById(R.id.event_information);
        event_information.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        event_picture = (TextView) findViewById(R.id.event_picture);
        event_picture.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        event_free_entrance = (TextView) findViewById(R.id.event_free_entrance);
        event_free_entrance.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        event_start_date_dec = (TextView) findViewById(R.id.event_start_date_desc);
        event_start_date_dec.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        event_start_date_dec_end = (TextView) findViewById(R.id.event_start_date_desc_end);
        event_start_date_dec_end.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));

        direction = (EditText) findViewById(R.id.event_direction);
        direction.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));

        tittle = (EditText) findViewById(R.id.tittle);
        tittle.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        tittle.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!validate(s.toString())) {
                    tittleWrapper.setErrorEnabled(false);
                }
            }
        });
        description = (EditText) findViewById(R.id.description);
        description.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!validate(s.toString())) {
                    descriptionWrapper.setErrorEnabled(false);
                }
            }
        });

        free_entrance = (SwitchCompat) findViewById(R.id.free_entrance);
        mPatio = (Patio) findViewById(R.id.patio);
        mPatio.setCallbacksListener(this);

        tittleWrapper = (TextInputLayout) findViewById(R.id.tittleWrapper);
        tittleWrapper.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        descriptionWrapper = (TextInputLayout) findViewById(R.id.descriptionWrapper);
        descriptionWrapper.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        directionWrapper = (TextInputLayout) findViewById(R.id.directionWrapper);
        directionWrapper.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));

        pick_start_date = (LinearLayout) findViewById(R.id.pick_start_date);
        pick_start_time = (LinearLayout) findViewById(R.id.pick_start_time);
        pick_end_date = (LinearLayout) findViewById(R.id.pick_end_date);
        pick_end_time = (LinearLayout) findViewById(R.id.pick_end_time);

        pick_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datestart = true;
                dateend = false;
                getDate("PICK START EVENT DATE");
            }
        });

        pick_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timestart = true;
                timeend = false;
                getTime("PICK START EVENT TIME");
            }
        });

        pick_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datestart = false;
                dateend = true;
                getDate("PICK END EVENT DATE");
            }
        });

        pick_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timestart = false;
                timeend = true;
                getTime("PICK END EVENT TIME");
            }
        });

        tvDayOfWeek = (TextView) parent_view.findViewById(R.id.widget_details_header_dayofweek);
        tvDayOfWeek.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        tvDay = (TextView) parent_view.findViewById(R.id.widget_details_header_day);
        tvMonthAndYear = (TextView) parent_view.findViewById(R.id.widget_details_header_monthandyear);
        tvMonthAndYear.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        tvHour = (TextView) parent_view.findViewById(R.id.widget_details_header_hour);
        tvMinute = (TextView) parent_view.findViewById(R.id.widget_details_header_minute);
        tvAmPm = (TextView) parent_view.findViewById(R.id.widget_details_header_ampm);

        tvDayOfWeekE = (TextView) parent_view.findViewById(R.id.widget_details_header_dayofweek_end);
        tvDayOfWeekE.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        tvDayE = (TextView) parent_view.findViewById(R.id.widget_details_header_day_end);
        tvMonthAndYearE = (TextView) parent_view.findViewById(R.id.widget_details_header_monthandyear_end);
        tvMonthAndYearE.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(ActivityAddEvent.this));
        tvHourE = (TextView) parent_view.findViewById(R.id.widget_details_header_hour_end);
        tvMinuteE = (TextView) parent_view.findViewById(R.id.widget_details_header_minute_end);
        tvAmPmE = (TextView) parent_view.findViewById(R.id.widget_details_header_ampm_end);

        SetDefaultStart();
        SetDefaultEnd();

        Button pickerButton = (Button) findViewById(R.id.add_address);
        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    if (mLocation != null)
                        intentBuilder.setLatLngBounds(toBounds(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 10));

                    Intent intent = intentBuilder.build(ActivityAddEvent.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LatLngBounds toBounds(LatLng center, double radius) {
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        return new LatLngBounds(southwest, northeast);
    }

    private void SetDefaultStart() {
        tvHour.setText(CustomDateFormat.format(startDate.getTime(), "h"));
        tvMinute.setText(CustomDateFormat.format(startDate.getTime(), "mm"));
        tvAmPm.setText(CustomDateFormat.format(startDate.getTime(), "a"));
        tvDayOfWeek.setText(CustomDateFormat.format(startDate.getTime(), "EEEE"));
        tvDay.setText(CustomDateFormat.format(startDate.getTime(), "d"));
        tvMonthAndYear.setText(CustomDateFormat.format(startDate.getTime(), "MMMMMM yyyy"));
    }

    private void SetDefaultEnd() {
        tvHourE.setText(CustomDateFormat.format(endDate.getTime(), "h"));
        tvMinuteE.setText(CustomDateFormat.format(endDate.getTime(), "mm"));
        tvAmPmE.setText(CustomDateFormat.format(endDate.getTime(), "a"));
        tvDayOfWeekE.setText(CustomDateFormat.format(endDate.getTime(), "EEEE"));
        tvDayE.setText(CustomDateFormat.format(endDate.getTime(), "d"));
        tvMonthAndYearE.setText(CustomDateFormat.format(endDate.getTime(), "MMMMMM yyyy"));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void startTimer() {
        mTimerHandler.postDelayed(mTimerRunnable, 0);
    }

    private void stopTimer() {
        mTimerHandler.removeCallbacks(mTimerRunnable);
    }

    private void setupTimer() {
        mTimerHandler = new Handler();
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {

                mGeolocation = null;
                mGeolocation = new Geolocation((LocationManager) getSystemService(Context.LOCATION_SERVICE), ActivityAddEvent.this);
                mTimerHandler.postDelayed(this, TIMER_DELAY);
            }
        };
    }

    private void Create() {
        ArrayList<String> thumbnails = mPatio.getThumbnailsPaths();

        if (validate(direction.getText().toString())) {
            showMessage("Please, pick an address for your event");
            direction.requestFocus();
            return;
        }

        if (validate(tittle.getText().toString())) {
            showMessage("Please, add a tittle");
            tittle.requestFocus();
            tittleWrapper.setError("Tittle should not be empty");
            return;
        }

        if (validate(description.getText().toString())) {
            showMessage("Please, add a description for your event");
            description.requestFocus();
            descriptionWrapper.setError("Description should not be empty");
            return;
        }

        if (thumbnails.size() == 0) {
            showMessage("The event must have at least one picture");
            return;
        }

        event = new LocalEvent();
        event.setDescription(description.getText().toString().trim());
        event.setTitle(description.getText().toString().trim());
        event.setFreeEntrance(free_entrance.isChecked());

        event.setStartDate(CustomDateFormat.getCurrentDateTime(startDate.getTime()));
        //LogManager.getInstance().info("startDate", CustomDateFormat.getCurrentDateTime(startDate.getTime()));
        event.setEndDate(CustomDateFormat.getCurrentDateTime(endDate.getTime()));
        //LogManager.getInstance().info("endDate", CustomDateFormat.getCurrentDateTime(endDate.getTime()));

        event.setFormattedAddress(place.getAddress().toString());
        event.setLatitude(String.valueOf(place.getLatLng().latitude));
        event.setLongitude(String.valueOf(place.getLatLng().longitude));
        event.setApplicationUserId(us.getId());


        for (String item : thumbnails) {
            File picfile = new File(item);
            String filename = picfile.getName();
            String pic = Tools.CompressImage(item, ActivityAddEvent.this);
            event.setImageBase64(pic);
            event.setImageFileName(filename);
        }

        ShowProgress("Please wait");
        new EventTask(this).CallService(3, "", event);
    }

    public boolean validate(String chain) {
        return chain.trim().length() == 0;
    }

    @Override
    public void onClick(View v) {
    }

    private void showMessage(String message) {
        Snackbar snack = Snackbar.make(parent_view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.YELLOW);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }

    public void initActionbar() {

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        TextView tittle = (TextView) findViewById(R.id.tvSearchToolBar_title);
        tittle.setText("CREATE NEW EVENT");
        toolbar.setBackgroundColor(getResources().getColor(R.color.global_color_blue_primary_dark));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
        TimePickerDialog tpd = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if (tpd != null) tpd.setOnTimeSetListener(this);
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
        if (mGeolocation != null) mGeolocation.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public void Close() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                HideProgress();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        };
        new Timer().schedule(task, 1000);
    }

    @Override
    public void onTakePictureClick() {
        Intent intent = mPatio.getTakePictureIntent();
        startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
    }

    @Override
    public void onAddPictureClick() {
        Intent intent = mPatio.getAttachPictureIntent();
        startActivityForResult(intent, REQUEST_CODE_ATTACH_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_ATTACH_PICTURE) {
            mPatio.handleAttachPictureResult(data);
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_TAKE_PICTURE) {
            mPatio.handleTakePictureResult(data);
        }
        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            place = PlacePicker.getPlace(this, data);
            name = place.getName();
            address = place.getAddress();
            direction.setText(name + " - " + address);
            renderViewMap(place.getLatLng());
        }
    }

    private void renderViewMap(LatLng latLng) {
        final ImageView imageView = (ImageView) parent_view.findViewById(R.id.fragment_poi_detail_map_image);

        String key = getString(R.string.google_maps_key);
        String url = getStaticMapUrl(key, latLng.latitude, latLng.longitude, MAP_ZOOM);
        Picasso.with(ActivityAddEvent.this)
                .load(url).resize(size, size)
                .placeholder(R.drawable.placeholder_map)
                .error(R.drawable.placeholder_map)
                .transform(new BitmapTransform(BitmapTransform.MAX_WIDTH, BitmapTransform.MAX_HEIGHT)).
                centerInside().into(imageView);
    }

    private String getStaticMapUrl(String key, double lat, double lon, int zoom) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if (item.getItemId() == R.id.action_create) {
            hideKeyboard();
            Create();
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void ShowProgress(String content) {
        if (progress == null) {
            progress = new MaterialDialog.Builder(this)
                    .content(content)
                    .cancelable(false)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .show();
        }
    }

    private void HideProgress() {
        if (progress != null) {
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }
    }

    @Override
    public void onGeolocationRespond(Geolocation geolocation, final Location location) {
        runTaskCallback(new Runnable() {
            public void run() {
                mLocation = location;
            }
        });
    }

    @Override
    public void onGeolocationFail(Geolocation geolocation) {
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;

        if (timestart) {

            startDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            startDate.set(Calendar.MINUTE, minute);
            startDate.set(Calendar.SECOND, second);

            tvHour.setText(hourString);
            tvMinute.setText(minuteString);
            tvAmPm.setText(CustomDateFormat.format(startDate.getTime(), "a"));

        } else if (timeend) {

            endDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            endDate.set(Calendar.MINUTE, minute);
            endDate.set(Calendar.SECOND, second);

            tvHourE.setText(hourString);
            tvMinuteE.setText(minuteString);
            tvAmPmE.setText(CustomDateFormat.format(startDate.getTime(), "a"));
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (datestart) {

            startDate.set(Calendar.YEAR, year);
            startDate.set(Calendar.MONTH, monthOfYear);
            startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            tvDayOfWeek.setText(CustomDateFormat.format(startDate.getTime(), "EEEE"));
            tvDay.setText(String.valueOf(dayOfMonth));
            tvMonthAndYear.setText(DataFormatter.getMonth(monthOfYear) + " " + year);

        } else if (dateend) {

            endDate.set(Calendar.YEAR, year);
            endDate.set(Calendar.MONTH, monthOfYear);
            endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            tvDayOfWeekE.setText(CustomDateFormat.format(endDate.getTime(), "EEEE"));
            tvDayE.setText(String.valueOf(dayOfMonth));
            tvMonthAndYearE.setText(DataFormatter.getMonth(monthOfYear) + " " + year);

        }
    }

    private void getDate(String Tittle) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ActivityAddEvent.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setThemeDark(true);
        dpd.vibrate(false);
        dpd.dismissOnPause(false);
        dpd.showYearPickerFirst(false);
        dpd.setTitle(Tittle);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    private void getTime(String tittle) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                ActivityAddEvent.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(true);
        tpd.vibrate(false);
        tpd.dismissOnPause(false);
        tpd.enableSeconds(false);
        tpd.enableMinutes(true);
        tpd.setTitle(tittle);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onResponseObjectCallBack(Object object) {
        showMessage("Event created succesfully");
        Close();
    }

    @Override
    public void onError(String message, Integer code) {
        HideProgress();
        showMessage(message);
    }

}
