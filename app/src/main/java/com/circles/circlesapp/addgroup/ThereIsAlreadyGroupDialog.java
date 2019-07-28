package com.circles.circlesapp.addgroup;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.ExistedGroupDialogBinding;
import com.circles.circlesapp.group.GroupActivity;
import com.circles.circlesapp.nearby.GroupObjectRespModel;
import com.circles.circlesapp.nearby.JoinGroupReqBody;
import com.circles.circlesapp.nearby.NearbyViewModel;

public class ThereIsAlreadyGroupDialog extends Fragment {
    ExistedGroupDialogBinding b;

    public static ThereIsAlreadyGroupDialog newInstance(GroupObjectRespModel g) {

        Bundle args = new Bundle();
        args.putParcelable("groupData",g);
        ThereIsAlreadyGroupDialog fragment = new ThereIsAlreadyGroupDialog();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.existed_group_dialog, container, false);
       NearbyViewModel vm= ViewModelProviders.of(this).get(NearbyViewModel.class);
       if(getArguments()==null)getActivity().onBackPressed();
       if(getArguments().getParcelable("groupData")==null)getActivity().onBackPressed();
        GroupObjectRespModel groupObjectRespModel=getArguments().getParcelable("groupData");
        b.deleteImgView.setOnClickListener(v->{
            getActivity().onBackPressed();
        });
        b.peopleNum.setText(groupObjectRespModel.users_count+"People in this group");
        b.groupName.setText(groupObjectRespModel.name);
        Glide.with(this).asBitmap().load(groupObjectRespModel.cover).into(b.imageView);
        b.joinGroupCV.setOnClickListener(v->{
            b.joinGroupCV.setEnabled(false);
            JoinGroupReqBody body=new JoinGroupReqBody();
            body.longitude=groupObjectRespModel.longitude;
            body.latitude=groupObjectRespModel.latitude;
            body.id=groupObjectRespModel.id;
            body.groupName=groupObjectRespModel.name;
            vm.requestJoinGroup(body);
        });
        vm.joinGroupMutableLD.observe(this,it->{
            if (it != null) {
                b.joinGroupCV.setEnabled(true);
                if(it.channel!=null){
                    Intent intent =new Intent(getContext(), GroupActivity.class);
                    intent.putExtra("data",it);
                    startActivity(intent);
                }
            }
        });
        return b.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
