package com.circles.circlesapp.settings.socialMedia;


import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.retrofit.Api;
import com.circles.circlesapp.retrofit.ResponseApi;
import com.circles.circlesapp.retrofit.RetrofitClient;
import com.circles.circlesapp.retrofit.api.Request;
import com.circles.circlesapp.retrofit.api.RequestListener;
import com.circles.circlesapp.settings.SocialMedia;
import com.circles.circlesapp.settings.SocialMediaResponse;
import com.circles.circlesapp.settings.callBacks.SocialMediaCallBack;
import com.circles.circlesapp.settings.viewModels.base.BaseViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SocialMediaViewModel<v extends SocialMediaCallBack> extends BaseViewModel<v> implements SocialMediaVmodel<v> {
    private Api api;
    private ObservableList<String> mediaAccounts = new ObservableArrayList<>();
    private ObservableList<Boolean> editAccounts = new ObservableArrayList<>();
    private ObservableList<String> errors = new ObservableArrayList<>();
    private ProgressDialog mProgressDialog;

    public SocialMediaViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.getInstance().getApi();
        setUpAccounts();
        setErrors();
    }

    public ObservableList<String> getErrors() {
        return errors;
    }

    public ObservableList<String> getMediaAccounts() {
        return mediaAccounts;
    }

    public ObservableList<Boolean> getEditAccounts() {
        return editAccounts;
    }

    private void setUpAccounts() {
        mediaAccounts.add(0, null);//faceBook
        mediaAccounts.add(1, null);//insta
        mediaAccounts.add(2, null);//towitter
        mediaAccounts.add(3, null);//snap
        mediaAccounts.add(4, null);//linkedIn
        mediaAccounts.add(5, null);//YouTube
        editAccounts.add(0, false);//faceBook
        editAccounts.add(1, false);//insta
        editAccounts.add(2, false);//twitter
        editAccounts.add(3, false);//snap
        editAccounts.add(4, false);//linkedIn
        editAccounts.add(5, false);//YouTube
    }

    private void setErrors() {
        errors.add(0, null);//faceBook
        errors.add(1, null);//insta
        errors.add(2, null);//twitter
        errors.add(3, null);//snap
        errors.add(4, null);//linkedIn
        errors.add(5, null);//YouTube
    }

    @BindingAdapter({"editWatcher", "setError"})
    public static void editWatcher(EditText text, TextWatcher watcher, String msg) {
        text.addTextChangedListener(watcher);
        text.setError(msg);
    }

    public TextWatcher faceBookWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mediaAccounts.set(0, s.toString().isEmpty() ? null : s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher instaWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mediaAccounts.set(1, s.toString().isEmpty() ? null : s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher twitterWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mediaAccounts.set(2, s.toString().isEmpty() ? null : s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher snapWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mediaAccounts.set(3, s.toString().isEmpty() ? null : s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher linkedWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mediaAccounts.set(4, s.toString().isEmpty() ? null : s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher youTubeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mediaAccounts.set(5, s.toString().isEmpty() ? null : s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void editFaceBook() {
        if (mediaAccounts.get(0) != null)
            editAccounts.set(0, true);
    }

    @Override
    public void editInsta() {
        if (mediaAccounts.get(1) != null)
            editAccounts.set(1, true);
    }

    @Override
    public void editTwitter() {
        if (mediaAccounts.get(2) != null)
            editAccounts.set(2, true);
    }

    @Override
    public void editSnap() {
        if (mediaAccounts.get(3) != null)
            editAccounts.set(3, true);
    }

    @Override
    public void editLinked() {
        if (mediaAccounts.get(4) != null)
            editAccounts.set(4, true);
    }

    @Override
    public void editYouTube() {
        if (mediaAccounts.get(5) != null)
            editAccounts.set(5, true);
    }

    @Override
    public void deleteFaceBook() {
        mediaAccounts.set(0, null);
        editAccounts.set(0, false);
    }

    @Override
    public void deleteInsta() {
        mediaAccounts.set(1, null);
        editAccounts.set(1, false);
    }

    @Override
    public void deleteTwitter() {
        mediaAccounts.set(2, null);
        editAccounts.set(2, false);
    }

    @Override
    public void deleteSnap() {
        mediaAccounts.set(3, null);
        editAccounts.set(3, false);
    }

    @Override
    public void deleteLinked() {
        mediaAccounts.set(4, null);
        editAccounts.set(4, false);
    }

    @Override
    public void deleteYouTube() {
        mediaAccounts.set(5, null);
        editAccounts.set(5, false);
    }

    @Override
    public void connectFaceBook() {
        editAccounts.set(0, true);
        mediaAccounts.set(0, "");
    }

    @Override
    public void connectInsta() {
        editAccounts.set(1, true);
        mediaAccounts.set(1, "");
    }

    @Override
    public void connectTwitter() {
        editAccounts.set(2, true);
        mediaAccounts.set(2, "");
    }

    @Override
    public void connectSnap() {
        editAccounts.set(3, true);
        mediaAccounts.set(3, "");
    }

    @Override
    public void connectLinked() {
        editAccounts.set(4, true);
        mediaAccounts.set(4, "");
    }

    @Override
    public void connectYouTube() {
        editAccounts.set(5, true);
        mediaAccounts.set(5, "");
    }

    @Override
    public void back() {
        getView().getNavController().popBackStack();
    }

    @Override
    public void confirmChange() {
        reqUpdateSocial();
      /*  setErrors();
        if (editAccounts.get(0) && !Patterns.EMAIL_ADDRESS.matcher(mediaAccounts.get(0).trim()).matches()) {
            errors.set(0, "FaceBook Account Invalid ");
        } else if (editAccounts.get(1) && !Patterns.EMAIL_ADDRESS.matcher(mediaAccounts.get(1).trim()).matches()) {
            errors.set(1, "Instagram Account Invalid ");
        } else if (editAccounts.get(2) && !Patterns.EMAIL_ADDRESS.matcher(mediaAccounts.get(2).trim()).matches()) {
            errors.set(2, "Twitter Account Invalid ");
        } else if (editAccounts.get(3) && !Patterns.EMAIL_ADDRESS.matcher(mediaAccounts.get(3).trim()).matches()) {
            errors.set(3, "Snap Chat Account Invalid ");
        } else if (editAccounts.get(4) && !Patterns.EMAIL_ADDRESS.matcher(mediaAccounts.get(4).trim()).matches()) {
            errors.set(4, "LinkedIn Account Invalid ");
        } else if (editAccounts.get(5) && !Patterns.EMAIL_ADDRESS.matcher(mediaAccounts.get(5).trim()).matches()) {
            errors.set(5, "YouTube Account Invalid ");
        } else {
            reqUpdateSocial();
        }*/
    }

    @Override
    public void reqGetSocialMedia() {
        mProgressDialog = new ProgressDialog(getView().getFragment().getContext());
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("update Data ..... ");
        mProgressDialog.show();
        api.getSocialMedia(MyServiceInterceptor.getAuth(), MyServiceInterceptor.userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Request<>(getView().getFragment().getContext(), new RequestListener<ResponseApi<SocialMedia>>() {
                    @Override
                    public void onResponse(MutableLiveData<ResponseApi<SocialMedia>> data) {
                        data.observe(getView().getFragment(), e -> {
                            if (e == null || e.getData() == null) {
                                onEmpty("No Social Media Found");
                            } else {
                                mediaAccounts.set(0, e.getData().getFacebook());
                                mediaAccounts.set(1, e.getData().getInstagram());
                                mediaAccounts.set(2, e.getData().getTwitter());
                                mediaAccounts.set(3, e.getData().getWhatsapp());
                                mediaAccounts.set(4, e.getData().getLinkedin());
                                mediaAccounts.set(5, e.getData().getYoutube());
                            }
                        });
                        mProgressDialog.dismiss();

                    }

                    @Override
                    public void onEmpty(String msg) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getView().getFragment().getContext(), msg, Toast.LENGTH_SHORT).show();
                        getView().getFragment().getActivity().onBackPressed();
                    }

                    @Override
                    public void onError(String msg) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getView().getFragment().getContext(), msg, Toast.LENGTH_SHORT).show();
                        getView().getFragment().getActivity().onBackPressed();
                    }

                    @Override
                    public void onSessionExpired(String msg) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getView().getFragment().getContext(), msg, Toast.LENGTH_SHORT).show();
                        getView().getFragment().getActivity().onBackPressed();
                    }

                    @Override
                    public void onNetWorkError(String msg) {
                        mProgressDialog.dismiss();
                        Toast.makeText(getView().getFragment().getContext(), msg, Toast.LENGTH_SHORT).show();
                        getView().getFragment().getActivity().onBackPressed();
                    }
                }));
    }

    @Override
    public void reqUpdateSocial() {
        mProgressDialog = new ProgressDialog(getView().getFragment().getContext());
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("update Data ..... ");
        mProgressDialog.show();
        api.updateSocialMedia(MyServiceInterceptor.getAuth(), MyServiceInterceptor.userId, mediaAccounts.get(0), mediaAccounts.get(1), mediaAccounts.get(2)
                , mediaAccounts.get(3), mediaAccounts.get(4), mediaAccounts.get(5))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Request<>(getView().getFragment().getContext(), new RequestListener<SocialMediaResponse>() {
                    @Override
                    public void onResponse(MutableLiveData<SocialMediaResponse> data) {
                        data.observe(getView().getFragment(), e -> {
                            Toast.makeText(getView().getFragment().getContext(), e.getStatus(), Toast.LENGTH_SHORT).show();
                        });
                        mProgressDialog.dismiss();
                        getView().getFragment().getActivity().onBackPressed();
                    }

                    @Override
                    public void onEmpty(String msg) {
                        mProgressDialog.dismiss();
                        getView().getFragment().getActivity().onBackPressed();
                        Toast.makeText(getView().getFragment().getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String msg) {
                        mProgressDialog.dismiss();
                        getView().getFragment().getActivity().onBackPressed();
                        Toast.makeText(getView().getFragment().getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSessionExpired(String msg) {
                        mProgressDialog.dismiss();
                        getView().getFragment().getActivity().onBackPressed();
                        Toast.makeText(getView().getFragment().getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNetWorkError(String msg) {
                        mProgressDialog.dismiss();
                        getView().getFragment().getActivity().onBackPressed();
                        Toast.makeText(getView().getFragment().getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
