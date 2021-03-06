package com.app.localbig.data.dao;

import com.app.localbig.data.AppDatabaseHelper;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.model.BusinessCategory;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class BusinessCategoryDAO extends AbstractDAO<BusinessCategory, String> implements
        IOperationDAO<BusinessCategory> {

    private AppDatabaseHelper helper;

    public BusinessCategoryDAO(AppDatabaseHelper helper) {
        this.helper = helper;
    }

    public AppDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<BusinessCategory> getEntityClass() {
        return BusinessCategory.class;
    }

    @Override
    public Dao<BusinessCategory, String> getDao() {
        try {
            return DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
        } catch (Exception localException) {
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }

    public void Create(BusinessCategory l, int operation) throws BusinessCategoryException {
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

    public BusinessCategory getBusinessCategory() throws BusinessCategoryException {
        BusinessCategory BusinessCategory = new BusinessCategory();
        try {
            BusinessCategory = getDao().queryBuilder().where().eq("active", true).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return BusinessCategory;
    }

    public BusinessCategory getBusinessCategoryByDescription(String description) throws BusinessCategoryException {
        BusinessCategory BusinessCategory = new BusinessCategory();
        try {
            BusinessCategory = getDao().queryBuilder().where().eq("description", description).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return BusinessCategory;
    }

    @Override
    public BusinessCategory Get(BusinessCategory object) throws BusinessCategoryException {
        BusinessCategory BusinessCategory = null;
        try {
            BusinessCategory = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return BusinessCategory;
    }

    public void Delete(BusinessCategory object) throws BusinessCategoryException {
        try {
            getDao().delete(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Refresh(BusinessCategory object) throws BusinessCategoryException {
        try {
            getDao().refresh(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Update(BusinessCategory object) throws BusinessCategoryException {
        try {
            getDao().update(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public long CountOf() throws BusinessCategoryException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return count;
    }

    public void CreateList(final ArrayList<BusinessCategory> list, final int operation) throws BusinessCategoryException {
        try {
            getDao().callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (BusinessCategory ur : list) {
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
    public ArrayList<BusinessCategory> GetList() {
        ArrayList<BusinessCategory> lists = null;
        try {
            lists = (ArrayList<BusinessCategory>) getDao().queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lists;
    }

}
