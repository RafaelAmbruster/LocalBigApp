package com.app.localbig.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class City {

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("stateId")
    @Expose
    private String stateId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("addresses")
    @Expose
    private List<Address> addresses = new ArrayList<>();

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
     *     The stateId
     */
    public String getStateId() {
        return stateId;
    }

    /**
     * 
     * @param stateId
     *     The stateId
     */
    public void setStateId(String stateId) {
        this.stateId = stateId;
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
     *     The addresses
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     *
     * @param addresses
     *     The addresses
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

}
