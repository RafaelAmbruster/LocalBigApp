package com.app.localbig.view.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.app.localbig.model.LocalEvent;
import com.app.localbig.rest.ResponseObjectCallBack;
import com.app.localbig.rest.task.event.EventTask;
import com.app.localbig.view.activity.ActivityAddEvent;
import com.app.localbig.view.activity.ActivityEventsDetails;
import com.app.localbig.view.adapter.EventAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Ambruster on 29/06/2016.
 */

public class ProfileEventFragment extends BaseFragment implements View.OnClickListener, ResponseObjectCallBack {

    private EventAdapter adapter;
    private ArrayList<LocalEvent> events;
    private static final String EXTRA_OBJCT_PROFILE = "USER";
    private ApplicationUser user;
    private RecyclerView recycler;
    private ProgressBar progressbar;
    private RelativeLayout ryt_empty, ryt_error;
    private Button action_retry, error_retry;
    private View parent_view;
    private static View view;
    private FloatingActionButton fab;

    public static ProfileEventFragment newInstance(ApplicationUser us) {
        ProfileEventFragment fragment = new ProfileEventFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(EXTRA_OBJCT_PROFILE, gSon.toJson(us));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        getActivity().invalidateOptionsMenu();
        super.onResume();
    }

    protected void Load() {
        getActivity().invalidateOptionsMenu();
        progressbar = (ProgressBar) parent_view.findViewById(R.id.progressbar);
        ryt_empty = (RelativeLayout) parent_view.findViewById(R.id.layout_empty);
        ryt_error = (RelativeLayout) parent_view.findViewById(R.id.layout_error);
        error_retry = (Button) parent_view.findViewById(R.id.action_error_retry);
        error_retry.setOnClickListener(this);

        fab = (FloatingActionButton) parent_view.findViewById(R.id.fab);

        if (user != null) {
            if (!user.isOwner())
                fab.setVisibility(View.GONE);
        }

        fab.setFocusable(false);
        fab.setFocusableInTouchMode(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityAddEvent.class);
                Bundle b = new Bundle();
                Gson gSon = new Gson();
                b.putString(ActivityAddEvent.EXTRA_OBJCT_USER, gSon.toJson(user));
                intent.putExtras(b);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityOptions options = ActivityOptions.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });

        recycler = (RecyclerView) parent_view.findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        events = new ArrayList<>();
        adapter = new EventAdapter(getActivity(), recycler, new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                int pos = adapter.getPosition(position);
                LocalEvent event = events.get(pos);
                view = v;
                progressbar.setVisibility(View.VISIBLE);
                OpenDetail(event);
            }
        });
        recycler.setAdapter(adapter);
        Execute();
    }

    private void OpenDetail(LocalEvent event) {
        new EventTask((ResponseObjectCallBack) this).CallService(2, event.getId(), null);
    }

    @Override
    public void onResponseObjectCallBack(Object object) {
        progressbar.setVisibility(View.GONE);
        startEventDetail((LocalEvent) object);
    }

    private void startEventDetail(LocalEvent object) {
        Intent intent = new Intent(getActivity(), ActivityEventsDetails.class);
        Bundle b = new Bundle();
        Gson gSon = new Gson();
        b.putString(ActivityEventsDetails.EXTRA_OBJCT_EVENT, gSon.toJson(object));
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
        LogManager.getInstance().error("Error: ", code + " : " + message);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent_view = inflater.inflate(R.layout.activity_account_list, container, false);
        setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.containsKey(EXTRA_OBJCT_PROFILE)) {
                Gson gSon = new Gson();
                user = gSon.fromJson(bundle.getString(EXTRA_OBJCT_PROFILE), new TypeToken<ApplicationUser>() {
                }.getType());
            }
        }

        getActivity().invalidateOptionsMenu();
        Load();
        return parent_view;
    }

    protected void Execute() {
        ryt_error.setVisibility(View.GONE);
        progressbar.setVisibility(View.GONE);
        try {
            if (user.getBusinesses() != null) {
                runTaskCallback(new Runnable() {
                    public void run() {
                        events.addAll(user.getLocalEvents());
                        adapter.AddItems(events);
                        adapter.setLoaded();

                        if (events.size() > 0) {
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
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.action_retry) {
            Execute();
        } else if (view.getId() == R.id.action_error_retry) {
            Execute();
        }
    }
}
