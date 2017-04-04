package com.app.localbig.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.localbig.R;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.helper.util.Constants;
import com.app.localbig.model.ApplicationUser;
import com.app.localbig.model.Business;
import com.app.localbig.view.tagview.TagView;
import com.app.localbig.view.widget.CustomFontTextView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityVideoPreview extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    public static final String EXTRA_OBJCT_BUSINESS = "BUSINESS";
    public static final String EXTRA_OBJCT_USER = "USER";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private Business business;
    private ApplicationUser user;
    private String[] youtubeUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_preview_video);
        Bundle bundle = getIntent().getExtras();

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

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(Constants.YT_DEVELOPER_KEY, this);
        CustomFontTextView tittle = (CustomFontTextView) findViewById(R.id.name);
        CustomFontTextView text = (CustomFontTextView) findViewById(R.id.address);
        tittle.setText(business.getTitle().equals("") ? "BUSINESS" : business.getTitle());
        text.setText(business.getBusinessCategory().getDescription().equals("") ? "BUSINESS" : business.getBusinessCategory().getDescription());
        TagView skip = (TagView) findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityVideoPreview.this, ActivityBusinessDetails.class);
                Bundle b = new Bundle();
                Gson gSon = new Gson();
                b.putString(ActivityBusinessDetails.EXTRA_OBJCT_BUSINESS, gSon.toJson(business));
                b.putString(ActivityBusinessDetails.EXTRA_OBJCT_USER, gSon.toJson(user));
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher;

            youtubeUrls = new String[business.getVideos().size()];
            for (int i = 0; i < business.getVideos().size();
                 i++) {
                matcher = compiledPattern.matcher(business.getVideos().get(i).getUrl());
                if (matcher.find()) {
                    youtubeUrls[i] = matcher.group();
                }
            }

            if (youtubeUrls.length > 0)
                player.loadVideo(youtubeUrls[0]);

            player.setPlayerStyle(PlayerStyle.CHROMELESS);
            player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {

                }

                @Override
                public void onLoaded(String s) {

                }

                @Override
                public void onAdStarted() {

                }

                @Override
                public void onVideoStarted() {
                    LogManager.getInstance().info("Video Started", "true");
                }

                @Override
                public void onVideoEnded() {
                    LogManager.getInstance().info("Video Finish", "true");
                    Intent intent = new Intent(ActivityVideoPreview.this, ActivityBusinessDetails.class);
                    Bundle b = new Bundle();
                    Gson gSon = new Gson();
                    b.putString(ActivityBusinessDetails.EXTRA_OBJCT_BUSINESS, gSon.toJson(business));
                    b.putString(ActivityBusinessDetails.EXTRA_OBJCT_USER, gSon.toJson(user));
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {

                }
            });

            player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                @Override
                public void onPlaying() {

                }

                @Override
                public void onPaused() {

                }

                @Override
                public void onStopped() {

                }

                @Override
                public void onBuffering(boolean b) {
                }

                @Override
                public void onSeekTo(int i) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            getYouTubePlayerProvider().initialize(Constants.YT_DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

}
