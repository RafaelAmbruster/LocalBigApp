package com.app.localbig.model;

import android.support.annotation.NonNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.app.localbig.data.AppDatabaseHelper;
import com.app.localbig.data.AppDatabaseManager;
import com.app.localbig.data.dao.BusinessDAO;
import com.app.localbig.data.dao.BusinessPictureDAO;
import com.app.localbig.data.dao.CouponDAO;
import com.app.localbig.data.dao.ReviewDAO;
import com.app.localbig.data.dao.VideoDAO;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Business")
public class Business extends AbstractMapModel implements Comparable<Business> {

    @DatabaseField(id = true)
    @SerializedName("id")
    @Expose
    private String id;

    @DatabaseField(foreign = true, index = true)
    @SerializedName("businessCategory")
    @Expose
    private BusinessCategory businessCategory;

    @DatabaseField(canBeNull = true)
    @SerializedName("reviewAverageStars")
    @Expose
    private Double reviewAverageStars;

    @DatabaseField(canBeNull = true)
    @SerializedName("applicationUserId")
    @Expose
    private String applicationUserId;

    @DatabaseField(canBeNull = true)
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    @DatabaseField(canBeNull = true)
    @SerializedName("webSiteUrl")
    @Expose
    private String webSiteUrl;

    @DatabaseField(canBeNull = true)
    @SerializedName("formattedAddress")
    @Expose
    private String formattedAddress;

    @DatabaseField(canBeNull = true)
    @SerializedName("sinceDate")
    @Expose
    private String sinceDate;

    @DatabaseField(canBeNull = true)
    @SerializedName("description")
    @Expose
    private String description;

    @DatabaseField(canBeNull = true)
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("videos")
    @Expose
    private List<Video> videos = new ArrayList<>();

    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = new ArrayList<>();

    @SerializedName("businessPictures")
    @Expose
    private List<BusinessPicture> businessPictures = new ArrayList<>();

    @SerializedName("coupons")
    @Expose
    private List<Coupon> coupons = new ArrayList<>();

    @DatabaseField(canBeNull = true)
    private float distance;
    @ForeignCollectionField
    private ForeignCollection<Video> videosdb;
    @ForeignCollectionField
    private ForeignCollection<Review> reviewsdb;
    @ForeignCollectionField
    private ForeignCollection<BusinessPicture> businessPicturesdb;
    @ForeignCollectionField
    private ForeignCollection<Coupon> couponsdb;

    public List<Video> getVideodb() {
        List<Video> list = new ArrayList<>();
        for (Video m : videosdb) {
            list.add(m);
        }
        return list;
    }

    public void setVideodb(ForeignCollection<Video> list) {
        this.videosdb = list;
    }

    public List<Review> getReviewdb() {
        List<Review> list = new ArrayList<>();
        for (Review m : reviewsdb) {
            list.add(m);
        }
        return list;
    }

    public void setReviewdb(ForeignCollection<Review> list) {
        this.reviewsdb = list;
    }

    public List<BusinessPicture> getBusinessPicturesdb() {
        List<BusinessPicture> list = new ArrayList<>();
        for (BusinessPicture m : businessPicturesdb) {
            list.add(m);
        }
        return list;
    }

    public void setPicturedb(ForeignCollection<BusinessPicture> list) {
        this.businessPicturesdb = list;
    }

    public List<Coupon> getCoupondb() {
        List<Coupon> list = new ArrayList<>();
        for (Coupon m : couponsdb) {
            list.add(m);
        }
        return list;
    }

    public void setCouponsdb(ForeignCollection<Coupon> list) {
        this.couponsdb = list;
    }

