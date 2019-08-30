package com.circles.circlesapp.phase2.views.adapter;
/*
 *
 * Created by Kamal Khalaf on 8/27/2019.
 * Contact : kamal.khalaf56@gmail.com
 *
 */

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.circles.circlesapp.phase2.services.ListOfGroupsObject;
import com.circles.circlesapp.R;
import com.circles.circlesapp.chatlist.ChatRoom;
import com.circles.circlesapp.databinding.ListItemGroupBinding;
import com.circles.circlesapp.helpers.core.Constants;
import com.circles.circlesapp.messaging.view.MessagingActivity;

import java.util.ArrayList;

public class ListOfGroupsAdapter extends RecyclerView.Adapter<ListOfGroupsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ListOfGroupsObject> items;


    public ListOfGroupsAdapter(Context context, ArrayList<ListOfGroupsObject> models) {
        this.context = context;
        this.items = models;
    }

    ///////////////to identify the layout context like  activity oncreate or fragment
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemGroupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_item_group, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {



        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatRoom chatRoom = new ChatRoom("kamal" , "web" , "room 20" , 20 , 12.22 , 15.55);
                Intent intent = new Intent(context, MessagingActivity.class);
                intent.putExtra(Constants.CHAT_ROOM_KEY, chatRoom);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_bottom);
        viewToAnimate.startAnimation(animation);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ListItemGroupBinding binding;

        ViewHolder(ListItemGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
