package com.circles.circlesapp.retrofit.api;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.SSLHandshakeException;

import com.circles.circlesapp.R;
import com.circles.circlesapp.retrofit.api.network.CheckNetwork;
import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public class Request<t> extends DisposableObserver<t> {
    private Context context;
    private RequestListener<t> listener;

    public Request(Context context, RequestListener<t> listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onNext(t t) {
        if (t == null) {
            listener.onEmpty(context.getString(R.string.no_data_found));
        } else {
            MutableLiveData<t> data = new MutableLiveData<>();
            data.setValue(t);
            listener.onResponse(data);
        }
    }

    @Override
    public void onError(Throwable t) {
        if (!CheckNetwork.isConnected(context)) {
            listener.onNetWorkError("No Data Found");

        } else {
            if (t instanceof HttpException) {
                int code = ((HttpException) t).code();
                try {
                    String error;
                    String responseStrings = ((HttpException) t).response().errorBody().string();
                    JSONObject jsonObject = new JSONObject(responseStrings);
                    if (jsonObject.has("msg")) {
                        error = jsonObject.getString("msg");
                    } else if (jsonObject.has("errors")){
                        error = jsonObject.getString("errors");
                    }else {
                        error=t.getMessage();
                    }
                    if (code == 401) {
                        listener.onSessionExpired(error);
                    } else if (code == 403) {
                        listener.onSessionExpired(error);
                    } else if (code == 404) {
                        listener.onError(error);
                    } else if (code == 405) {
                        listener.onError(error);
                    } else if (code == 400) {
                        listener.onError(error);
                    } else if (code == 500) {
                        listener.onError(error);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (t instanceof java.net.SocketTimeoutException) {
                listener.onError(context.getString(R.string.socketTimeout));
            } else if (t instanceof JsonSyntaxException) {
                listener.onError(context.getString(R.string.jsonError));
                Log.d("onError", "Throwable " + t);

            } else if (t instanceof SSLHandshakeException) {
                listener.onError(context.getString(R.string.connectionError));
            } else {
                listener.onError(context.getString(R.string.serverError));
            }
        }
    }

    @Override
    public void onComplete() {

    }
}
