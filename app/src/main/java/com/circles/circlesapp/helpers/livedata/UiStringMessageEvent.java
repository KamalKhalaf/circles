package com.circles.circlesapp.helpers.livedata;

import android.arch.lifecycle.LifecycleOwner;


public class UiStringMessageEvent extends SingleLiveEvent<String> {

    public void observe(LifecycleOwner owner, final UiMessageObserver observer) {
        super.observe(owner, t -> {
            if (t == null) {
                return;
            }
            observer.onNewMessage(t);
        });
    }

    public interface UiMessageObserver {
        /**
         * Called when there is a new message to be shown.
         *
         * @param messageText The new message, non-null.
         */
        void onNewMessage(String messageText);
    }

}