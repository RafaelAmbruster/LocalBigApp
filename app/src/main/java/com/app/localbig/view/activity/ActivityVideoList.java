package com.app.localbig.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.localbig.R;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.helper.util.BitmapTransform;
import com.app.localbig.helper.util.Constants;
import com.app.localbig.helper.util.Tools;
import com.app.localbig.model.Business;
import com.app.localbig.view.widget.youtube.YoutubeOverlayFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityVideoList extends AbstractActivity {
    public static final String EXTRA_OBJCT_BUSINESS = "BUSINESS";
    private Business business;
    public String[] youtubeUrls;
    private YoutubeOverlayFragment ytPlayer;
    private View parent_view;
    private int size = (int) Math.ceil(Math.sqrt(BitmapTransform.MAX_WIDTH * BitmapTransform.MAX_HEIGHT));
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_videos);

        initActionbar();

        Tools.systemBarLolipop(this);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(EXTRA_OBJCT_BUSINESS)) {
                Gson gSon = new Gson();
                business = gSon.fromJson(bundle.getString(EXTRA_OBJCT_BUSINESS), new TypeToken<Business>() {
                }.getType());
            }
        }

        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher;

        youtubeUrls = new String[business.getVideos().size()];
        for (int i = 0; i < business.getVideos().size();
             i++) {
            matcher = compiledPattern.matcher(business.getVideos().get(i).getUrl());
            if (matcher.find()) {
                youtubeUrls[i] = matcher.group();
                LogManager.getInstance().info("ID: ", matcher.group());
            }
        }

        ListView list = (ListView) findViewById(R.id.list_videos);
        list.setAdapter(new YoutubeListAdapter(this, youtubeUrls));
        ytPlayer = YoutubeOverlayFragment.newBuilder(Constants.YT_DEVELOPER_KEY, this).setScrollableViewId(R.id.list_videos).buildAndAdd();
    }

    public void initActionbar() {

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        TextView tittle = (TextView) findViewById(R.id.tvSearchToolBar_title);
        tittle.setText("PROMOTIONAL VIDEOS");
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_bg));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class YoutubeListAdapter extends ArrayAdapter<String> {

        private final LayoutInflater inflater;

        public YoutubeListAdapter(Context context, String[] list) {
            super(context, 0, list);
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_video, parent, false);
                holder = new ViewHolder();
                holder.rowMainImage = convertView.findViewById(R.id.rowMainImage);
                holder.img_thumnail = convertView.findViewById(R.id.img_thumnail);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            final String videoId = getItem(position);
            String img_url="http://img.youtube.com/vi/"+videoId+"/0.jpg";

            Picasso.with(ActivityVideoList.this)
                    .load(img_url).resize(size, size)
                    .transform(new BitmapTransform(BitmapTransform.MAX_WIDTH, BitmapTransform.MAX_HEIGHT)).
                    centerInside().into((ImageView) holder.img_thumnail);

            holder.img_thumnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ytPlayer.onClick(holder.rowMainImage, videoId, position);
                }
            });

            return convertView;
        }

        class ViewHolder {
            View rowMainImage;
            View img_thumnail;
        }
    }

    @Override
    public void onBackPressed() {
        if (ytPlayer.onBackPressed()) {
        } else {
            super.onBackPressed();
        }
    }
}
