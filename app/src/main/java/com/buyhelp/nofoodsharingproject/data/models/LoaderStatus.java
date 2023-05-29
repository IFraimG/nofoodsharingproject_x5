package com.buyhelp.nofoodsharingproject.data.models;

public class LoaderStatus {
    public enum Status {
        LOADING,
        LOADED,
        FAILURE
    }

    private final Status status;
    private final String message;

    public static final LoaderStatus LOADING = new LoaderStatus(Status.LOADING, null);
    public static final LoaderStatus LOADED = new LoaderStatus(Status.LOADED, null);
    public static final LoaderStatus FAILURE = new LoaderStatus(Status.FAILURE, null);

    public LoaderStatus(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
