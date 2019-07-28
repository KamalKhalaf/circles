package com.circles.circlesapp.messaging.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chibde.visualizer.LineBarVisualizer;
import com.circles.circlesapp.R;
import com.circles.circlesapp.addgroup.MessageEvent;
import com.circles.circlesapp.databinding.DialogFragmentRecordAudioBinding;
import com.circles.circlesapp.databinding.ItemImageBinding;
import com.circles.circlesapp.helpers.Utility;
import com.circles.circlesapp.helpers.record.RecorderVisualizerView;
import com.circles.circlesapp.helpers.ui.RecyclerAdapter;
import com.circles.circlesapp.helpers.ui.RecyclerCallback;
import com.circles.circlesapp.helpers.utilities.PermissionUtils;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.AudioPickActivity;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.AudioFile;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import eu.gsottbauer.equalizerview.EqualizerView;

import static android.app.Activity.RESULT_OK;
import static com.vincent.filepicker.activity.AudioPickActivity.IS_NEED_RECORDER;
import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.VideoPickActivity.IS_NEED_CAMERA;

public class RecordAudioDialogFragment extends Fragment implements RecyclerCallback<ItemImageBinding, String> {
    DialogFragmentRecordAudioBinding mBinding;
    private long timeWhenStopped = 0;
    public static final int REPEAT_INTERVAL = 40;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mediaPlayer;

    private File mOutputFile;
    boolean isSoundFile = false;
    private Handler handler;

    //    private Handler forUpdateVisulizerHandler;
    private static String FEEDS_INSTANCE_KEY = "feedsInstance";
    Runnable runnable = this::updateSeekBar;
    List<String> paths;
    RecyclerAdapter<String, ItemImageBinding> adapter;
    private boolean isRecording;
    private RecorderVisualizerView visualizerView;
    private boolean isSounfFileFounded = false;

    private LineBarVisualizer lineBarVisualizer;

    public enum UserBehaviour {
        CANCELING,
        LOCKING,
        NONE
    }

    //recordOnLayout

    private UserBehaviour userBehaviour = UserBehaviour.NONE;
    private float directionOffset, cancelOffset, lockOffset;
    private float dp = 0;

    private Handler handler2;
    private boolean isSoundRecording = false;
    private Animation animBlink;

    private int audioTotalTime;
    private TimerTask timerTask;
    private Timer audioTimer;
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("m:ss", Locale.getDefault());


    private float lastX, lastY;
    private float firstX, firstY;


    public RecordAudioDialogFragment() {
        // Required empty public constructor
    }


