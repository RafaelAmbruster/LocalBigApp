
package com.app.localbig.model;

public class Plan {

    private String id;
    private int durationInMonths;
    private String description;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Plan() {
    }

    /**
     * 
     * @param id
     * @param description
     * @param durationInMonths
     */
    public Plan(String id, int durationInMonths, String description) {
        this.id = id;
        this.durationInMonths = durationInMonths;
        this.description = description;
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
     *     The durationInMonths
     */
    public int getDurationInMonths() {
        return durationInMonths;
    }

    /**
     * 
     * @param durationInMonths
     *     The durationInMonths
     */
    public void setDurationInMonths(int durationInMonths) {
        this.durationInMonths = durationInMonths;
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

}
