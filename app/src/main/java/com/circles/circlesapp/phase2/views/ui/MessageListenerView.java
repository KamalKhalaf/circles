package com.circles.circlesapp.phase2.views.ui;
/*
 *
 * Created by Kamal Khalaf on 8/28/2019.
 * Contact : kamal.khalaf56@gmail.com
 *
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.circles.circlesapp.R;

public class MessageListenerView extends FrameLayout {

    private View imageViewSend;
    private View imageViewAttachment, ivVote;
    private EditText editTextMessage;


    public MessageListenerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public MessageListenerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MessageListenerView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.message_body_layout, null);
        addView(view);

        imageViewAttachment = view.findViewById(R.id.ivAttachment);
        ivVote = view.findViewById(R.id.ivVote);
        editTextMessage = view.findViewById(R.id.editTextMessage);

        imageViewSend = view.findViewById(R.id.imageViewSend);
    }

    public View getSendView() {
        return imageViewSend;
    }

    public View getAttachmentView() {
        return imageViewAttachment;
    }

    public View getVoteView() {
        return ivVote;
    }

    public EditText getMessageView() {
        return editTextMessage;
    }

}
