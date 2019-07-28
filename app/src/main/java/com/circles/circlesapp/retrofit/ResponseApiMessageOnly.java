package com.circles.circlesapp.retrofit;


public class ResponseApiMessageOnly {
    private String message;

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResponseApi{" +
                "message='" + message + '\'' +
                '}';
    }
}
