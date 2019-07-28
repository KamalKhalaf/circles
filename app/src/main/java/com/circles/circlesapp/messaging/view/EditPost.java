package com.circles.circlesapp.messaging.view;


import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.circles.circlesapp.R;
import com.circles.circlesapp.addgroup.MessageEvent;
import com.circles.circlesapp.databinding.FragmentEditPostBinding;
import com.circles.circlesapp.helpers.utilities.PermissionUtils;
import com.circles.circlesapp.home.NewsFeedData;
import com.google.gson.Gson;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.VideoPickActivity.IS_NEED_CAMERA;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPost extends Fragment {

    FragmentEditPostBinding mBinding;
    List<String> paths;
    private long timeWhenStopped = 0;
    private NewsFeedData newsFeedData;
    Gson gson;

    public EditPost() {
        // Required empty public constructor
    }

    public static EditPost editPostInstance(String data) {
        EditPost fragment = new EditPost();
        Bundle bundle = new Bundle();
        bundle.putString("postData", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_post, container, false);
        mBinding.setLifecycleOwner(this);
        gson = new Gson();


        if (getArguments() != null) {
            if (getArguments().getString("postData") != null) {
                newsFeedData = gson.fromJson(getArguments().getString("postData"), NewsFeedData.class);
            }
        }


        mBinding.ibCloseDialog.setOnClickListener(view -> {
            FragmentActivity activity = getActivity();
            if (activity == null) return;
            activity.onBackPressed();
        });
        mBinding.btnAttachImage.setOnClickListener(v -> {
            openImagePicker();
        });


        if(newsFeedData != null){
            updateView();
        }

        mBinding.btnUpdatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (newsFeedData != null && newsFeedData.getId() != 0) {
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setUpdatePost(true);
                    if(mBinding.text.getText().toString().isEmpty()){
                        messageEvent.setText(newsFeedData.getText());
                    }else {
                        messageEvent.setText(mBinding.text.getText().toString());
                    }
                    if(paths != null){
                        messageEvent.setImage((ArrayList<String>) paths);
                    }
                    messageEvent.setId(newsFeedData.getId());
                    EventBus.getDefault().post(messageEvent);
                    getActivity().onBackPressed();

                }
            }
        });

        return mBinding.getRoot();
    }

    private void updateView() {

        mBinding.text.setText(newsFeedData.getText());
        Glide.with(this)
                .asBitmap()
                .load(newsFeedData.getImage())
                .into(mBinding.postImage);
    }

    private void openImagePicker() {

        Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        intent1.putExtra(IS_NEED_FOLDER_LIST, true);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!PermissionUtils.hasPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || !PermissionUtils.hasPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            {
                String[] d = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(d, 1);
            }
        }
//        else {
//            prepareRecordingStatus();
//        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    String path = null;
                    List<String> mPaths = new ArrayList<>();
                    paths = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        mPaths.add(list.get(i).getPath());
                    }

                    if (paths == null) paths = new ArrayList<>();
                    paths.add(mPaths.get(list.size() - 1));
                    Glide.with(this)
                            .asBitmap()
                            .load(paths.get(0))
                            .into(mBinding.postImage);
                    mBinding.btnAttachImage.setText(R.string.change_image);


                    break;

                }
        }
    }

}
