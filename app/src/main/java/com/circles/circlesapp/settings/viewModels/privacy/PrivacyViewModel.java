package com.circles.circlesapp.settings.viewModels.privacy;

/*
 * Created By mabrouk on 29/01/19
 * com.circles
 */

import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.circles.circlesapp.helpers.SharedPrefHelper;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.retrofit.Api;
import com.circles.circlesapp.retrofit.ResponseApi;
import com.circles.circlesapp.retrofit.RetrofitClient;
import com.circles.circlesapp.retrofit.api.Request;
import com.circles.circlesapp.retrofit.api.RequestListener;
import com.circles.circlesapp.settings.PrivacyModel;
import com.circles.circlesapp.settings.callBacks.PrivacyCallBack;
import com.circles.circlesapp.settings.viewModels.base.BaseViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PrivacyViewModel<v extends PrivacyCallBack> extends BaseViewModel<v> implements PrivacyVmodel<v> {
    private ObservableField<PrivacyModel> privacyModel = new ObservableField<>();
    private Api api;
    private ProgressDialog mProgressDialog;

    public PrivacyViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.getInstance().getApi();
    }

    public ObservableField<PrivacyModel> getPrivacyModel() {
        return privacyModel;
    }


    @Override
    public void age_location(View view) {
        if (privacyModel == null || privacyModel.get() == null) return;
        privacyModel.get().setAge_location_privacy(!privacyModel.get().getProfile_privacy());
    }

    @Override
    public void following(View view) {
        if (privacyModel == null || privacyModel.get() == null) return;
        privacyModel.get().setFollowing_followers_privacy(!privacyModel.get().getFollowing_followers_privacy());
    }

    @Override
    public void privateAccount(View view) {
        if (privacyModel == null || privacyModel.get() == null) return;
        privacyModel.get().setProfile_privacy(!privacyModel.get().getProfile_privacy());
    }

    @Override
    public void confirmChanges(View view) {
        reqUpdatePrivacy();
    }

    @Override
    public void back(View view) {
        getView().getNavController().popBackStack();
    }

    @Override
    public void reqPrivacy() {
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        api.getPrivacy(MyServiceInterceptor.userId, MyServiceInterceptor.getAuth())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Request<>(getView().getFragment().getContext(), new RequestListener<PrivacyModel>() {
                    @Override
                    public void onResponse(MutableLiveData<PrivacyModel> data) {
                        data.observe(getView().getFragment(), e -> {
                            if (e == null) return;
                            mProgressDialog.dismiss();

                            privacyModel.set(e);
                        });
                    }

                    @Override
                    public void onEmpty(String msg) {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(String msg) {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onSessionExpired(String msg) {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onNetWorkError(String msg) {
                        mProgressDialog.dismiss();
                    }
                }));
    }

    @Override
    public void reqUpdatePrivacy() {
        mProgressDialog.setMessage("Update .....");
        mProgressDialog.show();
        api.updatePrivacy(MyServiceInterceptor.userId, MyServiceInterceptor.getAuth(), privacyModel.get().getProfile_privacy() ? 1 : 0, privacyModel.get().getFollowing_followers_privacy() ? 1 : 0,
                privacyModel.get().getAge_location_privacy() ? 1 : 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Request<>(getView().getFragment().getContext(), new RequestListener<ResponseApi>() {
                    @Override
                    public void onResponse(MutableLiveData<ResponseApi> data) {
                        data.observe(getView().getFragment(), e -> {
                            mProgressDialog.dismiss();
                            SharedPrefHelper prefHelper = new SharedPrefHelper(getView().getFragment().getActivity());
                            Toast.makeText(getView().getFragment().getContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                            if (privacyModel != null && privacyModel.get() != null) {
                                prefHelper.setShouldShowAge_Location(privacyModel.get().getAge_location_privacy());
                                prefHelper.setShouldShowFollowing(privacyModel.get().getFollowing_followers_privacy());
                                prefHelper.setShouldShowProfile(privacyModel.get().getProfile_privacy());
                            }
                        });
                    }

                    @Override
                    public void onEmpty(String msg) {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(getView().getFragment().getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onSessionExpired(String msg) {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onNetWorkError(String msg) {
                        mProgressDialog.dismiss();
                    }
                }));
    }

    @Override
    public void setUpProgress() {
        mProgressDialog = new ProgressDialog(getView().getFragment().getContext());
        mProgressDialog.setCanceledOnTouchOutside(false);
    }
}
