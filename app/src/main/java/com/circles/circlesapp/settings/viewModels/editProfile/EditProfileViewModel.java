package com.circles.circlesapp.settings.viewModels.editProfile;

/*
 * Created By mabrouk on 29/01/19
 * com.circles
 */

import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.circles.circlesapp.BuildConfig;
import com.circles.circlesapp.R;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.profile.model.UpdatedProfileDetails;
import com.circles.circlesapp.retrofit.Api;
import com.circles.circlesapp.retrofit.RetrofitClient;
import com.circles.circlesapp.retrofit.api.Request;
import com.circles.circlesapp.retrofit.api.RequestListener;
import com.circles.circlesapp.settings.UserModel;
import com.circles.circlesapp.settings.callBacks.EditProfileCallBack;
import com.circles.circlesapp.settings.ui.DatePickerFragment;
import com.circles.circlesapp.settings.viewModels.base.BaseViewModel;
import com.circles.circlesapp.vaildation.VaildationUserInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EditProfileViewModel<v extends EditProfileCallBack> extends BaseViewModel<v> implements EditProfileVmodel<v> {
    private ObservableField<UserModel> userModel = new ObservableField<>();
    private ObservableList<String> errors = new ObservableArrayList<>();
    private ObservableBoolean passwordShow = new ObservableBoolean();
    private ObservableBoolean confirmPasswordShow = new ObservableBoolean();
    private VaildationUserInfo vaildationUserInfo;
    private ObservableBoolean gender = new ObservableBoolean(true);
    private File mFile;
    private Api api;
    private ProgressDialog mProgressDialog;

    public EditProfileViewModel(@NonNull Application application) {
        super(application);
        setUpErrors();
        api = RetrofitClient.getInstance().getApi();
        vaildationUserInfo = new VaildationUserInfo();

    }

    private void setUpErrors() {
        errors.add(0, null);//firstName
        errors.add(1, null);//userName
        errors.add(2, null);//email
        errors.add(3, null);//country
        errors.add(4, null);//city
        errors.add(5, null);//password
        errors.add(6, null);//confirmPassword
        errors.add(7, null);//phone
        errors.add(8, null);//phone
    }


    public ObservableBoolean getGender() {
        return gender;
    }

    public void setmFile(File mFile) {
        this.mFile = mFile;
    }

    public ObservableBoolean getPasswordShow() {
        return passwordShow;
    }

    public ObservableBoolean getConfirmPasswordShow() {
        return confirmPasswordShow;
    }

    public ObservableList<String> getErrors() {
        return errors;
    }

    public ObservableField<UserModel> getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel.set(userModel);
    }

    /******************* adapter & watchers *************/
    @BindingAdapter({"profileWatcher", "profileError"})
    public static void profileWatcher(EditText editText, TextWatcher watcher, String error) {
        editText.addTextChangedListener(watcher);
        editText.setError(error);
    }

    @BindingAdapter("genderLisenter")
    public static void genderLisenter(RadioButton radioButton, CompoundButton.OnCheckedChangeListener listener) {
        radioButton.setOnCheckedChangeListener(listener);
    }

    @BindingAdapter("passwordShow")
    public static void editTextType1(EditText editText, boolean passwordShow) {
        if (passwordShow) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    @BindingAdapter("confirmPasswordShow")
    public static void editTextType2(EditText editText, boolean confirmPassword) {
        if (confirmPassword) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    @BindingAdapter("showPassword")
    public static void showPassword(CheckBox checkBox, CompoundButton.OnCheckedChangeListener listener) {
        checkBox.setOnCheckedChangeListener(listener);
    }

    public CompoundButton.OnCheckedChangeListener passwordListener = ((buttonView, isChecked) ->
            passwordShow.set(isChecked)
    );

    public CompoundButton.OnCheckedChangeListener confirmPasswordListener = ((buttonView, isChecked) ->
            confirmPasswordShow.set(isChecked)
    );

    public CompoundButton.OnCheckedChangeListener maleListener = (buttonView, isChecked) -> {
        gender.set(isChecked);
        userModel.get().setGender(isChecked ? 1 : 0);
    };

    public CompoundButton.OnCheckedChangeListener femaleListener = (buttonView, isChecked) -> {
        gender.set(!isChecked);
        userModel.get().setGender(isChecked ? 0 : 1);
    };


    public TextWatcher firstNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userModel.get().setFirst_name(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher userNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userModel.get().setUsername(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
//
    public TextWatcher descriptionWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userModel.get().setDescription(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userModel.get().setEmail(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    public TextWatcher phoneWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userModel.get().setPhone(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher countryWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userModel.get().setCountry(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher cityWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userModel.get().setCity(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userModel.get().setPassword(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public TextWatcher confirmPasswordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userModel.get().setPassword_confirmation(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void submit(View view) {
        setUpErrors();
        if (!vaildationUserInfo.isNameValid(userModel.get().getFirst_name().trim())) {
            errors.set(0, "Invalid Name");
        } else if (TextUtils.isEmpty(userModel.get().getUsername().trim())) {
            errors.set(1, "Invalid User Name");
        }else if (TextUtils.isEmpty(userModel.get().getDescription().trim())) {
            errors.set(8, "at least,you should short description");
        }  else if (!vaildationUserInfo.isEmailValid(userModel.get().getEmail().trim())) {
            errors.set(2, "Invalid Email");
        } else if (!vaildationUserInfo.isNameValid(userModel.get().getCountry())) {
            errors.set(3, "Invalid Country Name");
        } else if (!vaildationUserInfo.isNameValid(userModel.get().getCity())) {
            errors.set(4, "Invalid City Name");
        } else if (userModel.get().getPhone().length() < 8) {
            errors.set(7, "Invalid phone number");
        } else if (userModel.get().getPassword().length() < 8) {
            errors.set(5, "Password at least 8 digits");
        } else if (!userModel.get().getPassword().equals(userModel.get().getPassword_confirmation())) {
            Toast.makeText(getView().getFragment().getContext(), "Passwords Not Matches", Toast.LENGTH_SHORT).show();
        } else {
            try {
                reqUpdateData(userModel.get());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void back(View view) {
        getView().getNavController().popBackStack();
    }

    @Override
    public void male(View view) {
        gender.set(true);
        userModel.get().setGender(1);
    }

    @Override
    public void female(View view) {
        gender.set(false);
        userModel.get().setGender(0);
    }

    @Override
    public void chooseDate(View view) {
        DatePickerFragment datePickerFragment = DatePickerFragment.NewInstence("Birth Date", true, userModel.get().getBirthdate(), (date -> {
            userModel.get().setBirthdate(date);

        }));
        datePickerFragment.show(getView().getFragment().getChildFragmentManager(), "DatePickerFragment");
    }

    @Override
    public void editIMG(View view) {
        getView().changeImage();
    }

    @Override
    public void reqUserData(String auth) {
        mProgressDialog = new ProgressDialog(getView().getFragment().getContext());
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("Load Data ..... ");
        mProgressDialog.show();
        api.getUserData(auth)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(userModelResponseApi -> userModelResponseApi.getData())
                .subscribe(new Request<>(getView().getFragment().getContext(), new RequestListener<UserModel>() {
                    @Override
                    public void onResponse(MutableLiveData<UserModel> data) {
                        data.observe(getView().getFragment(), e -> {
                            if (e != null) {
                                mProgressDialog.dismiss();
                                userModel.set(e);
                                gender.set(e.getGender() == 1);
                                Log.d("onResponse", e.toString());
                            }
                        });
                    }

                    @Override
                    public void onEmpty(String msg) {
                        mProgressDialog.dismiss();
                        Log.d("onResponse", msg);
                    }

                    @Override
                    public void onError(String msg) {
                        mProgressDialog.dismiss();
                        Log.d("onResponse", msg);
                    }

                    @Override
                    public void onSessionExpired(String msg) {
                        mProgressDialog.dismiss();
                        Log.d("onResponse", msg);
                    }

                    @Override
                    public void onNetWorkError(String msg) {
                        mProgressDialog.dismiss();
                        Log.d("onResponse", msg);
                    }
                }));
    }

    @Override
    public void reqUpdateData(UserModel model) throws ParseException {
        mProgressDialog = new ProgressDialog(getView().getFragment().getContext());
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage("update Data ..... ");
        mProgressDialog.show();
        Date birthdate = model.getBirthdate();
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String bDate = s.format(birthdate);
        AndroidNetworking.upload(RetrofitClient.BASE_URL + "updateProfile")
                .addHeaders("Authorization", MyServiceInterceptor.getAuth())
                .addHeaders("Accept", "application/json")
                .addMultipartParameter("user_id", MyServiceInterceptor.userId + "")
                .addMultipartParameter("first_name", model.getFirst_name())
                .addMultipartParameter("last_name", model.getLast_name())
                .addMultipartParameter("description", model.getDescription())
                .addMultipartParameter("email", model.getEmail())
                .addMultipartParameter("phone", model.getPhone())
                .addMultipartParameter("city", model.getCity())
                .addMultipartParameter("country", model.getCountry())
                .addMultipartParameter("birthdate", getView().getFragment().layoutBinding.inputBirth.getText().toString())
                .addMultipartParameter("username", model.getUsername())
                .addMultipartParameter("gender", model.getGender() + "")
                .addMultipartParameter("password", model.getPassword())
                .addMultipartParameter("password_confirmation", model.getPassword_confirmation())
                .addMultipartFile("profile_image", mFile)
                .build().getAsObject(UpdatedProfileDetails.class, new ParsedRequestListener() {
            @Override
            public void onResponse(Object response) {
                if (getView().getFragment().getActivity() != null) {

                    UpdatedProfileDetails r = (UpdatedProfileDetails) response;
                    Log.d("onResponse", "onResponse: " + response);
                    getView().getFragment().updateDataView(r);
                    if (r.getMessage().contains("Success")) {
                        Toast.makeText(getView().getFragment().getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getView().getFragment().getContext(), "Error, please try again later", Toast.LENGTH_SHORT).show();
                    }
                    mProgressDialog.dismiss();
                    getView().getFragment().getActivity().onBackPressed();
                }
            }

            @Override
            public void onError(ANError anError) {
                mProgressDialog.dismiss();
                String errorBody = anError.getErrorBody();
                Toast.makeText(getView().getFragment().getContext(), "Can not Update", Toast.LENGTH_SHORT).show();

            }
        });

    }


    public File bitmapToFile(Bitmap bitmap) {
        //create a file to write bitmap data
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = getView().getFragment().getString(R.string.app_name) + sdf.format(currentTime) + ".jpg";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName);
        try {
            file.createNewFile();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) e.printStackTrace();
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapData = bos.toByteArray();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) e.printStackTrace();
            return null;
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
