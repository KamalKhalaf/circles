package com.circles.circlesapp.settings.ui;

import android.app.ProgressDialog;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.widget.Toast;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.LogoutLayoutBinding;
import com.circles.circlesapp.helpers.SharedPrefHelper;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.loginsignup.LoginActivity;
import com.circles.circlesapp.retrofit.Api;
import com.circles.circlesapp.retrofit.ResponseApi;
import com.circles.circlesapp.retrofit.RetrofitClient;
import com.circles.circlesapp.retrofit.api.Request;
import com.circles.circlesapp.retrofit.api.RequestListener;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/*
 * Created By mabrouk on 30/01/19
 * com.circles
 */

public class LogOutFragment extends BaseDailogFragment {
    private LogoutLayoutBinding layoutBinding;
    SharedPrefHelper mSharedPrefHelper;
    private Api api;
    private ProgressDialog mProgressDialog;

    public static LogOutFragment getInstance() {
        return new LogOutFragment();
    }

    @Override
    public int setContentView() {
        return R.layout.logout_layout;
    }

    @Override
    public void iniViews() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Logout .....");
        api = RetrofitClient.getInstance().getApi();
        layoutBinding = DataBindingUtil.bind(view);
        mSharedPrefHelper = new SharedPrefHelper(getContext());
        layoutBinding.cancel.setOnClickListener(e -> {
            dismiss();
        });

        layoutBinding.confirm.setOnClickListener(e -> {
            reqLogout();
        });
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void reqLogout() {
        mProgressDialog.show();
        api.logout(MyServiceInterceptor.getAuth())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Request<>(getContext(), new RequestListener<ResponseApi>() {
                    @Override
                    public void onResponse(MutableLiveData<ResponseApi> data) {
                        data.observe(LogOutFragment.this, e -> {
                            mProgressDialog.dismiss();
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            if (e.getMessage().contains("Successfully")) {
                                mSharedPrefHelper.clearAll();
                                mSharedPrefHelper.clearAllSharedPreferences();
                                dismiss();
                                startLoginActivity();
                            }

                        });
                    }

                    @Override
                    public void onEmpty(String msg) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String msg) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSessionExpired(String msg) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNetWorkError(String msg) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
