package com.app.localbig.data.dao;


public class CouponException extends RuntimeException {

    private String message;

    public CouponException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public CouponException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
