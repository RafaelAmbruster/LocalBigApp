package com.app.localbig.model;

import java.util.ArrayList;

public class AutoSuggestSearchResult {

    private ArrayList<Business> businesses;
    private ArrayList<LocalEvent> events;
    private ArrayList<Coupon> coupons;

    private boolean businessExpansion;
    private boolean eventExpansion;
    private boolean couponExpansion;

    private int totalBusinessesFound;
    private int totalCouponsFound;
    private int totalEventsFound;

    public ArrayList<Business> getBusinesses() {
        return businesses;
    }

    public void setPerformers(ArrayList<Business> businesses) {
        this.businesses = businesses;
    }

    public boolean isBusinessExpansion() {
        return businessExpansion;
    }

    public void setBusinessExpansion(boolean businessExpansion) {
        this.businessExpansion = businessExpansion;
    }

    public boolean isEventExpansion() {
        return eventExpansion;
    }

    public void setEventExpansion(boolean eventExpansion) {
        this.eventExpansion = eventExpansion;
    }

    public ArrayList<LocalEvent> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<LocalEvent> events) {
        this.events = events;
    }

    public int getTotalCouponsFound() {
        return totalCouponsFound;
    }

    public void setTotalCouponsFound(int totalCouponsFound) {
        this.totalCouponsFound = totalCouponsFound;
    }

    public ArrayList<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<Coupon> coupons) {
        this.coupons = coupons;
    }

    public int getTotalBusinessesFound() {
        return totalBusinessesFound;
    }

    public void setTotalBusinessesFound(int totalBusinessesFound) {
        this.totalBusinessesFound = totalBusinessesFound;
    }

    public boolean isCouponExpansion() {
        return couponExpansion;
    }

    public void setCouponExpansion(boolean couponExpansion) {
        this.couponExpansion = couponExpansion;
    }

    public int getTotalEventsFound() {
        return totalEventsFound;
    }

    public void setTotalEventsFound(int totalEventsFound) {
        this.totalEventsFound = totalEventsFound;
    }
}