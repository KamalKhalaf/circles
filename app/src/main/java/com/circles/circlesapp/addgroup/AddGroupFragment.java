package com.circles.circlesapp.addgroup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.AddNewGroupBinding;
import com.circles.circlesapp.databinding.ItemImageBinding;
import com.circles.circlesapp.helpers.SharedPrefHelper;
import com.circles.circlesapp.helpers.core.Constants;
import com.circles.circlesapp.helpers.ui.RecyclerAdapter;
import com.circles.circlesapp.helpers.ui.RecyclerCallback;
import com.google.android.gms.maps.model.LatLng;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class AddGroupFragment extends Fragment implements Contractor.view, RecyclerCallback<ItemImageBinding, String> {
    AddGroupPresenter presenter;
    AddGroupBody body;
    ProgressDialog dialog;
    List<String> paths;
    RecyclerAdapter<String, ItemImageBinding> adapter;
    GroupTypes type;
    Bundle bundle;
    SharedPrefHelper helper;
    AddNewGroupBinding b;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.add_new_group, container, false);
        presenter = new AddGroupPresenter(this);
        paths = new ArrayList<>();
        helper = new SharedPrefHelper(getActivity());
        body = new AddGroupBody();
        if (getArguments() != null) {
            bundle = getArguments();
            LatLng groupLocation = bundle.getParcelable("groupLocation");
            type = (GroupTypes) bundle.getSerializable("type");
            switch (type) {
                case EVENT:
                    prepareEventLayout();
                    break;
                case PRIVATE_GROUP:
                    preparePrivateGroupLayout();
                    break;
                case PUBLIC_GROUP:
                    prepareGroupLayout();

            }
            body.setLocation(groupLocation);
        }
        b.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new RecyclerAdapter<>(getActivity(), paths, R.layout.item_image, this);
        b.recyclerView.setAdapter(adapter);

        b.createTv.setOnClickListener(v -> {
            body.setName(b.nameEt.getText().toString());
            body.setPassword(b.passwordEt.getText().toString());
            body.setDescriptiom(b.descEt.getText().toString());
            body.setFileListFronUri(paths);
            dialog = ProgressDialog.show(getContext(), "", getActivity().getString(R.string.please_wait));
            presenter.addNewGroup(body, type);
        });
        b.closeIcon.setOnClickListener(v -> {
            if (getActivity() != null)
                getActivity().onBackPressed();
        });
        b.attachImage.setOnClickListener(v -> {
            openImagePicker();
        });
        b.pickUpCard.setOnClickListener(v -> {
            if (getActivity() == null) return;
            Intent i = new Intent(getContext(), PickUpLocationActivity.class);
            startActivity(i);
        });
        return b.getRoot();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(MessageEvent pickedUpLocation) {
        body.setLocation(pickedUpLocation.getLatLng());
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> location = geocoder.getFromLocation(pickedUpLocation.getLatLng().latitude, pickedUpLocation.getLatLng().longitude, 1);
            if (location == null) return;
            b.pickUpLocation.setText(location.get(0).getAddressLine(0));
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void preparePrivateGroupLayout() {
        b.nameEt.setHint(R.string.group_name);
        b.createTv.setText(R.string.create_group);
        b.pickUpCard.setVisibility(View.GONE);
        b.passwordCardView.setVisibility(View.VISIBLE);
        b.attachTv.setText(R.string.attach_multiple_images);


    }

    private void prepareGroupLayout() {
        b.nameEt.setHint(R.string.group_name);
        b.createTv.setText(R.string.create_group);
        b.pickUpCard.setVisibility(View.GONE);
        b.passwordCardView.setVisibility(View.GONE);
        b.attachTv.setText(R.string.attach_a_cover_image);


        if (bundle.getBoolean("isFromProfile")) {
            if (helper.getData(Constants.user_full_name) != null && !helper.getData(Constants.user_full_name).equals("")) {
                b.nameEt.setText(helper.getData(Constants.user_full_name));

                b.descEt.setText(helper.getData(Constants.user_full_name) + "'s Celebrity");
            }
        }

    }

    private void prepareEventLayout() {
        b.nameEt.setHint(R.string.event_name);
        b.createTv.setText(R.string.create_event);
        b.attachTv.setText(R.string.attach_multiple_images);
        b.titleTv.setText(R.string.create_new_event);
        b.pickUpCard.setVisibility(View.VISIBLE);
        b.passwordCardView.setVisibility(View.VISIBLE);

    }

    private void openImagePicker() {
//        new ImagePicker.Builder(getActivity())
//                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
//                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
//                .directory(ImagePicker.Directory.DEFAULT)
//                .extension(ImagePicker.Extension.PNG)
//                .allowMultipleImages(type != GroupTypes.PUBLIC_GROUP)
//                .enableDebuggingMode(true)
//                .build();

        Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(IS_NEED_FOLDER_LIST, true);
        if (type != GroupTypes.PUBLIC_GROUP) {
            intent1.putExtra(Constant.MAX_NUMBER, 9);
        } else {
            intent1.putExtra(Constant.MAX_NUMBER, 1);
        }
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);

    }

    public static AddGroupFragment newInstance(LatLng groupLocation, GroupTypes type, boolean isFromProfile) {
        Bundle args = new Bundle();
        args.putParcelable("groupLocation", groupLocation);
        args.putBoolean("isFromProfile", isFromProfile);
        args.putSerializable("type", type);
        AddGroupFragment fragment = new AddGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onGroupAddedSuccessfully() {
        dialog.dismiss();
        Toast.makeText(getContext(), "Group Created", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void onFailed(String message) {
        dialog.dismiss();
        Toast.makeText(getContext(), message + "", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();

    }

    @Override
    public void showWebView(String url) {
        dialog.dismiss();
        Intent i = new Intent(getContext(), WebViewActivity.class);
        i.putExtra("url", url);
        startActivityForResult(i, 22);
        //WebViewActivity.start(getActivity(),url);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
//            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
//            paths.addAll(mPaths);
//            adapter.notifyDataSetChanged();
//            if (type == GroupTypes.EVENT || type == GroupTypes.EVENT) {
//                b.attachTv.setText(R.string.add_more_images);
//            }
        switch (requestCode) {

            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    String path = null;
                    List<String> mPaths = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        mPaths.add(list.get(i).getPath());
                    }

                    paths.addAll(mPaths);
                    adapter.notifyDataSetChanged();

                    if (type == GroupTypes.EVENT || type == GroupTypes.EVENT) {
                        b.attachTv.setText(R.string.add_more_images);
                    }

                    break;

                }

            case 22:

                if (resultCode == RESULT_OK) {          //payment request code

                    if (data != null) {
                        if (data.getBooleanExtra("success", false)) {
                            Toast.makeText(getActivity(), "created successfully", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getActivity(), "failed to create ", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                break;
        }
    }

    @Override
    public void bindData(ItemImageBinding binding, String path, int position) {
        Glide.with(this)
                .asBitmap()
                .load(path)
                .into(binding.imageView);
        binding.deleteIcon.setOnClickListener(v -> {
            paths.remove(position);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onItemClicked(ItemImageBinding binding, String model, int position) {

    }
}
