package com.circles.circlesapp.messaging;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;

public class SingletonSocket {
    private static Socket inst;
    private static final String SOCKET_URL = "http://ec2-18-216-242-74.us-east-2.compute.amazonaws.com:6001";

    public static Socket newInstance() {
        if (inst == null){
            try {
                IO.Options options = new IO.Options();
                options.query = "authorization=" + MyServiceInterceptor.getAuth();
                inst = IO.socket(SOCKET_URL, options);
            } catch (URISyntaxException e) {
                Log.d("", "joinSocket: " + e.getMessage());
                e.printStackTrace();
            }
            return inst;
        }
        else return inst;
    }

    public void disconnect(){
        inst.off("connection");
        inst.disconnect();
    }
}
