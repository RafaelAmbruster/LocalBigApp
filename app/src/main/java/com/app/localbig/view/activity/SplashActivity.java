package com.app.localbig.view.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.app.localbig.R;
import com.app.localbig.data.AppDatabaseManager;
import com.app.localbig.data.dao.BusinessCategoryDAO;
import com.app.localbig.data.dao.IOperationDAO;
import com.app.localbig.rest.ResponseLoadCallBack;
import com.app.localbig.rest.task.business.BusinessCategoryTask;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity implements ResponseLoadCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Thread myThread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                   /* TaskStackBuilder.create(SplashActivity.this)
                            .addNextIntentWithParentStack(new Intent(SplashActivity.this, MainActivity.class))
                            .addNextIntent(new Intent(SplashActivity.this, IntroActivity.class))
                            .startActivities();
                 */

                    Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }
        };

        myThread.start();
        Seed();
    }

    private void Seed() {
        new BusinessCategoryTask(this).CallService(1, "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponseLoadCallBack(ArrayList list) {
        new BusinessCategoryDAO(AppDatabaseManager.getInstance().getHelper()).CreateList(list, IOperationDAO.OPERATION_INSERT_IF_NOT_EXISTS);
    }

    @Override
    public void onError(String message, Integer code) {

    }

}
