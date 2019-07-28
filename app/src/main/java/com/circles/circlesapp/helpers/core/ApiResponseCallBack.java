package com.circles.circlesapp.helpers.core;

public interface ApiResponseCallBack<T> {
    void success(T s);

    void fail(String message);
}
