package com.app.localbig.model;

import com.j256.ormlite.field.DatabaseField;

public abstract class AbstractIntegerIdentifier extends AbstractIdentifier {

    @DatabaseField(id = true)
    public int id;

    public int getId() {
        return this.id;
    }

    public void setId(int paramInt) {
        this.id = paramInt;
    }
}