    public static RecordAudioDialogFragment newInstance() {
        RecordAudioDialogFragment fragment = new RecordAudioDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(FEEDS_INSTANCE_KEY, false);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RecordAudioDialogFragment newInstance(int postId) {
        RecordAudioDialogFragment fragment = new RecordAudioDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(FEEDS_INSTANCE_KEY, true);
        bundle.putInt("postId", postId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RecordAudioDialogFragment newFeedsInstance() {
        RecordAudioDialogFragment fragment = new RecordAudioDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(FEEDS_INSTANCE_KEY, true);
        fragment.setArguments(bundle);
        return fragment;
    }

    private EqualizerView equalizer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_record_audio, container, false);
        mBinding.setLifecycleOwner(this);

        visualizerView = (RecorderVisualizerView) mBinding.ivRecord;
        equalizer = mBinding.equalizer;

//        glSurfaceView = mBinding.glSurface;
//        mHorizon = new Horizon(glSurfaceView, getResources().getColor(R.color.gray),
//                RECORDER_SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_ENCODING_BIT);
//        mHorizon.setMaxVolumeDb(MAX_DECIBELS);

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new RecyclerAdapter<>(getActivity(), paths, R.layout.item_image, this);
        mBinding.recyclerView.setAdapter(adapter);
        if (getArguments() != null) {
            if (getArguments().getBoolean(FEEDS_INSTANCE_KEY) &&
                    getArguments().getInt("postId", 0) == 0) {
                ViewGroup.LayoutParams layoutParams = mBinding.rlContainer.getLayoutParams();
                mBinding.clAdditionalInserts.setVisibility(View.VISIBLE);
                mBinding.btnSubmitRecordedAudio.setText(getString(R.string.post_to_timeline));
            } else {
                mBinding.clAdditionalInserts.setMaxHeight(24);
                mBinding.clAdditionalInserts.setVisibility(View.INVISIBLE);
            }
        }
        mBinding.ibCloseDialog.setOnClickListener(view -> {
            FragmentActivity activity = getActivity();
            if (activity == null) return;
            activity.onBackPressed();
        });

        mBinding.clRecordAudio.setVisibility(View.GONE);
        mBinding.clPreviewRecordedAudio.setVisibility(View.VISIBLE);
        mBinding.btnSubmitRecordedAudio.setVisibility(View.VISIBLE);
        mBinding.ibPauseRecordedAudio.setVisibility(View.GONE);


//        mBinding.clPreviewRecordedAudio.setVisibility(View.GONE);
//        mBinding.btnSubmitRecordedAudio.setVisibility(View.GONE);
        mBinding.ibEndRecording.setOnClickListener(view -> onEndRecordingButtonClick());
        mBinding.btnSubmitRecordedAudio.setOnClickListener(v -> {
            if (isSounfFileFounded) {

                if (getArguments().getBoolean(FEEDS_INSTANCE_KEY)) {
                    MessageEvent messageEvent = new MessageEvent();

                    messageEvent.setPostText(mBinding.etComment.getText().toString());
                    if (isSoundFile) {
                        messageEvent.setRecordpath(mOutputFile.getAbsolutePath());
                    } else {
                        messageEvent.setRecordpath(getOutputFile().getAbsolutePath());
                    }
                    messageEvent.setImagePaths((ArrayList<String>) paths);
                    messageEvent.setPostId(getArguments().getInt("postId", 0));
                    messageEvent.setIsComment(getArguments().getInt("postId", 0) != 0);
                    EventBus.getDefault().post(messageEvent);
                    getActivity().onBackPressed();
                    return;
                }
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setRecordpath(getOutputFile().getAbsolutePath());
                EventBus.getDefault().post(messageEvent);
                getActivity().onBackPressed();

            } else {
                Snackbar.make(mBinding.getRoot(), getString(R.string.miss_record), Snackbar.LENGTH_LONG).setAction(getString(R.string.close), view -> {
                }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
                Utility.showShakeError(getActivity(), mBinding.imageViewAudio);
                return;
            }
        });
        mBinding.btnAttachImage.setOnClickListener(v -> {
            openImagePicker();
        });

        mBinding.btnAttachSound.setOnClickListener(v -> {
            openSoundPicker();
        });

//        mVisualizerView = mBinding.myvisualizerview;
        lineBarVisualizer = mBinding.visualizerrr;


        //recordOn Layout
        animBlink = AnimationUtils.loadAnimation(getContext(),
                R.anim.blink);

        handler2 = new Handler(Looper.getMainLooper());
        lineBarVisualizer.setColor(ContextCompat.getColor(getActivity(), R.color.av_dark_blue));
        lineBarVisualizer.setDensity(90f);

        mBinding.imageViewAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!isSoundRecording) {

                    cancelOffset = (float) (mBinding.imageViewAudio.getX() / 2.8);
                    lockOffset = (float) (mBinding.imageViewAudio.getX() / 2.5);


                    mBinding.imageViewAudio.animate().scaleXBy(1f).scaleYBy(1f).setDuration(200).setInterpolator(new OvershootInterpolator()).start();
                    mBinding.textViewTime.setVisibility(View.VISIBLE);
                    mBinding.textViewTime.startAnimation(animBlink);
                    if (audioTimer == null) {
                        audioTimer = new Timer();
                        timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                    }

                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            handler2.post(new Runnable() {
                                @Override
                                public void run() {
                                    mBinding.textViewTime.setText(timeFormatter.format(new Date(audioTotalTime * 1000)));
                                    audioTotalTime++;
                                }
                            });
                        }
                    };

                    audioTotalTime = 0;
                    audioTimer.schedule(timerTask, 0, 1000);
                    isSoundRecording = true;
                    if(mediaPlayer != null){

                        mediaPlayer.pause();
                        mBinding.ibPlayRecordedAudio.setVisibility(View.VISIBLE);
                        mBinding.ibPauseRecordedAudio.setVisibility(View.GONE);
                    }

                    if(mBinding.layoutPlayStopAudio.getVisibility() == View.VISIBLE){
                        mBinding.layoutPlayStopAudio.setVisibility(View.GONE);
                    }

                    startRecording();


                } else {

                    if (mRecorder == null) {
                        return;
                    } else {
                        firstX = 0;
                        firstY = 0;
                        lastX = 0;
                        lastY = 0;

                        userBehaviour = UserBehaviour.NONE;

                        mBinding.imageViewAudio.animate().scaleX(1f).scaleY(1f).translationX(0).translationY(0).setDuration(100).setInterpolator(new LinearInterpolator()).start();
                        mBinding.textViewTime.clearAnimation();
                        mBinding.textViewTime.setVisibility(View.INVISIBLE);
                        isSoundRecording = false;

                        timerTask.cancel();
                        stopRecording();
                    }
                }

            }
        });

        return mBinding.getRoot();
    }

    private void openSoundPicker() {

        Intent intent1 = new Intent(getActivity(), AudioPickActivity.class);
        intent1.putExtra(IS_NEED_RECORDER, false);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        intent1.putExtra(IS_NEED_FOLDER_LIST, true);

        if(mediaPlayer != null){

            mediaPlayer.pause();
            mBinding.ibPlayRecordedAudio.setVisibility(View.VISIBLE);
            mBinding.ibPauseRecordedAudio.setVisibility(View.GONE);
        }


        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_AUDIO);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!PermissionUtils.hasPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                || !PermissionUtils.hasPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || !PermissionUtils.hasPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            {
                String[] d = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(d, 1);
            }
        }
    }

    private void prepareRecordingStatus() {
        handler = new Handler();
//        forUpdateVisulizerHandler = new Handler();
        mBinding.cmRecordTime.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        mBinding.cmRecordTime.start();
        startRecording();
    }


    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            int audioEncodingBitRateForHigherVersions = 48000;
            mRecorder.setAudioEncodingBitRate(audioEncodingBitRateForHigherVersions);

        } else {
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            int audioEncodingBitRateForLowerVersions = 64000;
            mRecorder.setAudioEncodingBitRate(audioEncodingBitRateForLowerVersions);

        }
        int audioSampleRate = 16000;
        mRecorder.setAudioSamplingRate(audioSampleRate);

        mOutputFile = getOutputFile();
        //noinspection ResultOfMethodCallIgnored
        mOutputFile.getParentFile().mkdirs();
        mRecorder.setOutputFile(mOutputFile.getAbsolutePath());


        try {
            mRecorder.prepare();
            mRecorder.start();
            isRecording = true;
//            forUpdateVisulizerHandler.post(updateVisualizer);
            Log.d("RecordAudioFragment", "started recording to " + mOutputFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("RecordAudioFragment", "prepare() failed");
            isRecording = false;
        } catch (IllegalStateException e) {
            Log.e("RecordAudioFragment", "Illegal state exception happened in start recording");
            isRecording = false;
        }

    }


    private void stopRecording() {
        if (mRecorder != null) {
//            mRecordingSampler.stopRecording();
            handler = new Handler();

            isSounfFileFounded = true;
            mRecorder.stop();
            isRecording = false;
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;

            Log.e("RecordAudioFragment", "stopRecording - successful");
            Uri uri = Uri.fromFile(mOutputFile);
            if(mBinding.layoutPlayStopAudio.getVisibility() == View.GONE){
                mBinding.layoutPlayStopAudio.setVisibility(View.VISIBLE);
            }

            mediaPlayer = MediaPlayer.create(getContext(), uri);
            setupVisualizerFxAndUI();
            lineBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
            mBinding.seekBar.setMax(mediaPlayer.getDuration());
            mBinding.clRecordAudio.setVisibility(View.GONE);
            mBinding.clPreviewRecordedAudio.setVisibility(View.VISIBLE);
            mBinding.btnSubmitRecordedAudio.setVisibility(View.VISIBLE);
            mBinding.ibPauseRecordedAudio.setVisibility(View.GONE);
            mBinding.tvRecordedAudioCurrentTime.setText("0:00");
            mBinding.tvRecordedAudioRemainingTime.setText(
                    getString(R.string.recorded_audio_remaining_time,
                            milliSecondsToTimer(mediaPlayer.getDuration())));


            mBinding.ibPlayRecordedAudio.setOnClickListener(view -> {
//                mVisualizer.setEnabled(true);
                mediaPlayer.start();
                log("stopRecording", "mediaPlayer is start");
                updateSeekBar();

                mBinding.ibPlayRecordedAudio.setVisibility(View.GONE);
                mBinding.ibPauseRecordedAudio.setVisibility(View.VISIBLE);
            });
            mBinding.ibPauseRecordedAudio.setOnClickListener(view -> {
                mediaPlayer.pause();
                mBinding.ibPlayRecordedAudio.setVisibility(View.VISIBLE);
                mBinding.ibPauseRecordedAudio.setVisibility(View.GONE);
            });
            mBinding.ibResetRecordedAudio.setOnClickListener(view -> {
                mediaPlayer.seekTo(0);
//                mVisualizer.setEnabled(false);
            });
            mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                mBinding.ibPlayRecordedAudio.setVisibility(View.VISIBLE);
                mBinding.ibPauseRecordedAudio.setVisibility(View.GONE);
//                mVisualizer.setEnabled(false);
            });
        }
    }

    private void setupVisualizerFxAndUI() {

        // Create the Visualizer object and attach it to our media player.


        // define custom number of bars you want in the visualizer between (10 - 256).


        // Set your media player to the visualizer.


//        mVisualizer = new Visualizer(mediaPlayer.getAudioSessionId());
//        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
//        mVisualizer.setDataCaptureListener(
//                new Visualizer.OnDataCaptureListener() {
//                    public void onWaveFormDataCapture(Visualizer visualizer,
//                                                      byte[] bytes, int samplingRate) {
//                        mVisualizerView.updateVisualizer(bytes);
//                        mVisualizerView.setPadding(0 , 0 , 0 , 0);
//
//                    }
//
//                    public void onFftDataCapture(Visualizer visualizer,
//                                                 byte[] bytes, int samplingRate) {
//                    }
//                }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }

    private void onEndRecordingButtonClick() {
        timeWhenStopped = mBinding.cmRecordTime.getBase() - SystemClock.elapsedRealtime();
        mBinding.cmRecordTime.stop();
        stopRecording();
    }

    private File getOutputFile() {

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/Records/Recording_"
                    + "circleRecord"
                    + ".m4a");

        } else {
            return new File("");
        }
    }

    private void updateSeekBar() {
        if (handler != null) {
//            if (forUpdateVisulizerHandler != null) {
//                forUpdateVisulizerHandler.removeCallbacks(updateVisualizer);
//                forUpdateVisulizerHandler = null;
//            }
            mBinding.seekBar.setProgress(mediaPlayer.getCurrentPosition());
            mBinding.tvRecordedAudioCurrentTime.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
            mBinding.tvRecordedAudioRemainingTime.setText(
                    getString(R.string.recorded_audio_remaining_time,
                            milliSecondsToTimer(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()))
            );
            handler.postDelayed(runnable, 50);
        }
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
    public void onDetach() {
        if (handler == null) {

            log("onDetach", "handler is null");
            super.onDetach();
            return;
        }
        Log.e("onDetach", "onDetach - onDetach");
        handler.removeCallbacks(runnable);
        handler.removeCallbacksAndMessages(null);
//        forUpdateVisulizerHandler = null;
        handler = null;
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
//        isFromImagePicker = false;
//        try {
//            handler.removeMessages(0);
//            if (mediaPlayer != null) {
//                mediaPlayer.stop();
//                mediaPlayer.reset();
//                mediaPlayer.release();
//                log("onStop", "meidaStopped");
//                mediaPlayer = null;
//            }
////            mVisualizer.setEnabled(false);
////            mVisualizer.release();
//            mRecorder = null;
//            log("onStop", "mRecorder is null");
//            handler = null;
//            runnable = null;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        try {
            handler.removeMessages(0);
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                log("onStop", "meidaStopped");
                mediaPlayer = null;
            }
//            mVisualizer.setEnabled(false);
//            mVisualizer.release();
            mRecorder = null;
            log("onStop", "mRecorder is null");
            handler = null;
            runnable = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindData(ItemImageBinding binding, String path, int position) {
        Glide.with(this)
                .asBitmap()
                .load(path)
                .into(binding.imageView);
        binding.deleteIcon.setOnClickListener(v -> {
            paths.remove(position);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onItemClicked(ItemImageBinding binding, String model, int position) {

    }

    private void openImagePicker() {
//        new ImagePicker.Builder(getActivity())
//                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
//                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
//                .directory(ImagePicker.Directory.DEFAULT)
//                .extension(ImagePicker.Extension.PNG)
//                .allowMultipleImages(false)
//                .build();


//        isFromImagePicker = true;
        Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        intent1.putExtra(IS_NEED_FOLDER_LIST, true);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
//        Intent intent = new Intent(
//                Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");
//        startActivityForResult(
//                Intent.createChooser(intent, "Select File"),
//                Constant.REQUEST_CODE_PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
//            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);
//            if (paths == null) paths = new ArrayList<>();
//            paths.addAll(mPaths);
//            adapter.notifyDataSetChanged();
//            mBinding.btnAttachImage.setText(R.string.add_more_images);
//        }

        switch (requestCode) {

            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    String path = null;
                    List<String> mPaths = new ArrayList<>();
                    paths = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        mPaths.add(list.get(i).getPath());
                    }

                    if (paths == null) paths = new ArrayList<>();
                    paths.add(mPaths.get(list.size() - 1));
//                    adapter.notifyDataSetChanged();
                    Glide.with(this)
                            .asBitmap()
                            .load(paths.get(0))
                            .into(mBinding.image);
                    mBinding.btnAttachImage.setText(R.string.change_image);


                    break;

                }

            case Constant.REQUEST_CODE_PICK_AUDIO:
                if (resultCode == RESULT_OK) {
                    ArrayList<AudioFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_AUDIO);
                    ArrayList<File> fs = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        String path = list.get(i).getPath();
                        fs.add(new File(path));
                    }
                    mOutputFile = fs.get(0);
                    playmediaPlayer();

                    break;
                }

        }
    }

    private void playmediaPlayer() {
        handler = new Handler();

        Uri uri = Uri.fromFile(mOutputFile);
        isSoundFile = true;
        isSounfFileFounded = true;
        if(mBinding.layoutPlayStopAudio.getVisibility() == View.GONE){
            mBinding.layoutPlayStopAudio.setVisibility(View.VISIBLE);
        }

        mediaPlayer = MediaPlayer.create(getContext(), uri);
        lineBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
        mBinding.seekBar.setMax(mediaPlayer.getDuration());
        mBinding.clRecordAudio.setVisibility(View.GONE);
        mBinding.clPreviewRecordedAudio.setVisibility(View.VISIBLE);
        mBinding.btnSubmitRecordedAudio.setVisibility(View.VISIBLE);
        mBinding.ibPauseRecordedAudio.setVisibility(View.GONE);
        mBinding.tvRecordedAudioCurrentTime.setText("0:00");
        mBinding.tvRecordedAudioRemainingTime.setText(
                getString(R.string.recorded_audio_remaining_time,
                        milliSecondsToTimer(mediaPlayer.getDuration())));


        mBinding.ibPlayRecordedAudio.setOnClickListener(view -> {
//                mVisualizer.setEnabled(true);
            mediaPlayer.start();
            log("stopRecording", "mediaPlayer is start");
            updateSeekBar();

            mBinding.ibPlayRecordedAudio.setVisibility(View.GONE);
            mBinding.ibPauseRecordedAudio.setVisibility(View.VISIBLE);
        });
        mBinding.ibPauseRecordedAudio.setOnClickListener(view -> {
            mediaPlayer.pause();
            mBinding.ibPlayRecordedAudio.setVisibility(View.VISIBLE);
            mBinding.ibPauseRecordedAudio.setVisibility(View.GONE);
        });
        mBinding.ibResetRecordedAudio.setOnClickListener(view -> {
            mediaPlayer.seekTo(0);
//                mVisualizer.setEnabled(false);
        });
        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            mBinding.ibPlayRecordedAudio.setVisibility(View.VISIBLE);
            mBinding.ibPauseRecordedAudio.setVisibility(View.GONE);
//                mVisualizer.setEnabled(false);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            boolean allowed = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allowed = false;
                }
            }
            if (!allowed) {
                Toast.makeText(getActivity(), "you must allow permission ", Toast.LENGTH_SHORT).show();
            }
