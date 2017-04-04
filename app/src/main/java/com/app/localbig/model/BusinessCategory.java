package com.app.localbig.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BusinessCategory")
public class BusinessCategory {

    @DatabaseField(id = true)
    @SerializedName("id")
    @Expose
    private String id;

    @DatabaseField(canBeNull = true)
    @SerializedName("imageBase64")
    @Expose
    private String imageBase64;

    @DatabaseField(canBeNull = true)
    @SerializedName("imageFileName")
    @Expose
    private String imageFileName;

    @DatabaseField(canBeNull = true)
    @SerializedName("imagePath")
    @Expose
    private String imagePath;

    @DatabaseField(canBeNull = true)
    @SerializedName("description")
    @Expose
    private String description;

    @DatabaseField(canBeNull = true)
    @SerializedName("name")
    @Expose
    private String name;


    public BusinessCategory() {
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The imageBase64
     */
    public String getImageBase64() {
        return imageBase64;
    }

    /**
     * 
     * @param imageBase64
     *     The imageBase64
     */
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    /**
     * 
     * @return
     *     The imageFileName
     */
    public String getImageFileName() {
        return imageFileName;
    }

    /**
     * 
     * @param imageFileName
     *     The imageFileName
     */
    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    /**
     * 
     * @return
     *     The imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * 
     * @param imagePath
     *     The imagePath
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

}
