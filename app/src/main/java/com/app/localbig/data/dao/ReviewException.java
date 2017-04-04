package com.app.localbig.data.dao;


public class ReviewException extends RuntimeException {

    private String message;

    public ReviewException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public ReviewException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
