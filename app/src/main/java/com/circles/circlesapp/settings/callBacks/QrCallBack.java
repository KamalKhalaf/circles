package com.circles.circlesapp.settings.callBacks;



import com.circles.circlesapp.qr.ProfileQrFragment;
import com.circles.circlesapp.qr.ScanQrActivity;

public interface QrCallBack extends BaseCallBack {
    default  ProfileQrFragment getFragment(){return null;}
    default ScanQrActivity getQrActivity(){return null;}
    default void onSuccess(String msg){}
    default void onError(String msg){}

    default void closeFragment(){}
}
