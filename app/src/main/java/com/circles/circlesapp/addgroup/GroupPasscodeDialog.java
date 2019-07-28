package com.circles.circlesapp.addgroup;

import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.PasscodeGroupDialogBinding;
import com.circles.circlesapp.helpers.livedata.Resource;
import com.circles.circlesapp.nearby.JoinGroupReqBody;
import com.circles.circlesapp.nearby.JoinGroupResponse;
import com.circles.circlesapp.nearby.NearbyRepo;
import com.circles.circlesapp.retrofit.responses.GenericResponse;

public class GroupPasscodeDialog extends Fragment {
    PasscodeGroupDialogBinding b;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.passcode_group_dialog, container, false);
        b.deleteImgView.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        b.confirmPassCode.setOnClickListener(v -> {
            MessageEvent event = new MessageEvent();
            String passcode = "";
            passcode += b.passcode1.getText().toString();
            passcode += b.passcode2.getText().toString();
            passcode += b.passcode3.getText().toString();
            passcode += b.passcode4.getText().toString();
            JoinGroupReqBody code = new JoinGroupReqBody();
            if (getArguments() != null) {
                code = getArguments().getParcelable("req_body");
            }
            if (passcode.equals("" + code.password)) {
                new NearbyRepo().RequestJoinGroup(code).observe(this, getJoiningObserver());
            } else {
                b.incorrectPasss.setVisibility(View.VISIBLE);
            }
        });

        return b.getRoot();
    }

    private Observer<Resource<GenericResponse<JoinGroupResponse>>> getJoiningObserver() {
        return resp -> {
            if (resp != null && resp.getStatus() == Resource.Status.SUCCESS) {
                JoinGroupResponse joinGroupResponse = resp.getData().data;
                EventBus.getDefault().postSticky(joinGroupResponse);
            }
        };
    }

    public static GroupPasscodeDialog newInstance(JoinGroupReqBody req_body) {

        Bundle args = new Bundle();
        args.putParcelable("req_body", req_body);
        GroupPasscodeDialog fragment = new GroupPasscodeDialog();
        fragment.setArguments(args);
        return fragment;
    }
}
