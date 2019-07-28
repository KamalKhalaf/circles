package com.circles.circlesapp.settings.viewModels.qr;



import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.circles.circlesapp.profile.UserProfileActivity;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import com.circles.circlesapp.R;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.qr.ScanQrActivity;
import com.circles.circlesapp.retrofit.Api;
import com.circles.circlesapp.retrofit.ResponseApi;
import com.circles.circlesapp.retrofit.RetrofitClient;
import com.circles.circlesapp.retrofit.api.Request;
import com.circles.circlesapp.retrofit.api.RequestListener;
import com.circles.circlesapp.settings.callBacks.QrCallBack;
import com.circles.circlesapp.settings.viewModels.base.BaseViewModel;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class QrViewModel<v extends QrCallBack> extends BaseViewModel<v> {
    private ObservableField<String> userName = new ObservableField<>();
    private ObservableField<String> qrTitle = new ObservableField<>();
    private ObservableField<String> profile_image = new ObservableField<>();
    private ProgressDialog mProgressDialog;
    Api api;

    @BindingAdapter("loadIMG")
    public static void loadIMG(CircleImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .apply(RequestOptions.placeholderOf(R.drawable.sign_up_profile))
                .into(view);
    }


    public QrViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.getInstance().getApi();
    }

    public String getQrTitle() {
        return this.qrTitle.get();
    }

    public void setQrTitle(String qrTitle) {
        this.qrTitle.set(qrTitle);
    }

    public ObservableField<String> getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set("@" + userName);
        setQrTitle(userName + "'s  Qr Code");
    }

    public ObservableField<String> getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image.set(profile_image);
    }

    public void scan(View view) {
        ScanQrActivity.start(view.getContext());
        getView().closeFragment();
    }

    public void close(View view) {
        getView().getFragment().dismiss();
    }

    public Bitmap generateQrCode(String id , String userName, int width, int height) {
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;
        Bitmap bitmap = null;
        QRGEncoder qrgEncoder = new QRGEncoder(id +" " +userName, null, QRGContents.Type.TEXT, smallerDimension);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void reqFollowOrUnFollow(Context context ,String userName ,int id , int customerId) {
        mProgressDialog = new ProgressDialog(getView().getQrActivity());
        mProgressDialog.setMessage("Follow .... ");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        api.followOrUnFollow(MyServiceInterceptor.getAuth(), userName , id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Request<>(getView().getQrActivity(), new RequestListener<ResponseApi>() {
                    @Override
                    public void onResponse(MutableLiveData<ResponseApi> data) {
                        data.observe(getView().getQrActivity(),e ->{
                            mProgressDialog.dismiss();
                            UserProfileActivity.start(context, customerId);
                            getView().onSuccess(e.getMessage());
                        });
                    }

                    @Override
                    public void onEmpty(String msg) {
                        mProgressDialog.dismiss();
                        getView().onError(msg);
                    }

                    @Override
                    public void onError(String msg) {
                        mProgressDialog.dismiss();
                        getView().onError(msg);
                    }

                    @Override
                    public void onSessionExpired(String msg) {
                        mProgressDialog.dismiss();
                        getView().onError(msg);
                    }

                    @Override
                    public void onNetWorkError(String msg) {
                        mProgressDialog.dismiss();
                        getView().onError(msg);
                    }
                }));
    }

}
