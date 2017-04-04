package com.app.localbig.data.dao;

import com.app.localbig.data.AppDatabaseHelper;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.model.BusinessPicture;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class BusinessPictureDAO extends AbstractDAO<com.app.localbig.model.BusinessPicture, String> implements
        IOperationDAO<com.app.localbig.model.BusinessPicture> {

    private AppDatabaseHelper helper;

    public BusinessPictureDAO(AppDatabaseHelper helper) {
        this.helper = helper;
    }

    public AppDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<com.app.localbig.model.BusinessPicture> getEntityClass() {
        return com.app.localbig.model.BusinessPicture.class;
    }

    @Override
    public Dao<com.app.localbig.model.BusinessPicture, String> getDao() {
        try {
            return DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
        } catch (Exception localException) {
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }

    public void Create(com.app.localbig.model.BusinessPicture l, int operation) throws BusinessPictureException {
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

    public com.app.localbig.model.BusinessPicture getBusinessPicture() throws BusinessPictureException {
        com.app.localbig.model.BusinessPicture BusinessPicture = new com.app.localbig.model.BusinessPicture();
        try {
            BusinessPicture = getDao().queryBuilder().where().eq("active", true).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return BusinessPicture;
    }

    public com.app.localbig.model.BusinessPicture getBusinessPictureByDescription(String description) throws BusinessPictureException {
        com.app.localbig.model.BusinessPicture BusinessPicture = new com.app.localbig.model.BusinessPicture();
        try {
            BusinessPicture = getDao().queryBuilder().where().eq("description", description).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return BusinessPicture;
    }

    @Override
    public com.app.localbig.model.BusinessPicture Get(com.app.localbig.model.BusinessPicture object) throws BusinessPictureException {
        com.app.localbig.model.BusinessPicture BusinessPicture = null;
        try {
            BusinessPicture = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return BusinessPicture;
    }

    public void Delete(com.app.localbig.model.BusinessPicture object) throws BusinessPictureException {
        try {
            getDao().delete(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Refresh(com.app.localbig.model.BusinessPicture object) throws BusinessPictureException {
        try {
            getDao().refresh(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Update(com.app.localbig.model.BusinessPicture object) throws BusinessPictureException {
        try {
            getDao().update(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public long CountOf() throws BusinessPictureException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return count;
    }

    public void CreateList(final ArrayList<com.app.localbig.model.BusinessPicture> list, final int operation) throws BusinessPictureException {
        try {
            getDao().callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (com.app.localbig.model.BusinessPicture ur : list) {
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
    public ArrayList<com.app.localbig.model.BusinessPicture> GetList() {
        ArrayList<com.app.localbig.model.BusinessPicture> lists = null;
        try {
            lists = (ArrayList<com.app.localbig.model.BusinessPicture>) getDao().queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lists;
    }

}
