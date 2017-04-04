package com.app.localbig.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.app.localbig.R;
import com.app.localbig.data.AppDatabaseManager;
import com.app.localbig.data.dao.ApplicationUserDAO;
import com.app.localbig.data.dao.IOperationDAO;
import com.app.localbig.helper.util.CustomTypefaceSpan;
import com.app.localbig.helper.util.FontTypefaceUtils;
import com.app.localbig.helper.util.Tools;
import com.app.localbig.model.ApplicationUser;
import com.app.localbig.view.fragment.MapFragment;
import com.app.localbig.view.fragment.SettingFragment;
import com.app.localbig.view.tagview.TagView;
import com.app.localbig.view.widget.CustomFontTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vistrav.ask.Ask;
import com.vistrav.ask.annotations.AskDenied;
import com.vistrav.ask.annotations.AskGranted;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_OBJCT_USER = "USER";
    private static final int REQUEST_LOGIN = 0;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private NavigationView navigationView;
    private ApplicationUser user;
    private Handler mainHandler;
    private Runnable mRunnable;
    private Fragment fragment;
    public static int currentColor;
    private DrawerLayout drawer;
    private Bundle bundle;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Ask.on(this)
                .forPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA)
                .withRationales("Location permission need for map to work properly",
                        "Camera permission need for take pictures of your business")
                .go();


        initActionbar();
        Tools.systemBarLolipop(this);
        currentColor = ContextCompat.getColor(this, R.color.toolbar_bg);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(EXTRA_OBJCT_USER)) {
                Gson gSon = new Gson();
                user = gSon.fromJson(bundle.getString(EXTRA_OBJCT_USER), new TypeToken<ApplicationUser>() {
                }.getType());
            }
        }

        if (user == null)
            user = new ApplicationUserDAO(AppDatabaseManager.getInstance().getHelper()).getApplicationUser();

        // This is temporary, this should be applied only for the Map initialization

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED)
            initDrawerMenu();

    }

    // Camera granted
    @AskGranted(Manifest.permission.CAMERA)
    public void cameraAccessGranted() {


    }

    // Camera Denied
    @AskDenied(Manifest.permission.CAMERA)
    public void cameraAccessDenied() {

    }

    // We have to wait for the user's response and after that, when the dialog is closed,
    // initialize the map. By the moment the same method will execute in both cases.

    // Important. If the user denied the permission, an error occur.


    // Location granted
    @AskGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void mapAccessGranted() {
        initDrawerMenu();
    }

    // Location denied
    @AskDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void mapAccessDenied() {
        initDrawerMenu();
    }



    private void initDrawerMenu() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                hideKeyboard();
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawer.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        actionBar.setTitle(menuItem.getTitle());

                        fragment = new MapFragment().newInstance(user);
                        displayContentView(fragment);

                        break;

                    case R.id.nav_setting:
                        actionBar.setTitle(menuItem.getTitle());
                        fragment = new SettingFragment();
                        displayContentView(fragment);
                        break;

                    case R.id.nav_business:
                        mainHandler = new Handler(getMainLooper());

                        mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Intent intent;
                                intent = new Intent(MainActivity.this, ActivityBusinessList.class);
                                Bundle b = new Bundle();
                                Gson gSon = new Gson();
                                b.putString(ActivityBusinessList.EXTRA_OBJCT_USER, gSon.toJson(user));
                                intent.putExtras(b);
                                startActivityForResult(intent, 1);
                            }
                        };
                        mainHandler.post(mRunnable);
                        break;

                    case R.id.nav_favorite:
                        mainHandler = new Handler(getMainLooper());

                        mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Intent intent;
                                intent = new Intent(MainActivity.this, ActivityBusinessFavoriteList.class);
                                Bundle b = new Bundle();
                                Gson gSon = new Gson();
                                b.putString(ActivityBusinessList.EXTRA_OBJCT_USER, gSon.toJson(user));
                                intent.putExtras(b);
                                startActivityForResult(intent, 1);
                            }
                        };
                        mainHandler.post(mRunnable);
                        break;

                    case R.id.nav_event:
                        mainHandler = new Handler(getMainLooper());

                        mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Intent intent;
                                intent = new Intent(MainActivity.this, ActivityEventList.class);

                                Bundle b = new Bundle();
                                Gson gSon = new Gson();
                                b.putString(ActivityEventList.EXTRA_OBJCT_USER, gSon.toJson(user));
                                intent.putExtras(b);
                                startActivityForResult(intent, 1);
                            }
                        };
                        mainHandler.post(mRunnable);
                        break;

                    case R.id.nav_coupons:
                        mainHandler = new Handler(getMainLooper());

                        mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Intent intent;
                                intent = new Intent(MainActivity.this, ActivityCouponList.class);

                                Bundle b = new Bundle();
                                Gson gSon = new Gson();
                                b.putString(ActivityCouponList.EXTRA_OBJCT_USER, gSon.toJson(user));
                                intent.putExtras(b);
                                startActivityForResult(intent, 1);
                            }
                        };
                        mainHandler.post(mRunnable);
                        break;

                    case R.id.nav_logout:

                        user.setActive(false);
                        new ApplicationUserDAO(AppDatabaseManager.getInstance().getHelper()).Create(user, IOperationDAO.OPERATION_INSERT_OR_UPDATE);

                        user = null;
                        getIntent().removeExtra(EXTRA_OBJCT_USER);
                        finish();
                        startActivity(getIntent());
                        break;

                    case R.id.nav_login:
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivityForResult(intent, REQUEST_LOGIN);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        break;
                }

                return true;
            }
        });

        navigationView.getMenu().clear();

        if (user != null) {
            navigationView.inflateMenu(R.menu.menu_drawer);
            View nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
            ((TextView) nav_header.findViewById(R.id.name)).setText(user.getFirstName());
            ((TextView) nav_header.findViewById(R.id.name)).setTypeface(FontTypefaceUtils.getKnockout(this));
            ((TextView) nav_header.findViewById(R.id.address)).setText(user.getEmail());
            ((TextView) nav_header.findViewById(R.id.address)).setTypeface(FontTypefaceUtils.getKnockout(this));
            ((TagView) nav_header.findViewById(R.id.owner)).setText(user.isOwner() ? "Owner" : "Customer");

            (nav_header.findViewById(R.id.header_content)).setOnClickListener(new View.OnClickListener() {
                                                                                  @Override
                                                                                  public void onClick(View view) {
                                                                                      Handler mainHandler = new Handler(getMainLooper());
                                                                                      Runnable myRunnable = new Runnable() {
                                                                                          @Override
                                                                                          public void run() {
                                                                                              Intent intent;
                                                                                              intent = new Intent(MainActivity.this, ActivityProfileDetails.class);
                                                                                              Bundle b = new Bundle();
                                                                                              Gson gSon = new Gson();
                                                                                              b.putString(ActivityProfileDetails.EXTRA_OBJCT_USER, gSon.toJson(user));
                                                                                              intent.putExtras(b);
                                                                                              startActivityForResult(intent, 1);
                                                                                              overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                                                                                          }
                                                                                      };
                                                                                      mainHandler.post(myRunnable);
                                                                                  }
                                                                              }
            );
            navigationView.addHeaderView(nav_header);
        } else {
            navigationView.inflateMenu(R.menu.menu_drawer_offline);
        }


        fragment = new MapFragment().newInstance(user);
        displayContentView(fragment);

        Menu m = navigationView.getMenu();

        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            SubMenu subMenu = mi.getSubMenu();
                if (subMenu != null && subMenu.size() > 0) {

                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        applyFontToMenuItem(subMenuItem);

//                    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//                        if (requestCode == REQUEST_LOGIN) {
//                            if (resultCode == RESULT_OK) {
//
//                            }
//                        }
//                    }

                }
            }
        applyFontToMenuItem(mi);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Knockout-29.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public void initActionbar() {

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.str_home));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        CustomFontTextView tvSearchToolBar_title = (CustomFontTextView) findViewById(R.id.tvSearchToolBar_title);
        tvSearchToolBar_title.setText(getString(R.string.app_name));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            doExitApp();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void displayContentView(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            moveTaskToBack(true);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
