package com.circles.circlesapp.messaging.view;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.FragmentPollBinding;
import com.circles.circlesapp.messaging.model.SendMessageBody;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.VideoPickActivity.IS_NEED_CAMERA;

/**
 * A simple {@link Fragment} subclass.
 */
public class PollFragment extends Fragment {

    FragmentPollBinding b;
    private boolean firstImageSelected=false;
    private File firstFile,secondFile;

    public PollFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_poll, container, false);
        b.closeIcon.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        b.attachImage.setOnClickListener(v -> {
            openImagePicker();
            firstImageSelected=true;
        });
        b.attachImage2.setOnClickListener(v -> {
            openImagePicker();
            firstImageSelected=false;

        });

        b.createBtn.setOnClickListener(view -> {
            SendMessageBody event=new SendMessageBody();
            List<File> fileList=new ArrayList<>();
            fileList.add(firstFile);
            fileList.add(secondFile);
            event.setFileList(fileList);
            event.setMessageBody(b.descEt.getText().toString());
            EventBus.getDefault().postSticky(event);
            try {
              getActivity().onBackPressed();
            }catch (Exception d){

            }

        });
        return b.getRoot();
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

    public static PollFragment newInstance() {

        Bundle args = new Bundle();

        PollFragment fragment = new PollFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
//            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
//            if (mPaths.size() > 0) {
//               if(firstImageSelected){
//                   firstFile=new File(mPaths.get(0));
//                   b.imagePoll1.setVisibility(View.VISIBLE);
//                   Glide.with(this).asBitmap().load(mPaths.get(0)).into( b.imagePoll1);
//               }else {
//                   secondFile=new File(mPaths.get(0));
//                   b.imagePoll2.setVisibility(View.VISIBLE);
//                   Glide.with(this).asBitmap().load(mPaths.get(0)).into( b.imagePoll2);
//               }
//            }
//        }


        switch (requestCode) {

            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    String path = null;
                    List<String> mPaths = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        mPaths.add(list.get(i).getPath());
                    }

                    if (mPaths.size() > 0) {
                        if(firstImageSelected){
                            firstFile=new File(mPaths.get(0));
                            b.imagePoll1.setVisibility(View.VISIBLE);
                            Glide.with(this).asBitmap().load(mPaths.get(0)).into( b.imagePoll1);
                        }else {
                            secondFile=new File(mPaths.get(0));
                            b.imagePoll2.setVisibility(View.VISIBLE);
                            Glide.with(this).asBitmap().load(mPaths.get(0)).into( b.imagePoll2);
                        }
                    }

                    break;

                }

        }
    }
}
