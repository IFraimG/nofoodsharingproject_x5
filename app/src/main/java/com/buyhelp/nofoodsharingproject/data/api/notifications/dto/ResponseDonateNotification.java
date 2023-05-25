package com.buyhelp.nofoodsharingproject.data.api.notifications.dto;

import com.google.gson.annotations.SerializedName;

public class ResponseDonateNotification {
    @SerializedName("multicast_id")
    int multicast_id;

    @SerializedName("success")
    int success;

    @SerializedName("failure")
    int failure;

    @SerializedName("results")
    ResultDonate results;

    public ResponseDonateNotification() {}

    public ResponseDonateNotification(int multicast_id, int success, int failure, ResultDonate results) {
        this.multicast_id = multicast_id;
        this.success = success;
        this.failure = failure;
        this.results = results;
    }

    public int getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(int multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public ResultDonate getResults() {
        return results;
    }

    public void setResults(ResultDonate results) {
        this.results = results;
    }

    static class ResultDonate {
        @SerializedName("message_id")
        public String message_id;

        @SerializedName("error")
        public String error;

        public ResultDonate() {}

        public ResultDonate(String message_id, String error) {
            this.message_id = message_id;
            this.error = error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        public String getMessage_id() {
            return message_id;
        }
    }
}
