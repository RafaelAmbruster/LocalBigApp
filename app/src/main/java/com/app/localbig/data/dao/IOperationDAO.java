package com.app.localbig.data.dao;

import java.util.ArrayList;

public interface IOperationDAO<T> {

    public static final int OPERATION_INSERT = 0;
    public static final int OPERATION_INSERT_OR_UPDATE = 1;
    public static final int OPERATION_INSERT_IF_NOT_EXISTS = 2;

    void Create(final T object, int operation);

    T Get(T object);

    void Delete(T object);

    void Refresh(T object);

    void Update(T object);

    long CountOf();

    void CreateList(final ArrayList<T> list, final int operation);

    ArrayList<T> GetList();

}
