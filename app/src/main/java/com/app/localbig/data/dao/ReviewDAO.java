package com.app.localbig.data.dao;

import com.app.localbig.data.AppDatabaseHelper;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.model.Review;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ReviewDAO extends AbstractDAO<Review, String> implements
        IOperationDAO<Review> {

    private AppDatabaseHelper helper;

    public ReviewDAO(AppDatabaseHelper helper) {
        this.helper = helper;
    }

    public AppDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<Review> getEntityClass() {
        return Review.class;
    }

    @Override
    public Dao<Review, String> getDao() {
        try {
            return DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
        } catch (Exception localException) {
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }

    public void Create(Review l, int operation) throws ReviewException {
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

    public Review getReview() throws ReviewException {
        Review Review = new Review();
        try {
            Review = getDao().queryBuilder().where().eq("active", true).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return Review;
    }

    public Review getReviewByDescription(String description) throws ReviewException {
        Review Review = new Review();
        try {
            Review = getDao().queryBuilder().where().eq("description", description).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return Review;
    }

    @Override
    public Review Get(Review object) throws ReviewException {
        Review Review = null;
        try {
            Review = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return Review;
    }

    public void Delete(Review object) throws ReviewException {
        try {
            getDao().delete(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Refresh(Review object) throws ReviewException {
        try {
            getDao().refresh(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Update(Review object) throws ReviewException {
        try {
            getDao().update(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public long CountOf() throws ReviewException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return count;
    }

    public void CreateList(final ArrayList<Review> list, final int operation) throws ReviewException {
        try {
            getDao().callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (Review ur : list) {
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
    public ArrayList<Review> GetList() {
        ArrayList<Review> lists = null;
        try {
            lists = (ArrayList<Review>) getDao().queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lists;
    }

}
