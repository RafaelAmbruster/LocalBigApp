package com.app.localbig.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LocalEvent")
public class LocalEvent extends AbstractMapModel implements Comparable<LocalEvent> {

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
    @SerializedName("applicationUserId")
    @Expose
    private String applicationUserId;

    @DatabaseField(canBeNull = true)
    @SerializedName("formattedAddress")
    @Expose
    private String formattedAddress;

    @DatabaseField(canBeNull = true)
    @SerializedName("freeEntrance")
    @Expose
    private Boolean freeEntrance;

    @DatabaseField(canBeNull = true)
    @SerializedName("endDate")
    @Expose
    private String endDate;

    @DatabaseField(canBeNull = true)
    @SerializedName("startDate")
    @Expose
    private String startDate;

    @DatabaseField(canBeNull = true)
    @SerializedName("description")
    @Expose
    private String description;

    @DatabaseField(canBeNull = true)
    @SerializedName("title")
    @Expose
    private String title;

    @DatabaseField(canBeNull = true)
    private float distance;

    public LocalEvent() {
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
     *     The applicationUserId
     */
    public String getApplicationUserId() {
        return applicationUserId;
    }

    /**
     * 
     * @param applicationUserId
     *     The applicationUserId
     */
    public void setApplicationUserId(String applicationUserId) {
        this.applicationUserId = applicationUserId;
    }

    /**
     * 
     * @return
     *     The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 
     * @param longitude
     *     The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     * @return
     *     The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 
     * @param latitude
     *     The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     * @return
     *     The formattedAddress
     */
    public String getFormattedAddress() {
        return formattedAddress;
    }

    /**
     * 
     * @param formattedAddress
     *     The formattedAddress
     */
    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    /**
     * 
     * @return
     *     The freeEntrance
     */
    public Boolean getFreeEntrance() {
        return freeEntrance;
    }

    /**
     * 
     * @param freeEntrance
     *     The freeEntrance
     */
    public void setFreeEntrance(Boolean freeEntrance) {
        this.freeEntrance = freeEntrance;
    }

    /**
     * 
     * @return
     *     The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * 
     * @param endDate
     *     The endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 
     * @return
     *     The startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 
     * @param startDate
     *     The startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
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
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(LocalEvent model) {
        if (this.getDistance() < model.getDistance()) return -1;
        else if (this.getDistance() > model.getDistance()) return 1;
        else return 0;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(Double.parseDouble(getLatitude()), Double.parseDouble(getLongitude()));
    }

    @Override
    public Boolean isFavorite() {
        return favorite == null ? false : favorite;
    }

    @Override
    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
