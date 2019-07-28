package com.circles.circlesapp.settings.ui;

/**/

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.EditProfileLayoutBinding;
import com.circles.circlesapp.helpers.SharedPrefHelper;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.profile.model.UpdatedProfileDetails;
import com.circles.circlesapp.settings.UserModel;
import com.circles.circlesapp.settings.callBacks.EditProfileCallBack;
import com.circles.circlesapp.settings.viewModels.editProfile.EditProfileViewModel;
import com.mukesh.countrypicker.CountryPicker;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;
import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.VideoPickActivity.IS_NEED_CAMERA;

public class EditProfileFragment extends Fragment implements EditProfileCallBack {
    public static final String USERMODEL = "UserModel";
    public EditProfileLayoutBinding layoutBinding;
    private UserModel mUserModel;
    private EditProfileViewModel viewModel;
    private NavController mNavController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = DataBindingUtil.inflate(inflater, R.layout.edit_profile_layout, container, false);
        mUserModel = getArguments().getParcelable(USERMODEL);
        return layoutBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(EditProfileViewModel.class);
        viewModel.attachView(this);
        viewModel.setUserModel(mUserModel);
        viewModel.reqUserData(MyServiceInterceptor.getAuth());
        layoutBinding.setEditProfile(viewModel);
        layoutBinding.inputCountry.setOnClickListener(v -> {
            if (getActivity() == null) return;
            CountryPicker.Builder builder = new CountryPicker.Builder();
            builder.with(getContext()).listener(country -> {
                layoutBinding.inputCountry.setText(country.getName());
                layoutBinding.inputPhone.setPrefixText(country.getDialCode());
            }).build().showDialog(getActivity().getSupportFragmentManager());

        });

        layoutBinding.inputDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 75) {
                    layoutBinding.layoutMaxiumCharecter.setVisibility(View.VISIBLE);
                } else {
                    layoutBinding.layoutMaxiumCharecter.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNavController = Navigation.findNavController(getActivity(), R.id.settings_nav_graph);
    }

    @Override
    public EditProfileFragment getFragment() {
        return this;
    }

    @Override
    public NavController getNavController() {
        return mNavController;
    }

    @Override
    public void changeImage() {
        openImagePicker();
    }


    public void updateDataView(UpdatedProfileDetails r) {


        SharedPrefHelper helper = new SharedPrefHelper(getActivity());

        if (r.getData().getProfile_image() != null) {
            helper.setProfileImage(r.getData().getProfile_image());
        }

    }

    private void openImagePicker() {
//        new ImagePicker.Builder(getActivity())
//                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
//                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
//                .directory(ImagePicker.Directory.DEFAULT)
//                .extension(ImagePicker.Extension.PNG)
//                .allowMultipleImages(false)
//                .build();

        Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        intent1.putExtra(IS_NEED_FOLDER_LIST, true);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
//            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
//            viewModel.setmFile(new File(mPaths.get(0)));
//            Glide.with(getContext()).asBitmap().load(mPaths.get(0)).into(layoutBinding.circleImageView3);
//
//
//        }

        switch (requestCode) {

            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    List<String> mPaths = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        mPaths.add(list.get(i).getPath());
                    }

                    viewModel.setmFile(new File(mPaths.get(0)));
                    Glide.with(getContext()).asBitmap().load(mPaths.get(0)).into(layoutBinding.circleImageView3);

                    break;

                }

        }
    }
}