    public Business() {
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

    /**
     * @return The businessCategoryId
     */
    public BusinessCategory getBusinessCategory() {
        return businessCategory;
    }

    /**
     * @param businessCategory The businessCategoryId
     */
    public void setBusinessCategory(BusinessCategory businessCategory) {
        this.businessCategory = businessCategory;
    }

    /**
     * @return The applicationUserId
     */
    public String getApplicationUserId() {
        return applicationUserId;
    }

    /**
     * @param applicationUserId The applicationUserId
     */
    public void setApplicationUserId(String applicationUserId) {
        this.applicationUserId = applicationUserId;
    }

    /**
     * @return The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber The phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return The webSiteUrl
     */
    public String getWebSiteUrl() {
        return webSiteUrl;
    }

    /**
     * @param webSiteUrl The webSiteUrl
     */
    public void setWebSiteUrl(String webSiteUrl) {
        this.webSiteUrl = webSiteUrl;
    }

    /**
     * @return The formattedAddress
     */
    public String getFormattedAddress() {
        return formattedAddress;
    }

    /**
     * @param formattedAddress The formattedAddress
     */
    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    /**
     * @return The sinceDate
     */
    public String getSinceDate() {
        return sinceDate;
    }

    /**
     * @param sinceDate The sinceDate
     */
    public void setSinceDate(String sinceDate) {
        this.sinceDate = sinceDate;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return The videos
     */
    public List<Video> getVideos() {
        return videos;
    }

    /**
     * @param videos The videos
     */
    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    /**
     * @return The reviews
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * @param reviews The reviews
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * @return The businessPictures
     */
    public List<BusinessPicture> getBusinessPictures() {
        return businessPictures;
    }

    /**
     * @param businessPictures The businessPictures
     */
    public void setBusinessPictures(List<BusinessPicture> businessPictures) {
        this.businessPictures = businessPictures;
    }

    /**
     * @return The reviewAverageStars
     */
    public Double getReviewAverageStars() {
        return reviewAverageStars;
    }

    /**
     * @param reviewAverageStars The reviewAverageStars
     */
    public void setReviewAverageStars(Double reviewAverageStars) {
        this.reviewAverageStars = reviewAverageStars;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Business model) {
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

    public void SyncrhonizeTodb() {

        if (getVideos() != null) {
            try {
                videosdb = new BusinessDAO(AppDatabaseManager.getInstance().getHelper()).getDao().getEmptyForeignCollection("videosdb");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (Video item : getVideos()) {
                if (new VideoDAO(AppDatabaseManager.getInstance().getHelper()).Get(item) == null)
                    videosdb.add(item);
            }
        }

        if (getCoupons() != null) {
            try {
                couponsdb = new BusinessDAO(AppDatabaseManager.getInstance().getHelper()).getDao().getEmptyForeignCollection("couponsdb");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (Coupon item : getCoupons()) {
                if (new CouponDAO(AppDatabaseManager.getInstance().getHelper()).Get(item) == null)
                    couponsdb.add(item);
            }
        }

        if (getReviews() != null) {
            try {
                reviewsdb = new BusinessDAO(AppDatabaseManager.getInstance().getHelper()).getDao().getEmptyForeignCollection("reviewsdb");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (Review item : getReviews()) {
                if (new ReviewDAO(AppDatabaseManager.getInstance().getHelper()).Get(item) == null)
                    reviewsdb.add(item);
            }
        }

        if (getBusinessPictures() != null) {
            try {
                businessPicturesdb = new BusinessDAO(AppDatabaseManager.getInstance().getHelper()).getDao().getEmptyForeignCollection("businessPicturesdb");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (BusinessPicture item : getBusinessPictures()) {
                if (new BusinessPictureDAO(AppDatabaseManager.getInstance().getHelper()).Get(item) == null)
                    businessPicturesdb.add(item);
            }
        }
    }

    public void SyncrhonizeFromdb() {

        if (getVideodb() != null)
            videos = new ArrayList<>(getVideodb());

        if (getCoupondb() != null)
            coupons = new ArrayList<>(getCoupondb());

        if (getReviewdb() != null)
            reviews = new ArrayList<>(getReviewdb());

        if (getBusinessPicturesdb() != null)
            businessPictures = new ArrayList<>(getBusinessPicturesdb());

    }

}

