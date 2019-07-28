package com.circles.circlesapp.retrofit;


public class ResponseApi<t> {
    private String message;
    private t data;

    public String getMessage() {
        return message;
    }

    public t getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ResponseApi{" +
                "message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
