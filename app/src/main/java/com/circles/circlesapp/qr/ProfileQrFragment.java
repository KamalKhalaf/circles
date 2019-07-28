package com.circles.circlesapp.qr;



import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.Display;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.ProfileQrLayoutBinding;
import com.circles.circlesapp.helpers.SharedPrefHelper;
import com.circles.circlesapp.settings.callBacks.QrCallBack;
import com.circles.circlesapp.settings.ui.BaseDailogFragment;
import com.circles.circlesapp.settings.viewModels.qr.QrViewModel;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGSaver;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class ProfileQrFragment extends BaseDailogFragment implements QrCallBack {
    public static final String USERNAME = "userName";
    private static final String USERIMAGE = "userImage";
    private ProfileQrLayoutBinding layoutBinding;
    private QrViewModel viewModel;
    private String username, userImage;
    private SharedPrefHelper prefHelper;
    private int userId;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    private String[] params = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int code = 123;

    public static ProfileQrFragment getInstance(String userName, String imageUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(USERNAME, userName);
        bundle.putString(USERIMAGE, imageUrl);
        ProfileQrFragment fragment = new ProfileQrFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int setContentView() {
        return R.layout.profile_qr_layout;
    }

    @Override
    public void iniViews() {
        chaeckPermission();
        userImage = getArguments().getString(USERIMAGE);
        username = getArguments().getString(USERNAME);
        prefHelper = new SharedPrefHelper(getActivity());
        userId = prefHelper.getUserId();
        layoutBinding = DataBindingUtil.bind(view);
        viewModel = ViewModelProviders.of(this).get(QrViewModel.class);
        viewModel.attachView(this);
        layoutBinding.setQrScan(viewModel);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        String strUserId = String.valueOf(userId);
        Bitmap bitmap = viewModel.generateQrCode(strUserId ,username, size.x, size.y);

        try {
            QRGSaver.save(savePath, strUserId + " " + username, bitmap, QRGContents.ImageType.IMAGE_JPEG);
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewModel.setUserName(username);
        viewModel.setProfile_image(userImage);
        layoutBinding.qrImg.setImageBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(code)
    private void chaeckPermission() {
        if (EasyPermissions.hasPermissions(getContext(), params)) {

        } else {
            EasyPermissions.requestPermissions(this, "please allow permission", code, params);
        }
    }

    @Override
    public void closeFragment() {
        dismiss();
    }

    public ProfileQrFragment getFragment() {
        return this;
    }
}
