package com.app.localbig.data.dao;


public class ApplicationUserException extends RuntimeException {

    private String message;

    public ApplicationUserException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public ApplicationUserException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
