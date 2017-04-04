package com.app.localbig.rest;

import com.app.localbig.model.AccountLoginResponse;
import com.app.localbig.model.AccountRegistrationResponse;
import com.app.localbig.model.Address;
import com.app.localbig.model.ApplicationUser;
import com.app.localbig.model.AutoSuggestSearchResult;
import com.app.localbig.model.Business;
import com.app.localbig.model.BusinessCategory;
import com.app.localbig.model.BusinessFilter;
import com.app.localbig.model.BusinessResponse;
import com.app.localbig.model.City;
import com.app.localbig.model.Country;
import com.app.localbig.model.Coupon;
import com.app.localbig.model.CreateResponse;
import com.app.localbig.model.LocalEvent;
import com.app.localbig.model.State;
import com.app.localbig.model.UserFavoriteBusiness;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiInterface {

    /**
     * Login
     * @return
     */
    @FormUrlEncoded
    @POST("api/CustomLogin")
    Call<AccountLoginResponse> Login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("tables/ApplicationUser/register")
    Call<AccountRegistrationResponse> Register(@FieldMap Map<String, String> params);

    @GET("tables/ApplicationUser/{id}?$expand=Businesses,LocalEvents,Notifications")
    Call<ApplicationUser> getAppUserInformation(@Path("id") String userId);

    /**
     * Favorites
     * @return
     */
    @GET("tables/UserFavoriteBusiness")
    Call<List<UserFavoriteBusiness>> getBusinessFavorites(@Query("userId") String userId);

    @FormUrlEncoded
    @POST("tables/UserFavoriteBusiness")
    Call<ResponseBody> addBusinessFavorite(@FieldMap Map<String, String> params);

    @DELETE("tables/UserFavoriteBusiness/{id}")
    Call<ResponseBody> deleteBusinessFavorites(@Path("id") String id);

    /**
     * Business
     * @return
     */
    /*1*/
    @POST("tables/Business/filter?$expand=BusinessCategory,businessPictures")
    Call<List<Business>> getBusiness(@Body BusinessFilter filter);

    /*2*/
    @GET("tables/Business/search?criteria={criteria}")
    Call<List<Business>> getBusinessCriteria(@Path("criteria") String criteria);

    /*3*/
    @GET("tables/Business/{id}?$expand=businessPictures,Reviews,Videos,Coupons,BusinessCategory")
    Call<Business> getBusinessDetail(@Path("id") String id);

    /*4*/
    @GET("tables/Business/search?")
    Observable<List<Business>> autoSuggestBusiness(@Query("criteria") String criteria);

    /*5*/
    @FormUrlEncoded
    @POST("tables/Business")
    Call<BusinessResponse> createBusiness(@FieldMap Map<String, String> params);

    /*6*/
    @FormUrlEncoded
    @POST("tables/BusinessPicture")
    Observable<CreateResponse> createBusinessPicture(@FieldMap Map<String, String> params);

    /*3*/
    @GET("tables/Business/{id}?$expand=businessPictures,Reviews,Videos,Coupons,BusinessCategory")
    Observable<Business> getBusinessFavorite(@Path("id") String id);

    @FormUrlEncoded
    @POST("tables/Review")
    Call<CreateResponse> createBusinessReview(@FieldMap Map<String, String> params);

    /**
     * BusinessCategory
     * @return
     */
    /*1*/
    @GET("tables/BusinessCategory")
    Call<List<BusinessCategory>> getBusinessCategories();

    /*2*/
    @GET("tables/BusinessCategory/{id}/?$expand=businessCategoryImage")
    Call<BusinessCategory> getBusinessCategoryDetail(@Path("id") String id);


    /**
     * LocalEvent
     * @return
     */
    /*1*/
    @GET("tables/LocalEvent")
    Call<List<LocalEvent>> getLocalEvents();

    /*2*/
    @GET("tables/LocalEvent/{id}")
    Call<LocalEvent> getLocalEventDetail(@Path("id") String id);

    /*3*/
    @FormUrlEncoded
    @POST("tables/LocalEvent")
    Call<CreateResponse> createEvent(@FieldMap Map<String, String> params);


    /**
     * Coupon
     * @return
     */
    /*1*/
    @GET("tables/Coupon")
    Call<List<Coupon>> getCoupons();

    /*2*/
    @GET("tables/Coupon/{id}")
    Call<Coupon> getCouponDetail(@Path("id") String id);

    /*3*/
    @FormUrlEncoded
    @POST("tables/Coupon")
    Call<CreateResponse> createBusinessCoupon(@FieldMap Map<String, String> params);

    /**
     * Country
     * @return
     */
    /*1*/
    @GET("tables/Country")
    Call<List<Country>> getCountries();

    /*2*/
    @GET("tables/Country/{id}?$expand=State")
    Call<Country> getCountryDetail(@Path("id") String id);


    /**
     * State
     * @return
     */
    /*1*/
    @GET("tables/State")
    Call<List<State>> getStates();

    /*2*/
    @GET("tables/State/{id}?$expand=Cities")
    Call<State> getStateDetail(@Path("id") String id);


    /**
     * City
     * @return
     */
    /*1*/
    @GET("tables/State")
    Call<List<City>> getCities();

    /*2*/
    @GET("tables/State/{id}")
    Call<City> getCityDetail(@Path("id") String id);


    /**
     * Address
     * @return
     */
    /*1*/
    @GET("tables/Address")
    Call<List<Address>> getAddress();

    /*2*/
    @GET("tables/Address/{id}")
    Call<Address> getAddressDetails(@Path("id") String id);

}
