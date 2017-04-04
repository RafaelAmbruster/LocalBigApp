package com.app.localbig.data.dao;


public class VideoException extends RuntimeException {

    private String message;

    public VideoException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public VideoException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
