package com.app.localbig.data.dao;

import com.app.localbig.data.AppDatabaseHelper;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.model.ApplicationCategory;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ApplicationCategoryDAO extends AbstractDAO<ApplicationCategory, String> implements
        IOperationDAO<ApplicationCategory> {

    private AppDatabaseHelper helper;

    public ApplicationCategoryDAO(AppDatabaseHelper helper) {
        this.helper = helper;
    }

    public AppDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<ApplicationCategory> getEntityClass() {
        return ApplicationCategory.class;
    }

    @Override
    public Dao<ApplicationCategory, String> getDao() {
        try {
            return DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
        } catch (Exception localException) {
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }

    public void Create(ApplicationCategory l, int operation) throws ApplicationCategoryException {
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

    public ApplicationCategory getApplicationCategory() throws ApplicationCategoryException {
        ApplicationCategory ApplicationCategory = new ApplicationCategory();
        try {
            ApplicationCategory = getDao().queryBuilder().where().eq("active", true).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return ApplicationCategory;
    }

    public ApplicationCategory getApplicationCategoryById(String userid) throws ApplicationCategoryException {
        ApplicationCategory ApplicationCategory = new ApplicationCategory();
        try {
            ApplicationCategory = getDao().queryBuilder().where().eq("id", userid).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return ApplicationCategory;
    }

    @Override
    public ApplicationCategory Get(ApplicationCategory object) throws ApplicationCategoryException {
        ApplicationCategory ApplicationCategory = null;
        try {
            ApplicationCategory = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return ApplicationCategory;
    }

    public void Delete(ApplicationCategory object) throws ApplicationCategoryException {
        try {
            getDao().delete(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Refresh(ApplicationCategory object) throws ApplicationCategoryException {
        try {
            getDao().refresh(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Update(ApplicationCategory object) throws ApplicationCategoryException {
        try {
            getDao().update(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public long CountOf() throws ApplicationCategoryException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return count;
    }

    public void CreateList(final ArrayList<ApplicationCategory> list, final int operation) throws ApplicationCategoryException {
        try {
            getDao().callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (ApplicationCategory ur : list) {
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
    public ArrayList<ApplicationCategory> GetList() {
        ArrayList<ApplicationCategory> lists = null;
        try {
            lists = (ArrayList<ApplicationCategory>) getDao().queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lists;
    }

}
