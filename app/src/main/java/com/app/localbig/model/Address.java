package com.app.localbig.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

public class Address {

    @DatabaseField(id = true)
    @SerializedName("id")
    @Expose
    private String id;

    @DatabaseField(canBeNull = true)
    @SerializedName("cityId")
    @Expose
    private String cityId;

    @DatabaseField(canBeNull = true)
    @SerializedName("zipCode")
    @Expose
    private Integer zipCode;

    @DatabaseField(canBeNull = true)
    @SerializedName("streetAddress2")
    @Expose
    private String streetAddress2;

    @DatabaseField(canBeNull = true)
    @SerializedName("streetAddress")
    @Expose
    private String streetAddress;


    public Address() {
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The cityId
     */
    public String getCityId() {
        return cityId;
    }

    /**
     *
     * @param cityId
     * The cityId
     */
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    /**
     *
     * @return
     * The zipCode
     */
    public Integer getZipCode() {
        return zipCode;
    }

    /**
     *
     * @param zipCode
     * The zipCode
     */
    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    /**
     *
     * @return
     * The streetAddress2
     */
    public String getStreetAddress2() {
        return streetAddress2;
    }

    /**
     *
     * @param streetAddress2
     * The streetAddress2
     */
    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    /**
     *
     * @return
     * The streetAddress
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     *
     * @param streetAddress
     * The streetAddress
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
}