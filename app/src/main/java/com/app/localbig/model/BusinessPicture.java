package com.app.localbig.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "BusinessPicture")
public class BusinessPicture {

    @DatabaseField(canBeNull = true)
    @SerializedName("businessId")
    @Expose
    private String businessId;

    @DatabaseField(canBeNull = true)
    @SerializedName("imagePath")
    @Expose
    private String imagePath;

    @DatabaseField(canBeNull = true)
    @SerializedName("imageBase64")
    @Expose
    private String imageBase64;

    @DatabaseField(canBeNull = true)
    @SerializedName("imageFileName")
    @Expose
    private String imageFileName;

    @DatabaseField(id = true)
    @SerializedName("id")
    @Expose
    private String id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, index = true)
    private Business business;


    public BusinessPicture() {
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    /**
     * @return The businessId
     */
    public String getBusinessId() {
        return businessId;
    }

    /**
     * @param businessId The businessId
     */
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    /**
     * @return The imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath The imagePath
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return The imageBase64
     */
    public String getImageBase64() {
        return imageBase64;
    }

    /**
     * @param imageBase64 The imageBase64
     */
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    /**
     * @return The imageFileName
     */
    public String getImageFileName() {
        return imageFileName;
    }

    /**
     * @param imageFileName The imageFileName
     */
    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

}