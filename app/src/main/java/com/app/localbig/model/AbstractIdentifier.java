package com.app.localbig.model;

import com.j256.ormlite.field.DatabaseField;

 abstract class AbstractIdentifier {
    @DatabaseField(index = true)
    public String description;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String paramString) {
        this.description = paramString;
    }

    public String toString() {
        return this.description;
    }
}

