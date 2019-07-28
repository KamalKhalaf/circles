package com.circles.circlesapp.helpers;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.SeekBar;

import com.chibde.visualizer.LineBarVisualizer;
import com.circles.circlesapp.R;

import static org.greenrobot.eventbus.EventBus.TAG;

public class SeekBarUtil implements SeekBar.OnSeekBarChangeListener {
    private MediaPlayer player;
    private Handler h;
    private boolean wasPaused = false;
    private String seekbarTag;
    private MySeekBarCallback callback;
    private Runnable runnable = this::updateSeekBar;
    private static SeekBarUtil inst;

    public static SeekBarUtil newInstance(String seekBarTag, MySeekBarCallback callback) {
        if (inst == null)
            return inst = new SeekBarUtil(seekBarTag, callback);
        else if (inst.seekbarTag.equals(seekBarTag)) {
            return inst;
        } else
            inst.finishMediaPlayr();
        return inst = new SeekBarUtil(seekBarTag, callback);
    }

    private SeekBarUtil(String seekBarTag, MySeekBarCallback callback) {
        this.seekbarTag = seekBarTag;
        this.callback = callback;
        player = new MediaPlayer();
        h = new Handler();
        player.setOnCompletionListener(m -> {
          finishMediaPlayr();
        });
    }

    public boolean isPlayin() {
        if (player == null) return false;
        return player.isPlaying();
    }

    public void startmediaPlayer(String url) {
        if (player == null) return;
        if (player.isPlaying()) return;
        try {
            if (!wasPaused) {
                player.setDataSource(url);
                player.prepare();
                player.start();
                callback.callHearVoiceApi();
                updateSeekBar();
            } else {
                player.start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void startmediaPlayer(String url, LineBarVisualizer visualizer) {
        Log.d(TAG, "startmediaPlayer: "+"1");

        if (player == null) return;
        if (player.isPlaying()) return;
        try {
            if (!wasPaused) {
                Log.d(TAG, "startmediaPlayer: "+"player. not wasPaused");
                player.setDataSource(url);
                visualizer.setPlayer(player.getAudioSessionId());
                player.prepare();
                player.start();
                callback.callHearVoiceApi();
                updateSeekBar();

                player.setOnCompletionListener(m -> {
                    finishMediaPlayr();
                });

            } else {
                Log.d(TAG, "startmediaPlayer: "+"player. wasPaused");
                player.start();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "startmediaPlayer: "+"player. Exception");
        }
    }


    public void pauseMediaPlayer() {

        try {
            player.pause();
            Log.d(TAG, "pauseMediaPlayer: "+"player.pause");
            wasPaused = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "pauseMediaPlayer: "+"Exception");
        }
    }

    public void finishMediaPlayr() {
        try {
            player.stop();
            player.release();


            Log.d(TAG, "finishMediaPlayr: "+"player.release");

            player = null;
            callback.finishSeekBar();
        } catch (Exception s) {
            Log.d(TAG, "finishMediaPlayr: "+s.getLocalizedMessage());
        }

    }

    private void updateSeekBar() {
        if (player == null) return;
        callback.updateSeekTv(milliSecondsToTimer(player.getCurrentPosition()));
        callback.setSeekMax(player.getDuration());
        callback.setprogress(player.getCurrentPosition());
        h.postDelayed(runnable, 100);

    }

    private String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString;
        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }
        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        finalTimerString = finalTimerString + minutes + ":" + secondsString;
        // return timer string
        return finalTimerString;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (b && player.isPlaying()) {
            player.seekTo(i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void flush() {
        inst=null;
    }
}
