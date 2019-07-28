package com.circles.circlesapp.messaging.view.adapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import com.circles.circlesapp.messaging.model.Message;

public class MessagesBindingAdapter {

    @BindingAdapter("messages")
    public static void bindMessagesItems(RecyclerView view,
                                         List<Message> messageList) {

        /*MessagesAdapter adapter = new MessagesAdapter(messageList);
        view.setAdapter(adapter);*/
    }
}
