package com.app.localbig.data.dao;
import com.app.localbig.data.AppDatabaseHelper;
import com.app.localbig.helper.log.LogManager;
import com.app.localbig.model.ApplicationUser;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ApplicationUserDAO extends AbstractDAO<ApplicationUser, String> implements
        IOperationDAO<ApplicationUser> {

    private AppDatabaseHelper helper;

    public ApplicationUserDAO(AppDatabaseHelper helper) {
        this.helper = helper;
    }

    public AppDatabaseHelper getHelper() {
        return helper;
    }

    @Override
    public Class<ApplicationUser> getEntityClass() {
        return ApplicationUser.class;
    }

    @Override
    public Dao<ApplicationUser, String> getDao() {
        try {
            return DaoManager.createDao(getHelper().getConnectionSource(), getEntityClass());
        } catch (Exception localException) {
            LogManager.getInstance().error(getClass().getCanonicalName(), localException.getMessage());
        }
        return null;
    }

    public void Create(ApplicationUser l, int operation) throws ApplicationUserException {
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

    public ApplicationUser getApplicationUser() throws ApplicationUserException {
        ApplicationUser ApplicationUser = new ApplicationUser();
        try {
            ApplicationUser = getDao().queryBuilder().where().eq("active", true).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return ApplicationUser;
    }

    public ApplicationUser getApplicationUserById(String userid) throws ApplicationUserException {
        ApplicationUser ApplicationUser = new ApplicationUser();
        try {
            ApplicationUser = getDao().queryBuilder().where().eq("id", userid).queryForFirst();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return ApplicationUser;
    }

    @Override
    public ApplicationUser Get(ApplicationUser object) throws ApplicationUserException {
        ApplicationUser ApplicationUser = null;
        try {
            ApplicationUser = getDao().queryForSameId(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return ApplicationUser;
    }

    public void Delete(ApplicationUser object) throws ApplicationUserException {
        try {
            getDao().delete(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Refresh(ApplicationUser object) throws ApplicationUserException {
        try {
            getDao().refresh(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public void Update(ApplicationUser object) throws ApplicationUserException {
        try {
            getDao().update(object);
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
    }

    public long CountOf() throws ApplicationUserException {
        long count = 0;
        try {
            count = getDao().countOf();
        } catch (SQLException e) {
            LogManager.getInstance().error(getClass().getCanonicalName(), e.getMessage());
        }
        return count;
    }

    public void CreateList(final ArrayList<ApplicationUser> list, final int operation) throws ApplicationUserException {
        try {
            getDao().callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (ApplicationUser ur : list) {
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
    public ArrayList<ApplicationUser> GetList() {
        ArrayList<ApplicationUser> lists = null;
        try {
            lists = (ArrayList<ApplicationUser>) getDao().queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lists;
    }

}
