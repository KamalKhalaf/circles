package com.circles.circlesapp.home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.circles.circlesapp.R;
import com.circles.circlesapp.addgroup.MessageEvent;
import com.circles.circlesapp.databinding.AddNewCommentBinding;
import com.circles.circlesapp.databinding.ItemImageBinding;
import com.circles.circlesapp.helpers.ui.RecyclerAdapter;
import com.circles.circlesapp.helpers.ui.RecyclerCallback;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class AddTextCommentFragment extends DialogFragment implements RecyclerCallback<ItemImageBinding, String> {
    List<String> paths;
    RecyclerAdapter<String, ItemImageBinding> adapter;
    AddNewCommentBinding b;
    boolean isReply;
    private int id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isReply = getArguments().getBoolean("isReply");
            id = getArguments().getInt("id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.add_new_comment, container, false);
        paths = new ArrayList<>();
        b.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new RecyclerAdapter<>(getActivity(), paths, R.layout.item_image, this);
        b.recyclerView.setAdapter(adapter);

        b.createBtn.setOnClickListener(v -> {
            if (isReply) {
                AddReplyModel m = new AddReplyModel();
                m.commentId = id;
                EventBus.getDefault().postSticky(m);
                getActivity().onBackPressed();
                return;
            }
            MessageEvent event = new MessageEvent();
            event.setPostText(b.descEt.getText().toString());
            event.setImagePaths((ArrayList<String>) paths);
            event.setIsComment(true);
            event.setPostId(getArguments().getInt("id", 0));
            EventBus.getDefault().postSticky(event);
            getActivity().onBackPressed();
        });
        b.closeIcon.setOnClickListener(v -> {
            if (getActivity() != null)
                getActivity().onBackPressed();
        });
        b.attachImage.setOnClickListener(v -> {
            openImagePicker();
        });
        if (isReply) {
            b.cardView.setVisibility(View.GONE);
            b.pickUpCard.setVisibility(View.GONE);
            b.passwordCardView.setVisibility(View.GONE);
            b.titleTv.setText(R.string.add_new_reply);
            b.descEt.setHint(R.string.add_new_reply);
            b.createTv.setText(R.string.add_reply);
        } else {
            b.cardView.setVisibility(View.GONE);
            b.pickUpCard.setVisibility(View.GONE);
            b.passwordCardView.setVisibility(View.GONE);
            b.titleTv.setText(R.string.add_new_comment);
            b.descEt.setHint(R.string.add_new_comment);
            b.createTv.setText(R.string.add_comment);
        }

        return b.getRoot();
    }


    private void openImagePicker() {
//        new ImagePicker.Builder(getActivity())
//                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
//                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
//                .directory(ImagePicker.Directory.DEFAULT)
//                .extension(ImagePicker.Extension.PNG)
//                .allowMultipleImages(false)
//                .enableDebuggingMode(true)
//                .build();


        Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        intent1.putExtra(IS_NEED_FOLDER_LIST, true);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);


    }

    public static AddTextCommentFragment newInstance(int id, boolean isReply) {
        AddTextCommentFragment fragment = new AddTextCommentFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putBoolean("isReply", isReply);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
//            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
//            paths.addAll(mPaths);
//            adapter.notifyDataSetChanged();
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

                    paths.addAll(mPaths);
                    adapter.notifyDataSetChanged();

                    break;

                }

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
