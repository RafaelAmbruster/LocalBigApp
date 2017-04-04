package com.app.localbig.helper.manager;

import android.os.Environment;
import android.util.SparseArray;
import com.app.localbig.LocalBigApplication;
import java.io.File;
import java.io.IOException;

public class ConfigurationManager {

    private static final String FOLDER_NAME = "/" + LocalBigApplication.TAG + "/";
    private SparseArray<String> paths = new SparseArray<>();
    private static ConfigurationManager singleton;

    private ConfigurationManager() {
        this.paths.put(1, Environment.getExternalStorageDirectory()
                .getAbsolutePath() + FOLDER_NAME);
        this.paths.put(2, Environment.getExternalStorageDirectory()
                .getAbsolutePath() + FOLDER_NAME + "photos/");
        this.paths.put(3,
                "/data/data/com.app.localbig/databases/");
        this.paths.put(4,
                "/data/com.app.localbig/databases/");
        this.paths.put(6, Environment.getExternalStorageDirectory()
                .getAbsolutePath() + FOLDER_NAME + "logs/");
        this.paths.put(5, Environment.getExternalStorageDirectory()
                .getAbsolutePath() + FOLDER_NAME + "temp/");

    }

    public synchronized static ConfigurationManager getInstance() {
        if (singleton == null) {
            singleton = new ConfigurationManager();
        }
        return singleton;
    }

    public String getPath(int paramInt) {
        String str = this.paths.get(paramInt, "");
        if (str.length() > 0) {
            File localFile = new File(str);
            if (!localFile.exists())
                localFile.mkdirs();
        }
        try {
            new File(str + LocalBigApplication.TAG).createNewFile();
            return str;
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return str;
    }

}
