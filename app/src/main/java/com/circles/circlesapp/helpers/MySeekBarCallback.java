package com.circles.circlesapp.helpers;

public interface MySeekBarCallback {
    void setSeekMax(int max);

    void setprogress(int playerCurrentPosition);

    void updateSeekTv(String currentSec);
//    void updateSeekTv(String duration ,String currentSec);

    void finishSeekBar();

    void callHearVoiceApi();
}
