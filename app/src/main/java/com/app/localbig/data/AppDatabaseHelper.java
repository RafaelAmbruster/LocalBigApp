package com.app.localbig.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.helper.manager.ConfigurationManager;
import com.app.localbig.model.Address;
import com.app.localbig.model.ApplicationCategory;
import com.app.localbig.model.ApplicationUser;
import com.app.localbig.model.Business;
import com.app.localbig.model.BusinessCategory;
import com.app.localbig.model.BusinessPicture;
import com.app.localbig.model.Coupon;
import com.app.localbig.model.LocalEvent;
import com.app.localbig.model.Review;
import com.app.localbig.model.Video;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class AppDatabaseHelper extends OrmLiteSqliteOpenHelper {

    public static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 3;

    public AppDatabaseHelper(Context context) {
        super(context,  ConfigurationManager.getInstance().getPath(3) + DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, ApplicationUser.class);
            TableUtils.createTable(connectionSource, ApplicationCategory.class);
            TableUtils.createTable(connectionSource, BusinessCategory.class);
            TableUtils.createTable(connectionSource, Business.class);
            TableUtils.createTable(connectionSource, Address.class);
            TableUtils.createTable(connectionSource, BusinessPicture.class);
            TableUtils.createTable(connectionSource, Coupon.class);
            TableUtils.createTable(connectionSource, LocalEvent.class);
            TableUtils.createTable(connectionSource, Review.class);
            TableUtils.createTable(connectionSource, Video.class);


        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {

            //Removing
            TableUtils.dropTable(connectionSource, ApplicationUser.class, true);
            TableUtils.dropTable(connectionSource, ApplicationCategory.class, true);
            TableUtils.dropTable(connectionSource, BusinessCategory.class, true);
            TableUtils.dropTable(connectionSource, ApplicationCategory.class, true);
            TableUtils.dropTable(connectionSource, BusinessCategory.class, true);
            TableUtils.dropTable(connectionSource, Business.class, true);
            TableUtils.dropTable(connectionSource, Address.class, true);
            TableUtils.dropTable(connectionSource, BusinessPicture.class, true);
            TableUtils.dropTable(connectionSource, Coupon.class, true);
            TableUtils.dropTable(connectionSource, LocalEvent.class, true);
            TableUtils.dropTable(connectionSource, Review.class, true);
            TableUtils.dropTable(connectionSource, Video.class, true);

            //Creating
            TableUtils.createTable(connectionSource, ApplicationUser.class);
            TableUtils.createTable(connectionSource, ApplicationCategory.class);
            TableUtils.createTable(connectionSource, BusinessCategory.class);
            TableUtils.createTable(connectionSource, ApplicationUser.class);
            TableUtils.createTable(connectionSource, ApplicationCategory.class);
            TableUtils.createTable(connectionSource, BusinessCategory.class);
            TableUtils.createTable(connectionSource, Business.class);
            TableUtils.createTable(connectionSource, Address.class);
            TableUtils.createTable(connectionSource, BusinessPicture.class);
            TableUtils.createTable(connectionSource, Coupon.class);
            TableUtils.createTable(connectionSource, LocalEvent.class);
            TableUtils.createTable(connectionSource, Review.class);
            TableUtils.createTable(connectionSource, Video.class);

        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    @Override
    public void close() {
        super.close();
    }
}
