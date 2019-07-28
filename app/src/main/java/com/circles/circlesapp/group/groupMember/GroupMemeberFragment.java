package com.circles.circlesapp.group.groupMember;



import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nkzawa.socketio.client.Socket;

import java.util.ArrayList;

import com.circles.circlesapp.R;
import com.circles.circlesapp.databinding.GroupMemberLayoutBinding;
import com.circles.circlesapp.messaging.SingletonSocket;

public class GroupMemeberFragment extends Fragment implements GroupMemeberAdapter.GroupMemeberListener {
    private GroupMemberLayoutBinding layoutBinding;
    private GroupMemeberAdapter adapter;
    String description;
    String roomName;

    Socket socket;

    {
        socket = SingletonSocket.newInstance();
    }

    public static GroupMemeberFragment getInstance(String description,String roomName) {
        Bundle bundle = new Bundle();
        bundle.putString("description", description);
        bundle.putString("roomName", roomName);
        GroupMemeberFragment fragment = new GroupMemeberFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutBinding = DataBindingUtil.inflate(inflater, R.layout.group_member_layout, container, false);
        if (getArguments() != null) {
            description = getArguments().getString("description");
            roomName = getArguments().getString("roomName");
        }
        setUpAdapter();
        loadMemebers(getMemberItems());
        layoutBinding.textView8.setText(description);

        return layoutBinding.getRoot();
    }

    private void setUpAdapter() {
        adapter = new GroupMemeberAdapter();
        adapter.setListener(this);
        layoutBinding.membersRecyclerView.setAdapter(adapter);
    }

    public void loadMemebers(ArrayList<MemberItem> memberItems) {
        adapter.setData(memberItems);
    }

    private ArrayList<MemberItem> getMemberItems() {
      return (ArrayList<MemberItem>) MemberItemList.getInst().getList();
    }

    @Override
    public void onClick(MemberItem item, int pos) {
    }
}
