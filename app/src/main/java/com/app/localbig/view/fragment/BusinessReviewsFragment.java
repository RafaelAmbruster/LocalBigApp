package com.app.localbig.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.app.localbig.R;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.helper.util.FontTypefaceUtils;
import com.app.localbig.model.ApplicationUser;
import com.app.localbig.model.Business;
import com.app.localbig.model.Review;
import com.app.localbig.rest.ResponseObjectCallBack;
import com.app.localbig.rest.task.business.BusinessReviewTask;
import com.app.localbig.view.adapter.ReviewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Ambruster on 29/06/2016.
 */

public class BusinessReviewsFragment extends BaseFragment implements View.OnClickListener, ResponseObjectCallBack {

    private ReviewAdapter adapter;
    private ArrayList<Review> reviews;
    public static final String EXTRA_OBJCT_BUSINESS = "BUSINESS";
    public static final String EXTRA_OBJCT_USER = "USER";
    public Business business;
    public ApplicationUser user;
    private RecyclerView recycler;
    private ProgressBar progressbar;
    private RelativeLayout ryt_empty, ryt_error;
    private Button error_retry;
    private View parent_view;
    private TextInputLayout emailWrapper;
    private TextInputLayout firstNWrapper;
    private TextInputLayout commentWrapper;
    private EditText email;
    private EditText first_name;
    private EditText comment;
    private RatingBar ratingBar;
    private MaterialDialog progress;
    private MaterialDialog dialog;

    public static BusinessReviewsFragment newInstance(Business business, ApplicationUser user) {
        BusinessReviewsFragment fragment = new BusinessReviewsFragment();
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
        parent_view = inflater.inflate(R.layout.activity_business_reviews, container, false);
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

        FloatingActionButton myFab = (FloatingActionButton) parent_view.findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateReview();
            }
        });

        reviews = new ArrayList<>();
        adapter = new ReviewAdapter(getActivity(), recycler, new ReviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {

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
                        reviews.addAll(business.getReviews());
                        adapter.AddItems(reviews);
                        adapter.setLoaded();

                        if (reviews.size() > 0) {
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

    public void CreateReview() {

        dialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.createreview)
                .customView(R.layout.dialog_create_business_reviews, true)
                .cancelable(false)
                .positiveText(R.string.post)
                .negativeText(android.R.string.cancel)
                .titleColorRes(R.color.colorPrimaryDark)
                .titleGravity(GravityEnum.CENTER)
                .contentColorRes(android.R.color.white)
                .btnSelector(R.drawable.md_btn_selector_custom_accent, DialogAction.POSITIVE)
                .positiveColor(Color.WHITE)
                .theme(Theme.LIGHT)
                .autoDismiss(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        Review review = new Review();
                        review.setBusinessId(business.getId());
                        review.setBusiness(business);
                        review.setComment(comment.getText().toString().trim());
                        review.setFirstName(first_name.getText().toString().trim());
                        review.setEmail(email.getText().toString().trim());
                        review.setStars((int) ratingBar.getRating());
                        review.setLastName("");

                        if (Validate(review)) {
                            Create(review);
                        }else
                            ShowMessage("Please, verify your data");
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();

        emailWrapper = (TextInputLayout) dialog.getCustomView().findViewById(R.id.emailWrapper);
        emailWrapper.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        firstNWrapper = (TextInputLayout) dialog.getCustomView().findViewById(R.id.firstNWrapper);
        firstNWrapper.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        commentWrapper = (TextInputLayout) dialog.getCustomView().findViewById(R.id.commentWrapper);
        commentWrapper.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        email = (EditText) dialog.getCustomView().findViewById(R.id.email);
        email.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        first_name = (EditText) dialog.getCustomView().findViewById(R.id.first_name);
        first_name.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));
        comment = (EditText) dialog.getCustomView().findViewById(R.id.comment);
        comment.setTypeface(FontTypefaceUtils.getRobotoCondensedLight(getActivity()));

        if (user != null) {
            first_name.setText(user.getFirstName().concat(" ").concat(user.getLastName()));
            first_name.setEnabled(false);
            email.setText(user.getEmail());
            email.setEnabled(false);
        }

        ratingBar = (RatingBar) dialog.getCustomView().findViewById(R.id.ratingBar);
        dialog.show();
    }

    private boolean Validate(Review review) {

        Boolean valid = true;

        if (review.getEmail().trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(review.getEmail().trim()).matches()) {
            emailWrapper.setError("  Enter a valid email address");
            valid = false;
        } else
            emailWrapper.setError("");

        if (review.getFirstName().trim().isEmpty()) {
            firstNWrapper.setError("  Your name cannot be empty");
            valid = false;
        } else
            firstNWrapper.setError("");

        return valid;
    }

    private void Create(Review review) {
        ShowProgress("Please wait");
        new BusinessReviewTask(this).CallService(1, review);
    }

    @Override
    public void onResponseObjectCallBack(Object object) {
        dialog.dismiss();
        HideProgress();
        ShowMessage("Review created");
    }

    @Override
    public void onError(String message, Integer code) {
        HideProgress();
        LogManager.getInstance().info("Error", message + " : " + code);
        ShowMessage(message);
    }

    private void ShowMessage(String message) {
        Snackbar snack = Snackbar.make(parent_view, message, Snackbar.LENGTH_LONG).setActionTextColor(Color.YELLOW);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }

    private void ShowProgress(String content) {
        if (progress == null) {
            progress = new MaterialDialog.Builder(getActivity())
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
}