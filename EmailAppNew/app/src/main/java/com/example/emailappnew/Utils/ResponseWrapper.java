package com.example.emailappnew.Utils;

/**
 * Author: Jatin N Gupte, Dheeraj Mirashi
 * Group No: 50
 */
public class ResponseWrapper {
    public String response;
    public boolean success;

    public ResponseWrapper(String response, boolean success) {
        this.response = response;
        this.success = success;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
