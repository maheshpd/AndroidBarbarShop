package com.arfeenkhan.androidbarbershop.model;

import java.util.List;



public class FCMResponse {
    private long multicate_id;
    private int success;
    private int failure;
    private int canonical_ids;
    private List<Result> results;

    public FCMResponse() {
    }

    public long getMulticate_id() {
        return multicate_id;
    }

    public void setMulticate_id(long multicate_id) {
        this.multicate_id = multicate_id;
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

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    class Result {
        private String message_id;

        public Result() {
        }

        public Result(String message_id) {
            this.message_id = message_id;
        }

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }
    }

}
