package com.circles.circlesapp.phase2.views.adapter;
/*
 *
 * Created by Kamal Khalaf on 8/29/2019.
 * Contact : kamal.khalaf56@gmail.com
 *
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.circles.circlesapp.R;
import com.circles.circlesapp.phase2.services.model.MessagePhase2;

import java.util.List;

public class MessagesAdapterPhase2 extends RecyclerView.Adapter<MessagesAdapterPhase2.ViewHolder> {


    private static final int INCOME = 1;
    private static final int OUTGOING = 2;

    private List<MessagePhase2> messageList;
    private Context context;

    public MessagesAdapterPhase2(Context context, List<MessagePhase2> models) {
        this.context = context;
        this.messageList = models;
    }

    ///////////////to identify the layout context like  activity oncreate or fragment
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater;
        View view = null;
        switch (viewType) {
            case INCOME:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.other_user_message_item2_phase2, parent, false);
                break;
            case OUTGOING:
                inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.other_user_message_item_outgoing_phase2, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (!this.messageList.get(position).isCurrentuser()) {
            return INCOME;
        } else {
            return OUTGOING;
        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        ViewHolder b = (ViewHolder) holder;

        if (!messageList.get(position).isOnline()) {
            b.isOnline.setVisibility(View.GONE);
            b.isOffline.setVisibility(View.VISIBLE);
        } else {
            b.isOnline.setVisibility(View.VISIBLE);
            b.isOffline.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View isOnline;
        private View isOffline;

        public ViewHolder(View v) {
            super(v);

            this.isOnline = (View) v.findViewById(R.id.isOnline);
            this.isOffline = (View) v.findViewById(R.id.isOffline);
        }
    }
}
