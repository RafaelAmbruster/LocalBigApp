package com.app.localbig.data.dao;


public class AddressException extends RuntimeException {

    private String message;

    public AddressException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public AddressException(Exception exception) {
        super(exception);
        this.setStackTrace(exception.getStackTrace());
        this.message = exception.getMessage();
    }

    @Override
    public String getMessage() {
        return getMessage();
    }
}
