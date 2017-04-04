package com.app.localbig.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Video")
public class Video {

    @DatabaseField(canBeNull = true)
    @SerializedName("businessId")
    @Expose
    private String businessId;

    @DatabaseField(canBeNull = true)
    @SerializedName("title")
    @Expose
    private String title;

    @DatabaseField(canBeNull = true)
    @SerializedName("url")
    @Expose
    private String url;

    @DatabaseField(id = true)
    @SerializedName("id")
    @Expose
    private String id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, index = true)
    private Business business;

    public Video() {
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
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
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