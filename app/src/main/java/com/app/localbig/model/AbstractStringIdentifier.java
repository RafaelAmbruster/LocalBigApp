package com.app.localbig.model;

import com.j256.ormlite.field.DatabaseField;

public abstract class AbstractStringIdentifier extends AbstractIdentifier {

    @DatabaseField(id = true)
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