//            else {
//                prepareRecordingStatus();
//            }
        }
    }

//    Runnable updateVisualizer = new Runnable() {
//        @Override
//        public void run() {
//            if (isRecording) // if we are already recording
//            {
//                // get the current amplitude
//                int x = mRecorder.getMaxAmplitude();
//                visualizerView.addAmplitude(x); // update the VisualizeView
//                visualizerView.invalidate(); // refresh the VisualizerView
//
//                equalizer.animateBars();
//                forUpdateVisulizerHandler.postDelayed(this, REPEAT_INTERVAL);
//            }
//        }
//    };


//    public static byte[] fileToBytes(File file) {
//        int size = (int) file.length();
//        byte[] bytes = new byte[size];
//        try {
//            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
//            buf.read(bytes, 0, bytes.length);
//            buf.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bytes;
//    }


    private void log(String method, String s) {
        Log.e("RecordFragment", method + "  " + s);
    }


    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    try {

                        if (mediaPlayer != null) {

                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();
                            log("KEYCODE_BACK", "meidaStopped");
                            mediaPlayer = null;
                        }
                        if (mRecorder != null) {

                            mRecorder.stop();
                            isRecording = false;
                            mRecorder.reset();
                            mRecorder.release();
                            mRecorder = null;
                        }
                        log("KEYCODE_BACK", "mRecorder is null");

                        FragmentActivity activity = getActivity();
                        activity.onBackPressed();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    return true;
                }
                return false;
            }
        });
    }
}
