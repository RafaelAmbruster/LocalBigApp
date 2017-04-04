package com.app.localbig.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.app.localbig.R;
import com.app.localbig.data.AppDatabaseManager;
import com.app.localbig.data.dao.BusinessCategoryDAO;
import com.app.localbig.helper.geolocation.Geolocation;
import com.app.localbig.helper.geolocation.GeolocationListener;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.helper.util.BitmapTransform;
import com.app.localbig.helper.util.FontTypefaceUtils;
import com.app.localbig.helper.util.Preferences;
import com.app.localbig.helper.util.Tools;
import com.app.localbig.model.AbstractMapModel;
import com.app.localbig.model.ApplicationUser;
import com.app.localbig.model.Business;
import com.app.localbig.model.BusinessCategory;
import com.app.localbig.model.BusinessFilter;
import com.app.localbig.model.LocalEvent;
import com.app.localbig.rest.ResponseLoadCallBack;
import com.app.localbig.rest.ResponseObjectCallBack;
import com.app.localbig.rest.task.business.BusinessTask;
import com.app.localbig.rest.task.event.EventTask;
import com.app.localbig.view.activity.ActivityBusinessDetails;
import com.app.localbig.view.activity.ActivityEventsDetails;
import com.app.localbig.view.activity.ActivityVideoPreview;
import com.app.localbig.view.rangeBar.IRangeBarFormatter;
import com.app.localbig.view.rangeBar.RangeBar;
import com.app.localbig.view.widget.ViewState;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapFragment extends BaseFragment implements ResponseObjectCallBack,
        ResponseLoadCallBack,
        View.OnClickListener,
        GeolocationListener,
        OnMapReadyCallback
{

    public static final String EXTRA_OBJCT_USER = "User";
    private static final int MAP_ZOOM = 14;
    private static final long TIMER_DELAY = 60000l;
    private ViewState mViewState = null;
    private View mRootView;
    private List<AbstractMapModel> models = new ArrayList();
    private ClusterManager<AbstractMapModel> ClusterManager;
    private double mPoiLatitude = 0.0;
    private double mPoiLongitude = 0.0;
    private GoogleMap map;
    private FloatingActionButton fab;
    private ArrayList<String> filtercategories;
    private Button action_retry;
    private static StringBuilder strFilter;
    private Integer range;
    private Handler mTimerHandler;
    private Runnable mTimerRunnable;
    private Geolocation mGeolocation = null;
    private Location mLocation = null;
    private RangeBar distance;
    private AppCompatCheckBox checkbox;
    private ApplicationUser user;
    private String[] youtubeUrls;
    private AbstractMapModel clickedClusterItem;
    private int size = (int) Math.ceil(Math.sqrt(BitmapTransform.MAX_WIDTH * BitmapTransform.MAX_HEIGHT));

    public static MapFragment newInstance(ApplicationUser us) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(EXTRA_OBJCT_USER, gSon.toJson(us));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        setupTimer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_map, container, false);
        getActivity().invalidateOptionsMenu();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey(EXTRA_OBJCT_USER)) {
                Gson gSon = new Gson();
                user = gSon.fromJson(bundle.getString(EXTRA_OBJCT_USER), new TypeToken<ApplicationUser>() {
                }.getType());
            }
        }

        initMap();
        iniFilter();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mViewState == null || mViewState == ViewState.OFFLINE) {
            Run();
        } else if (mViewState == ViewState.CONTENT) {
            if (models != null) {
                renderView();
                showContent();
            }
        } else if (mViewState == ViewState.PROGRESS) {
            showProgress();
        } else if (mViewState == ViewState.EMPTY) {
            showEmpty();
        }
    }

    private void iniFilter() {

        fab = (FloatingActionButton) mRootView.findViewById(R.id.fab);
        fab.setFocusable(false);
        fab.setFocusableInTouchMode(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenFilter();
            }
        });

    }

    public void OpenFilter() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.businesses)
                .customView(R.layout.dialog_filter_business, true)
                .cancelable(false)
                .positiveText(R.string.filter)
                .negativeText(android.R.string.cancel)
                .titleColorRes(R.color.colorPrimaryDark)
                .titleGravity(GravityEnum.CENTER)
                .contentColorRes(android.R.color.white)
                .btnSelector(R.drawable.md_btn_selector_custom_accent, DialogAction.POSITIVE)
                .positiveColor(Color.WHITE)
                .theme(Theme.LIGHT)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        BusinessFilter filter = new BusinessFilter();
                        filter.setCategories(filtercategories);
                        filter.setDistance(range);

                        if (checkbox.isChecked()) {
                            if (mLocation != null) {
                                filter.setLatitude(mLocation.getLatitude());
                                filter.setLongitude(mLocation.getLongitude());
                            } else {
                                Toast.makeText(getActivity(), "Skipping your current location due to an unknown error", Toast.LENGTH_LONG).show();
                            }
                        }

                        ExecuteBusiness(filter);
                    }
                }).build();


        distance = (RangeBar) dialog.getCustomView().findViewById(R.id.distance_filter);
        distance.setRangeBarEnabled(false);
        distance.setTickEnd(50);
        distance.setTickStart(1);
        distance.setTickInterval(1);
        float ini = Float.parseFloat("1");
        float end = 1;
        distance.setRangePinsByValue(ini, end);

        distance.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                range = Integer.parseInt(rightPinValue);
            }

        });

        distance.setFormatter(new IRangeBarFormatter() {
            @Override
            public String format(String s) {
                return s + " M";
            }
        });

        checkbox = (AppCompatCheckBox) dialog.getCustomView().findViewById(R.id.enabled);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    distance.setVisibility(View.VISIBLE);
                } else
                    distance.setVisibility(View.GONE);
            }
        });


        Button openCategories = (Button) dialog.getCustomView().findViewById(R.id.button2);
        openCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMultiChoice();
            }
        });
        dialog.show();

    }

    public void showMultiChoice() {

        ArrayList<BusinessCategory> list = new BusinessCategoryDAO(AppDatabaseManager.getInstance().getHelper()).GetList();

        final String[] categories = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            categories[i] = list.get(i).getDescription();
        }

        new MaterialDialog.Builder(getActivity())
                .title(R.string.business_categories)
                .items(categories)
                .itemsCallbackMultiChoice(new Integer[]{}, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        return true;
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.clearSelectedIndices();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.getSelectedIndices();
                        filtercategories = new ArrayList<>();
                        for (int i = 0; i < dialog.getSelectedIndices().length; i++) {
                            filtercategories.add(new BusinessCategoryDAO(AppDatabaseManager.getInstance().getHelper()).getBusinessCategoryByDescription(categories[dialog.getSelectedIndices()[i]]).getId());
                        }

                        strFilter = new StringBuilder();
                        for (int j = 0; j < filtercategories.size(); j++) {
                            if (j > 0) strFilter.append('\n');
                            strFilter.append(filtercategories.get(j));
                            strFilter.append(": ");
                        }

                        dialog.dismiss();
                    }
                })
                .alwaysCallMultiChoiceCallback()
                .positiveText(R.string.chose)
                .autoDismiss(false)
                .neutralText(R.string.clear)
                .show();
    }

    @Override
    public void onResume() {
        startTimer();
        super.onResume();

    }

    @Override
    public void onPause() {
        stopTimer();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
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
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_layers_normal:
                setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;

            case R.id.menu_layers_satellite:
                setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;

            case R.id.menu_layers_hybrid:
                setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;

            case R.id.menu_layers_terrain:
                setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Run() {
        BusinessFilter filter = new BusinessFilter();
        ExecuteBusiness(filter);
    }

    private void ExecuteBusiness(BusinessFilter filter) {
        showProgress();
        fab.setVisibility(View.VISIBLE);
        models = new ArrayList<>();
        new BusinessTask((ResponseLoadCallBack) this).CallService(1, "", filter, null);
    }

    private void ExecuteEvents() {
        fab.setVisibility(View.GONE);
        showProgress();
        models = new ArrayList<>();
        new EventTask((ResponseLoadCallBack) this).CallService(1, "", null);
    }

    private void showContent() {
        ViewGroup containerContent = (ViewGroup) mRootView.findViewById(R.id.container_content);
        ViewGroup containerProgress = (ViewGroup) mRootView.findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) mRootView.findViewById(R.id.container_offline);
        ViewGroup containerEmpty = (ViewGroup) mRootView.findViewById(R.id.container_empty);
        containerContent.setVisibility(View.VISIBLE);
        containerProgress.setVisibility(View.GONE);
        containerOffline.setVisibility(View.GONE);
        containerEmpty.setVisibility(View.GONE);
        mViewState = ViewState.CONTENT;
    }

    private void showProgress() {
        ViewGroup containerContent = (ViewGroup) mRootView.findViewById(R.id.container_content);
        ViewGroup containerProgress = (ViewGroup) mRootView.findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) mRootView.findViewById(R.id.container_offline);
        ViewGroup containerEmpty = (ViewGroup) mRootView.findViewById(R.id.container_empty);
        containerContent.setVisibility(View.VISIBLE);
        containerProgress.setVisibility(View.VISIBLE);
        containerOffline.setVisibility(View.GONE);
        containerEmpty.setVisibility(View.GONE);
        mViewState = ViewState.PROGRESS;
    }

    private void showEmpty() {
        ViewGroup containerContent = (ViewGroup) mRootView.findViewById(R.id.container_content);
        ViewGroup containerProgress = (ViewGroup) mRootView.findViewById(R.id.container_progress);
        ViewGroup containerOffline = (ViewGroup) mRootView.findViewById(R.id.container_offline);
        ViewGroup containerEmpty = (ViewGroup) mRootView.findViewById(R.id.container_empty);
        containerContent.setVisibility(View.GONE);
        containerProgress.setVisibility(View.GONE);
        containerOffline.setVisibility(View.GONE);
        containerEmpty.setVisibility(View.VISIBLE);
        mViewState = ViewState.EMPTY;
    }

    private void showOffline() {

        try {
            ViewGroup containerContent = (ViewGroup) mRootView.findViewById(R.id.container_content);
            ViewGroup containerProgress = (ViewGroup) mRootView.findViewById(R.id.container_progress);
            ViewGroup containerOffline = (ViewGroup) mRootView.findViewById(R.id.container_offline);
            ViewGroup containerEmpty = (ViewGroup) mRootView.findViewById(R.id.container_empty);
            containerContent.setVisibility(View.GONE);
            containerProgress.setVisibility(View.GONE);
            containerOffline.setVisibility(View.VISIBLE);
            containerEmpty.setVisibility(View.GONE);

            action_retry = (Button) mRootView.findViewById(R.id.action_retry);
            action_retry.setOnClickListener(this);

            mViewState = ViewState.OFFLINE;
        } catch (Exception ex) {
        }
    }

    private void renderView() {

        if (map != null) {
            map.clear();
            ClusterManager.clearItems();
            for (AbstractMapModel items : models) {
                ClusterManager.addItem(items);
            }
            ClusterManager.cluster();
        }
    }

    private void initMap() {
        if (!Tools.isSupportedOpenGlEs2(getActivity())) {
            Toast.makeText(getActivity(), R.string.global_map_fail_toast, Toast.LENGTH_LONG).show();
        }
    }

    public class InfoWindowRefresher implements Callback {
        private Marker markerToRefresh;

        public InfoWindowRefresher(Marker markerToRefresh) {
            this.markerToRefresh = markerToRefresh;
        }

        @Override
        public void onSuccess() {
            markerToRefresh.showInfoWindow();
        }

        @Override
        public void onError() {
        }
    }

    class BusinessInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        BusinessInfoWindowAdapter() {
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.row_business_marker, null);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getInfoContents(Marker marker) {
            final ImageView mImageView_b;
            final TextView tittle_b;
            final RatingBar rating_b;
            final TextView address_b;
            final CardView cv_parent_b;
            final int mDimensionHe, mDimensionWe;

            mDimensionHe = (int) getResources().getDimension(R.dimen.marker_large);
            mDimensionWe = (int) getResources().getDimension(R.dimen.marker_large_we);

            cv_parent_b = (CardView) myContentsView.findViewById(R.id.cv_parent);
            mImageView_b = (ImageView) myContentsView.findViewById(R.id.image);
            tittle_b = (TextView) myContentsView.findViewById(R.id.amu_text);
            tittle_b.setTypeface(FontTypefaceUtils.getKnockout(getActivity()));
            rating_b = (RatingBar) myContentsView.findViewById(R.id.user_review_list_rating_bar);
            address_b = (TextView) myContentsView.findViewById(R.id.amu_text_address);
            address_b.setTypeface(FontTypefaceUtils.getKnockout(getActivity()));

            cv_parent_b.setLayoutParams(new CardView.LayoutParams(
                    mDimensionWe, mDimensionHe));


            if (clickedClusterItem != null) {
                if (clickedClusterItem instanceof Business) {

                    final Business bus = (Business) clickedClusterItem;

                    tittle_b.setText(bus.getTitle());
                    DecimalFormat decimalFormat = new DecimalFormat("#");
                    rating_b.setRating(Float.parseFloat(decimalFormat.format(bus.getReviewAverageStars())));
                    address_b.setText(bus.getBusinessCategory().getName());
                    cv_parent_b.setLayoutParams(new CardView.LayoutParams(mDimensionWe, mDimensionHe));

                    try {
                        if (bus.getBusinessPictures() != null) {
                            if (bus.getBusinessPictures().size() > 0) {
                                String url = bus.getBusinessPictures().get(0).getImagePath();

                                if (mImageView_b.getDrawable() == null) {
                                    Picasso.with(getActivity()).load(url)
                                            .resize(size, size).
                                            transform(new BitmapTransform(BitmapTransform.MAX_WIDTH, BitmapTransform.MAX_HEIGHT))
                                            .centerInside().into(mImageView_b, new InfoWindowRefresher(marker));
                                } else {
                                    Picasso.with(getActivity()).load(url)
                                            .resize(size, size).
                                            transform(new BitmapTransform(BitmapTransform.MAX_WIDTH, BitmapTransform.MAX_HEIGHT))
                                            .centerInside().into(mImageView_b);
                                }
                            } else {
                                mImageView_b.setImageDrawable(getActivity().getDrawable(R.drawable.placeholder));
                            }
                        } else {
                            if (bus.getBusinessCategory().getId().equals("a8b2b662700a4de0be314a7aa6531059")) {
                                mImageView_b.setImageResource(R.drawable.ic_auto);
                            } else if (bus.getBusinessCategory().getId().equals("901591a0eb164e74a63f3f0d3524b41d")) {
                                mImageView_b.setImageResource(R.drawable.ic_business);
                            } else if (bus.getBusinessCategory().getId().equals("78ccddc0b67443dfa3846844eb6f9023")) {
                                mImageView_b.setImageResource(R.drawable.ic_computer);
                            } else if (bus.getBusinessCategory().getId().equals("ed67db932cd64d3fbc0fd0eb8abbc411")) {
                                mImageView_b.setImageResource(R.drawable.ic_construction);
                            } else if (bus.getBusinessCategory().getId().equals("66a9de88152b41ea9105494dcbbc04bf")) {
                                mImageView_b.setImageResource(R.drawable.ic_education);
                            } else if (bus.getBusinessCategory().getId().equals("d8b6a943a3f04cafb5c0e4ffae8bc516")) {
                                mImageView_b.setImageResource(R.drawable.ic_entertainment);
                            } else if (bus.getBusinessCategory().getId().equals("824082f89e454998870afa9f2cf168f3")) {
                                mImageView_b.setImageResource(R.drawable.ic_food);
                            } else if (bus.getBusinessCategory().getId().equals("6290cf040faf4d0d9b5250864ef10c73")) {
                                mImageView_b.setImageResource(R.drawable.ic_health);
                            } else if (bus.getBusinessCategory().getId().equals("9e44d4ddbe354819b4580007b2114425")) {
                                mImageView_b.setImageResource(R.drawable.ic_home);
                            } else if (bus.getBusinessCategory().getId().equals("346abece27cf4081bd363beb39c79d58")) {
                                mImageView_b.setImageResource(R.drawable.ic_legal);
                            } else if (bus.getBusinessCategory().getId().equals("fc59a27c5ab24212a5e877cf3520f2cf")) {
                                mImageView_b.setImageResource(R.drawable.ic_manufacturing);
                            } else if (bus.getBusinessCategory().getId().equals("bcfec3e2eaef463abcbf7564315abd0b")) {
                                mImageView_b.setImageResource(R.drawable.ic_merchants);
                            } else if (bus.getBusinessCategory().getId().equals("be4ec18b93dc4aec959d8580b8c9947f")) {
                                mImageView_b.setImageResource(R.drawable.ic_miscellaneous);
                            } else if (bus.getBusinessCategory().getId().equals("94b3c7151783465ea3ec572913ada7ff")) {
                                mImageView_b.setImageResource(R.drawable.ic_personal_care);
                            } else if (bus.getBusinessCategory().getId().equals("b38e0253edca4153b59ec752088f5f2b")) {
                                mImageView_b.setImageResource(R.drawable.ic_realstate);
                            } else if (bus.getBusinessCategory().getId().equals("f43ee0abae1a46bbbd8dd0d23d229a0f")) {
                                mImageView_b.setImageResource(R.drawable.ic_travel);
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
            }

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

    }

    class EventInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        EventInfoWindowAdapter() {
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.row_event_marker, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            final ImageView mImageView_e;
            final TextView tittle_e;
            final TextView address_e;
            final CardView cv_parent_e;
            final int mDimensionHe, mDimensionWe;

            mDimensionHe = (int) getResources().getDimension(R.dimen.marker_large);
            mDimensionWe = (int) getResources().getDimension(R.dimen.marker_large_we);

            cv_parent_e = (CardView) myContentsView.findViewById(R.id.cv_parent);
            mImageView_e = (ImageView) myContentsView.findViewById(R.id.image);
            tittle_e = (TextView) myContentsView.findViewById(R.id.amu_text);
            tittle_e.setTypeface(FontTypefaceUtils.getKnockout(getActivity()));
            address_e = (TextView) myContentsView.findViewById(R.id.amu_text_address);
            address_e.setTypeface(FontTypefaceUtils.getKnockout(getActivity()));

            cv_parent_e.setLayoutParams(new CardView.LayoutParams(
                    mDimensionWe, mDimensionHe));

            if (clickedClusterItem != null) {
                if (clickedClusterItem instanceof LocalEvent) {
                    final LocalEvent eve = (LocalEvent) clickedClusterItem;
                    tittle_e.setText(eve.getTitle());
                    address_e.setText(eve.getFormattedAddress());
                    mImageView_e.setImageResource(R.drawable.ic_event);
                }
            }

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

    }

    private void SetupClusterManager() {

        final IconGenerator BusinessIconGenerator = new IconGenerator(getActivity());
        final IconGenerator EventsIconGenerator = new IconGenerator(getActivity());
        final ImageView mImageView_b, mImageView_e;

        View BusinessProfile = getActivity().getLayoutInflater().inflate(R.layout.row_business_marker_v_one, null);
        BusinessIconGenerator.setContentView(BusinessProfile);
        View EventProfile = getActivity().getLayoutInflater().inflate(R.layout.row_event_marker_v_one, null);
        EventsIconGenerator.setContentView(EventProfile);

        mImageView_b = (ImageView) BusinessProfile.findViewById(R.id.image);
        mImageView_e = (ImageView) EventProfile.findViewById(R.id.image);

        if (map != null) {

            ClusterManager = new ClusterManager<>(getActivity(), map);
            map.setInfoWindowAdapter(ClusterManager.getMarkerManager());

            ClusterManager.setRenderer(new DefaultClusterRenderer<AbstractMapModel>(getActivity(), map, ClusterManager) {

                @Override
                protected boolean shouldRenderAsCluster(Cluster cluster) {
                    return cluster.getSize() > 5;
                }

                @Override
                protected void onBeforeClusterItemRendered(AbstractMapModel model, MarkerOptions markerOptions) {

                    if (model instanceof Business) {
                        final Business business = (Business) model;

                        ClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new BusinessInfoWindowAdapter());

                        try {
                            if (business.getBusinessCategory().getId().equals("a8b2b662700a4de0be314a7aa6531059")) {
                                mImageView_b.setImageResource(R.drawable.ic_auto);
                            } else if (business.getBusinessCategory().getId().equals("901591a0eb164e74a63f3f0d3524b41d")) {
                                mImageView_b.setImageResource(R.drawable.ic_business);
                            } else if (business.getBusinessCategory().getId().equals("78ccddc0b67443dfa3846844eb6f9023")) {
                                mImageView_b.setImageResource(R.drawable.ic_computer);
                            } else if (business.getBusinessCategory().getId().equals("ed67db932cd64d3fbc0fd0eb8abbc411")) {
                                mImageView_b.setImageResource(R.drawable.ic_construction);
                            } else if (business.getBusinessCategory().getId().equals("66a9de88152b41ea9105494dcbbc04bf")) {
                                mImageView_b.setImageResource(R.drawable.ic_education);
                            } else if (business.getBusinessCategory().getId().equals("d8b6a943a3f04cafb5c0e4ffae8bc516")) {
                                mImageView_b.setImageResource(R.drawable.ic_entertainment);
                            } else if (business.getBusinessCategory().getId().equals("824082f89e454998870afa9f2cf168f3")) {
                                mImageView_b.setImageResource(R.drawable.ic_food);
                            } else if (business.getBusinessCategory().getId().equals("6290cf040faf4d0d9b5250864ef10c73")) {
                                mImageView_b.setImageResource(R.drawable.ic_health);
                            } else if (business.getBusinessCategory().getId().equals("9e44d4ddbe354819b4580007b2114425")) {
                                mImageView_b.setImageResource(R.drawable.ic_home);
                            } else if (business.getBusinessCategory().getId().equals("346abece27cf4081bd363beb39c79d58")) {
                                mImageView_b.setImageResource(R.drawable.ic_legal);
                            } else if (business.getBusinessCategory().getId().equals("fc59a27c5ab24212a5e877cf3520f2cf")) {
                                mImageView_b.setImageResource(R.drawable.ic_manufacturing);
                            } else if (business.getBusinessCategory().getId().equals("bcfec3e2eaef463abcbf7564315abd0b")) {
                                mImageView_b.setImageResource(R.drawable.ic_merchants);
                            } else if (business.getBusinessCategory().getId().equals("be4ec18b93dc4aec959d8580b8c9947f")) {
                                mImageView_b.setImageResource(R.drawable.ic_miscellaneous);
                            } else if (business.getBusinessCategory().getId().equals("94b3c7151783465ea3ec572913ada7ff")) {
                                mImageView_b.setImageResource(R.drawable.ic_personal_care);
                            } else if (business.getBusinessCategory().getId().equals("b38e0253edca4153b59ec752088f5f2b")) {
                                mImageView_b.setImageResource(R.drawable.ic_realstate);
                            } else if (business.getBusinessCategory().getId().equals("f43ee0abae1a46bbbd8dd0d23d229a0f")) {
                                mImageView_b.setImageResource(R.drawable.ic_travel);
                            }
                        } catch (Exception ex) {
                        }

                        Bitmap icon = BusinessIconGenerator.makeIcon();
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                if (clickedClusterItem != null)
                                    if (clickedClusterItem instanceof Business)
                                        startBusinessDetailActivity((Business) clickedClusterItem);
                            }
                        });

                    } else if (model instanceof LocalEvent) {

                        ClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new EventInfoWindowAdapter());
                        mImageView_e.setImageResource(R.drawable.ic_event);
                        Bitmap icon = EventsIconGenerator.makeIcon();
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
                        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                if (clickedClusterItem != null)
                                    if (clickedClusterItem instanceof LocalEvent)
                                        startEventDetailActivity((LocalEvent) clickedClusterItem);
                            }
                        });
                    }

                    super.onBeforeClusterItemRendered(model, markerOptions);
                }
            });

            map.setOnCameraChangeListener(ClusterManager);
            map.setOnInfoWindowClickListener(ClusterManager);
            map.setOnMarkerClickListener(ClusterManager);

            ClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<AbstractMapModel>() {
                @Override
                public boolean onClusterItemClick(AbstractMapModel item) {
                    clickedClusterItem = item;
                    return false;
                }
            });
        }
    }

    private Location getLastKnownLocation(LocationManager locationManager) {

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        long timeNet = 0l;
        long timeGps = 0l;

        if (locationNet != null) {
            timeNet = locationNet.getTime();
        }

        if (locationGps != null) {
            timeGps = locationGps.getTime();
        }

        if (timeNet > timeGps) return locationNet;
        else return locationGps;
    }

    private void setMapType(int type) {
        if (map != null) {
            map.setMapType(type);

            Preferences preferences = new Preferences(getActivity());
            preferences.setMapType(type);
        }
    }

    private void startBusinessDetailActivity(Business business) {
        showProgress();
        new BusinessTask((ResponseObjectCallBack) this).CallService(2, business.getId(), null, null);
    }

    private void startEventDetailActivity(LocalEvent event) {
        showProgress();
        new EventTask((ResponseObjectCallBack) this).CallService(2, event.getId(), null);
    }

    @Override
    public void onResponseLoadCallBack(final ArrayList list) {

        runTaskCallback(new Runnable() {
            public void run() {
                if (mRootView == null) return;

                Iterator<AbstractMapModel> iterator = list.iterator();
                while (iterator.hasNext()) {
                    AbstractMapModel bus = iterator.next();
                    models.add(bus);
                }
                if (models != null && models.size() > 0) {
                    renderView();
                    showContent();
                } else showEmpty();
            }
        });
    }

    @Override
    public void onResponseObjectCallBack(Object object) {
        showContent();
        Intent intent;
        if (object instanceof Business) {
            Business tmp = (Business) object;
            if (tmp.getVideos() != null) {
                if (tmp.getVideos().size() > 0) {

                    String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
                    Pattern compiledPattern = Pattern.compile(pattern);
                    Matcher matcher;

                    youtubeUrls = new String[tmp.getVideos().size()];
                    for (int i = 0; i < tmp.getVideos().size();
                         i++) {
                        matcher = compiledPattern.matcher(tmp.getVideos().get(i).getUrl());
                        if (matcher.find()) {
                            youtubeUrls[i] = matcher.group();
                        }
                    }

                    if (youtubeUrls.length > 0)
                        intent = new Intent(getActivity(), ActivityVideoPreview.class);
                    else
                        intent = new Intent(getActivity(), ActivityBusinessDetails.class);

                } else {
                    intent = new Intent(getActivity(), ActivityBusinessDetails.class);
                }
            } else {
                intent = new Intent(getActivity(), ActivityBusinessDetails.class);
            }

            Bundle b = new Bundle();
            Gson gSon = new Gson();
            b.putString(ActivityBusinessDetails.EXTRA_OBJCT_BUSINESS, gSon.toJson(object));

            LogManager.getInstance().info("User", user == null ? "IS NULL" : "IS GOOD");

            b.putString(ActivityBusinessDetails.EXTRA_OBJCT_USER, gSon.toJson(user));
            intent.putExtras(b);
            startActivity(intent);
        } else if (object instanceof LocalEvent) {
            intent = new Intent(getActivity(), ActivityEventsDetails.class);
            Bundle b = new Bundle();
            Gson gSon = new Gson();

            LogManager.getInstance().info("User", user == null ? "IS NULL" : "IS GOOD");

            b.putString(ActivityEventsDetails.EXTRA_OBJCT_EVENT, gSon.toJson(object));
            b.putString(ActivityBusinessDetails.EXTRA_OBJCT_USER, gSon.toJson(user));
            intent.putExtras(b);
            startActivity(intent);
        }
    }

    @Override
    public void onError(String message, Integer code) {
        LogManager.getInstance().info(message, code + "");
        showOffline();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.action_retry) {
            BusinessFilter filter = new BusinessFilter();
            ExecuteBusiness(filter);
        } else if (view.getId() == R.id.action_error_retry) {

        }
    }

    private void startTimer() {
        mTimerHandler.postDelayed(mTimerRunnable, 0);
    }

    private void setupTimer() {
        mTimerHandler = new Handler();
        mTimerRunnable = new Runnable() {
            @Override
            public void run() {
                initGeolocation();
                mTimerHandler.postDelayed(this, TIMER_DELAY);
            }
        };
    }

    private void initGeolocation() {
        mGeolocation = null;
        mGeolocation = new Geolocation((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE), this);
    }

    private void stopTimer() {
        mTimerHandler.removeCallbacks(mTimerRunnable);
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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setUpMap();
    }

    public void setUpMap() {

        if (map != null) {
            Preferences preferences = new Preferences(getActivity());
            map.setMapType(preferences.getMapType());
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);
            UiSettings settings = map.getUiSettings();
            settings.setAllGesturesEnabled(true);
            settings.setMyLocationButtonEnabled(true);
            settings.setZoomControlsEnabled(false);

            LatLng latLng = null;
            if (mPoiLatitude == 0.0 && mPoiLongitude == 0.0) {
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Location location = getLastKnownLocation(locationManager);
                if (location != null)
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                latLng = new LatLng(mPoiLatitude, mPoiLongitude);
            }

            if (latLng != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(MAP_ZOOM)
                        .bearing(0)
                        .tilt(0)
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }

        SetupClusterManager();
    }

}

