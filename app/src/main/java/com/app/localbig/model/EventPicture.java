package com.app.localbig.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventPicture {

    @SerializedName("imagePath")
    @Expose
    private String imagePath;
    @SerializedName("imageFileName")
    @Expose
    private String imageFileName;
    @SerializedName("imageBase64")
    @Expose
    private String imageBase64;
    @SerializedName("localEventId")
    @Expose
    private String localEventId;
    @SerializedName("id")
    @Expose
    private String id;

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
     *     The localEventId
     */
    public String getLocalEventId() {
        return localEventId;
    }

    /**
     * 
     * @param localEventId
     *     The localEventId
     */
    public void setLocalEventId(String localEventId) {
        this.localEventId = localEventId;
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
