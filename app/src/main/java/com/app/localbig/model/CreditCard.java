package com.app.localbig.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditCard {

    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("expirationMonth")
    @Expose
    private Integer expirationMonth;
    @SerializedName("expirationYear")
    @Expose
    private Integer expirationYear;
    @SerializedName("id")
    @Expose
    private String id;

    /**
     * 
     * @return
     *     The number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 
     * @param number
     *     The number
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 
     * @return
     *     The expirationMonth
     */
    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    /**
     * 
     * @param expirationMonth
     *     The expirationMonth
     */
    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    /**
     * 
     * @return
     *     The expirationYear
     */
    public Integer getExpirationYear() {
        return expirationYear;
    }

    /**
     * 
     * @param expirationYear
     *     The expirationYear
     */
    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
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

}
