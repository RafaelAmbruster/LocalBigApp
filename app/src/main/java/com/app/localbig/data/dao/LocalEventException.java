package com.app.localbig.data.dao;


public class LocalEventException extends RuntimeException {

    private String message;

    public LocalEventException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public LocalEventException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
