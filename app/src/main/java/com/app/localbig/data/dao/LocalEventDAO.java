package com.app.localbig.data.dao;

import com.app.localbig.data.AppDatabaseHelper;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.model.LocalEvent;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class LocalEventDAO extends AbstractDAO<LocalEvent, String> implements
        IOperationDAO<LocalEvent> {

    private AppDatabaseHelper helper;

    public LocalEventDAO(AppDatabaseHelper helper) {
        this.helper = helper;
    }

    public AppDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<LocalEvent> getEntityClass() {
        return LocalEvent.class;
    }

    @Override
    public Dao<LocalEvent, String> getDao() {
        try {
            return DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
        } catch (Exception localException) {
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }

    public void Create(LocalEvent l, int operation) throws LocalEventException {
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

    public LocalEvent getLocalEvent() throws LocalEventException {
        LocalEvent LocalEvent = new LocalEvent();
        try {
            LocalEvent = getDao().queryBuilder().where().eq("active", true).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return LocalEvent;
    }

    public LocalEvent getLocalEventByDescription(String description) throws LocalEventException {
        LocalEvent LocalEvent = new LocalEvent();
        try {
            LocalEvent = getDao().queryBuilder().where().eq("description", description).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return LocalEvent;
    }

    @Override
    public LocalEvent Get(LocalEvent object) throws LocalEventException {
        LocalEvent LocalEvent = null;
        try {
            LocalEvent = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return LocalEvent;
    }

    public void Delete(LocalEvent object) throws LocalEventException {
        try {
            getDao().delete(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Refresh(LocalEvent object) throws LocalEventException {
        try {
            getDao().refresh(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Update(LocalEvent object) throws LocalEventException {
        try {
            getDao().update(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public long CountOf() throws LocalEventException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return count;
    }

    public void CreateList(final ArrayList<LocalEvent> list, final int operation) throws LocalEventException {
        try {
            getDao().callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (LocalEvent ur : list) {
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
    public ArrayList<LocalEvent> GetList() {
        ArrayList<LocalEvent> lists = null;
        try {
            lists = (ArrayList<LocalEvent>) getDao().queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lists;
    }

}
