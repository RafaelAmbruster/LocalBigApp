package com.app.localbig.data.dao;


public class ApplicationCategoryException extends RuntimeException {

    private String message;

    public ApplicationCategoryException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public ApplicationCategoryException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
