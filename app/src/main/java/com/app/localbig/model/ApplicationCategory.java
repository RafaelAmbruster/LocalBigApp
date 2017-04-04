
package com.app.localbig.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ApplicationCategory")
public class ApplicationCategory {
    @DatabaseField(id = true)
    private int id;
    @DatabaseField(canBeNull = true)
    private String description;
    @DatabaseField(canBeNull = true)
    private String Image;


    public ApplicationCategory() {
    }

    /**
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * @return
     */
    public String getImage() {
        return Image;
    }

    /**
     * @param image
     */
    public void setImage(String image) {
        Image = image;
    }
}
