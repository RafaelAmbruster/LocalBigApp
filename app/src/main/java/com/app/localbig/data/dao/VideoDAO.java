package com.app.localbig.data.dao;

import com.app.localbig.data.AppDatabaseHelper;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.model.Video;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class VideoDAO extends AbstractDAO<Video, String> implements
        IOperationDAO<Video> {

    private AppDatabaseHelper helper;

    public VideoDAO(AppDatabaseHelper helper) {
        this.helper = helper;
    }

    public AppDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<Video> getEntityClass() {
        return Video.class;
    }

    @Override
    public Dao<Video, String> getDao() {
        try {
            return DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
        } catch (Exception localException) {
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }

    public void Create(Video l, int operation) throws VideoException {
        try {
            switch (operation) {
                case IOperationDAO.OPERATION_INSERT:
                    getDao().create(l);
                    break;
                case IOperationDAO.OPERATION_INSERT_OR_UPDATE:
                    getDao().createOrUpdate(l);
                    break;
                case IOperationDAO.OPERATION_INSERT_IF_NOT_EXISTS:
                    getDao().createIfNotExists(l);
                    break;
            }
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public Video getVideo() throws VideoException {
        Video Video = new Video();
        try {
            Video = getDao().queryBuilder().where().eq("active", true).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return Video;
    }

    public Video getVideoByDescription(String description) throws VideoException {
        Video Video = new Video();
        try {
            Video = getDao().queryBuilder().where().eq("description", description).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return Video;
    }

    @Override
    public Video Get(Video object) throws VideoException {
        Video Video = null;
        try {
            Video = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return Video;
    }

    public void Delete(Video object) throws VideoException {
        try {
            getDao().delete(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Refresh(Video object) throws VideoException {
        try {
            getDao().refresh(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Update(Video object) throws VideoException {
        try {
            getDao().update(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public long CountOf() throws VideoException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return count;
    }

    public void CreateList(final ArrayList<Video> list, final int operation) throws VideoException {
        try {
            getDao().callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (Video ur : list) {
                        Create(ur, operation);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            LogManager.getInstance().error("Inserting List ", e.getMessage());
        }
    }

    @Override
    public ArrayList<Video> GetList() {
        ArrayList<Video> lists = null;
        try {
            lists = (ArrayList<Video>) getDao().queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lists;
    }

}
