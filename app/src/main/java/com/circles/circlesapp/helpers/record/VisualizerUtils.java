package com.circles.circlesapp.helpers.record;
/**/

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.circles.circlesapp.helpers.MySeekBarCallback;

import static org.greenrobot.eventbus.EventBus.TAG;

public class VisualizerUtils {

//    private MediaPlayer player;
//    private Handler h;
//    private boolean wasPaused = false;
//    private String seekbarTag;
//    private MySeekBarCallback callback;
//    private Runnable runnable = this::updateVisuilizer;
//    private static VisualizerUtils inst;
//
//
//    public static VisualizerUtils newInstance(String seekBarTag, MySeekBarCallback callback) {
//        if (inst == null)
//            return inst = new VisualizerUtils(seekBarTag, callback);
//        else if (inst.seekbarTag.equals(seekBarTag)) {
//            return inst;
//        } else
//            inst.finishMediaPlayr();
//        return inst = new VisualizerUtils(seekBarTag, callback);
//    }
//
//    private VisualizerUtils(String seekBarTag, MySeekBarCallback callback) {
//        this.seekbarTag = seekBarTag;
//        this.callback = callback;
//        player = new MediaPlayer();
//        h = new Handler();
//        player.setOnCompletionListener(m -> {
//            finishMediaPlayr();
//        });
//    }
//
//    public boolean isPlayin() {
//        if (player == null) return false;
//        return player.isPlaying();
//    }
//
//    public void startmediaPlayer(String url) {
//        if (player == null) return;
//        if (player.isPlaying()) return;
//        try {
//            if (!wasPaused) {
//                player.setDataSource(url);
//                player.prepare();
//                player.start();
//                callback.callHearVoiceApi();
//                updateVisuilizer();
//            } else {
//                player.start();
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void pauseMediaPlayer() {
//
//        try {
//            player.pause();
//            wasPaused = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void finishMediaPlayr() {
//        try {
//            player.stop();
//            player.release();
//            player = null;
//            callback.finishSeekBar();
//        } catch (Exception s) {
//            Log.d(TAG, "finishMediaPlayr: " + s.getLocalizedMessage());
//        }
//
//    }
//
//    private void updateVisuilizer() {
//        if (player == null) return;
//        callback.updateSeekTv(milliSecondsToTimer(player.getCurrentPosition()));
//        callback.setSeekMax(player.getDuration());
//        callback.setprogress(player.getCurrentPosition());
//        h.postDelayed(runnable, 100);
//
//    }
//
//    private String milliSecondsToTimer(long milliseconds) {
//        String finalTimerString = "";
//        String secondsString;
//        // Convert total duration into time
//        int hours = (int) (milliseconds / (1000 * 60 * 60));
//        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
//        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
//        // Add hours if there
//        if (hours > 0) {
//            finalTimerString = hours + ":";
//        }
//        // Prepending 0 to seconds if it is one digit
//        if (seconds < 10) {
//            secondsString = "0" + seconds;
//        } else {
//            secondsString = "" + seconds;
//        }
//        finalTimerString = finalTimerString + minutes + ":" + secondsString;
//        // return timer string
//        return finalTimerString;
//    }
//
//    public void flush() {
//        inst = null;
//    }
}
