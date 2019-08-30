package com.circles.circlesapp.phase2.views.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.circles.circlesapp.R;
import com.circles.circlesapp.addgroup.MessageEvent;
import com.circles.circlesapp.chatlist.ChatRoom;
import com.circles.circlesapp.databinding.FragmentChatMessaging2Binding;
import com.circles.circlesapp.group.groupMember.MemberItem;
import com.circles.circlesapp.group.groupMember.MemberItemList;
import com.circles.circlesapp.helpers.FilePath;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.helpers.utilities.CustomOnClickListener;
import com.circles.circlesapp.messaging.SingletonSocket;
import com.circles.circlesapp.messaging.emotion.AndroidUtilities;
import com.circles.circlesapp.messaging.emotion.EmojiView;
import com.circles.circlesapp.messaging.emotion.NotificationCenter;
import com.circles.circlesapp.messaging.emotion.SizeNotifierRelativeLayout;
import com.circles.circlesapp.messaging.model.Message;
import com.circles.circlesapp.messaging.model.MessageFiles;
import com.circles.circlesapp.messaging.model.SendMessageBody;
import com.circles.circlesapp.messaging.model.SocketJoinObject;
import com.circles.circlesapp.messaging.view.ChooseToSendFragment;
import com.circles.circlesapp.messaging.view.PollFragment;
import com.circles.circlesapp.messaging.viewmodel.MessagesViewModel;
import com.circles.circlesapp.phase2.services.model.MessagePhase2;
import com.circles.circlesapp.phase2.views.adapter.MessagesAdapterPhase2;
import com.circles.circlesapp.retrofit.RetrofitClient;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.AudioPickActivity;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.activity.VideoPickActivity;
import com.vincent.filepicker.filter.entity.AudioFile;
import com.vincent.filepicker.filter.entity.ImageFile;
import com.vincent.filepicker.filter.entity.VideoFile;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

import static android.app.Activity.RESULT_OK;
import static com.vincent.filepicker.activity.AudioPickActivity.IS_NEED_RECORDER;
import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatMessagingFragment extends Fragment implements CustomOnClickListener<MessageFiles>, NotificationCenter.NotificationCenterDelegate {


    private static final String SOCKET_URL = "http://ec2-18-216-242-74.us-east-2.compute.amazonaws.com:6001";
    private static final String IMAGE_FILE_TYPE = "image";
    private static final String VIDEO_FILE_TYPE = "video";
    private static final String PDF_FILE_TYPE = "pdf";
    private static final String VOICE_FILE_TYPE = "voice";
    private static final String VOTING_MSG_TYPE = "voting";
    private static final String MESSAGE_MSG_TYPE = "message";
    private static final int PICK_PDF_REQUEST = 20;
    private Uri filePath;
    private static String CONVERSATION_ID = "conversationId";
    FragmentChatMessaging2Binding b;
    MessagesViewModel mViewModel;
    private SendMessageBody sendMessageBody;
    private long time;
    Socket socket;
    private ChatRoom chatRoom;
    private List<Message> messageList = new ArrayList<>();
    private MessageListenerView audioRecordView;
    MessagesAdapterPhase2 adapter;
    private MediaRecorder mRecorder = null;
    private File mOutputFile;

    //    Receiver receiver;
    boolean isReciverRegistered = false;


    private ImageView enterChatView1, emojiButton, videoButton;
    EditText editTextMessage;
    private EmojiView emojiView;
    private SizeNotifierRelativeLayout sizeNotifierRelativeLayout;
    private boolean showingEmoji;

    private int keyboardHeight;

    private boolean keyboardVisible;

    private WindowManager.LayoutParams windowLayoutParams;

    private String sentMsgText;

    {
        socket = SingletonSocket.newInstance();
    }


    public ChatMessagingFragment() {
        // Required empty public constructor
    }


    public static ChatMessagingFragment newInstance(ChatRoom chatRoom) {
        ChatMessagingFragment fragment = new ChatMessagingFragment();
        Bundle args = new Bundle();
        args.putParcelable("chatRoom", chatRoom);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            chatRoom = getArguments().getParcelable("chatRoom");
//            if (chatRoom == null) getActivity().onBackPressed();
//            initializingSendMsgBody();
        }

        AndroidNetworking.initialize(getActivity().getApplicationContext());
    }

    private void initializingSendMsgBody() {
        sendMessageBody = new SendMessageBody();
        sendMessageBody.setId(chatRoom.id);
        sendMessageBody.setLatitude(chatRoom.latitude);
        sendMessageBody.setLongitude(chatRoom.longitude);
        sendMessageBody.setMessageType(MESSAGE_MSG_TYPE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        AndroidUtilities.statusBarHeight = getStatusBarHeight();

        b = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_messaging_2, container, false);
        mViewModel = ViewModelProviders.of(this).get(MessagesViewModel.class);


//        b.setViewModel(mViewModel);
//        b.setLifecycleOwner(this);

//        joinSocket();
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        audioRecordView = b.recordingView;
        mViewModel.toastMsg.observe(this, o -> {
            Toast.makeText(getContext(), o, Toast.LENGTH_SHORT).show();
        });


        b.recordingView.findViewById(R.id.ivAttachment).setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.add(android.R.id.content, ChooseToSendFragment.newInstance(chatRoom.is_group), "").commit();
        });

        setListener();

        editTextMessage = b.recordingView.findViewById(R.id.editTextMessage);

        NotificationCenter.getInstance().addObserver(this, NotificationCenter.emojiDidLoaded);


        mViewModel.mesSentSuccessfully.observe(this, bool -> {
//            if (bool != null) {
//                if (bool) {
//                    editTextMessage.setText("");
//                    String s = new Gson().toJson(sendMessageBody);
//                    try {
//                        JSONObject jsonObject = new JSONObject(s);
//                        Log.d("mesSentSuccessfully", "onCreateView: " + s);
//                        socket.emit(chatRoom.channel, jsonObject);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    initializingSendMsgBody();
//                } else {
//                    Toast.makeText(getContext(), "message not Sent,Press send again", Toast.LENGTH_SHORT).show();
//
//                }
//            }
        });

        List<MessagePhase2> models = new ArrayList<>();
//        models.add(new MessagePhase2(true , "Kamal khalaf" , "20 KM" , "12.30 AM" , false));
//        models.add(new MessagePhase2(false , "Mohammed khalaf" , "1.5 KM" , "12.30 AM" , false));
//        models.add(new MessagePhase2(true , "Ahmed Magdy" , "10 KM" , "12.30 PM" , true));
//        models.add(new MessagePhase2(false , "User Name" , "15 KM" , "1.30 AM" , false));
//        models.add(new MessagePhase2(true , "User Name" , "0.5 KM" , "5.8 PM" , false));
//        models.add(new MessagePhase2(false , "User Name" , "200 KM" , "4.03 AM" , false));
//        models.add(new MessagePhase2(true , "User Name" , "1.8 KM" , "8.22 PM" , false));
//        models.add(new MessagePhase2(true , "User Name" , "3.5 KM" , "5.30 AM" , true));
//        models.add(new MessagePhase2(false , "User Name" , "50 KM" , "12.11 PM" , false));
//        models.add(new MessagePhase2(true , "User Name" , "44 KM" , "7.07 PM" , true));
        adapter = new MessagesAdapterPhase2(getActivity(), models);
        b.rvMessaging.setAdapter(adapter);


//        mViewModel.messagesListItems.observe(this, messages -> {
//            if (messages != null) {
//
//
//                messageList.clear();
//                messageList.addAll(messages);
//                adapter.notifyDataSetChanged();
//                b.rvMessaging.smoothScrollToPosition(messageList.size() - 1);
//
//            } else {
//
//            }
//        });


        return b.getRoot();
    }

    private void openVideoPivker() {

        Intent intent2 = new Intent(getActivity(), VideoPickActivity.class);
        intent2.putExtra(IS_NEED_CAMERA, true);
        intent2.putExtra(Constant.MAX_NUMBER, 1);
        intent2.putExtra(IS_NEED_FOLDER_LIST, true);
        startActivityForResult(intent2, Constant.REQUEST_CODE_PICK_VIDEO);

    }

    private void setListener() {

        audioRecordView.getAttachmentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.addToBackStack(null);
//                transaction.add(android.R.id.content, ChooseToSendFragment.newInstance(chatRoom.is_group), "").commit();
            }
        });

        audioRecordView.getSendView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String msg = audioRecordView.getMessageView().getText().toString();
//                audioRecordView.getMessageView().setText("");
//
//                sendMessageBody.setMessageBody(msg);
//
//                sentMsgText = sendMessageBody.getMessageBody();
//                if (sendMessageBody.getFileList().isEmpty()) {
//                    if (sendMessageBody.getMessageBody().trim().length() == 0) {
//                        Toast.makeText(getContext(), "empty message", Toast.LENGTH_SHORT).show();
//                    } else {
//                        mViewModel.sendMessage2(sendMessageBody);
//                    }
//                    editTextMessage.setText("");
//                } else {
//                    mViewModel.sendMessage2(sendMessageBody);
//                }

            }
        });
    }


    private void joinSocket() {
        socket.connect();
        socket.on("connection", joinSocketEmitter());

    }


    private Emitter.Listener joinSocketEmitter() {
        return args -> {
            if (getActivity() != null)
                getActivity().runOnUiThread(() -> {
                    JSONObject jsonObject = (JSONObject) args[0];
                    try {
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            SocketJoinObject o = new SocketJoinObject();
                            o.room = chatRoom.room;
                            String s = new Gson().toJson(o);
                            Log.d("joinSocketEmitter", "joinSocketEmitter: " + s);
                            JSONObject h = new JSONObject(s);
                            socket.emit("join", h);

                            socket.on("join", getJoinEventEmitter());
                        } else {
                            getActivity().onBackPressed();
                            Log.d("joinSocketEmitter", "joinSocketEmitter: " + "not 200");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("joinSocketEmitter", "joinSocketEmitter: " + "JSONException");
                    }
                });
        };

    }

    private Emitter.Listener getJoinEventEmitter() {
        return args -> {
            if (getActivity() != null)
                getActivity().runOnUiThread(() -> {
                    JSONObject jsonObject = (JSONObject) args[0];
                    try {
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            mViewModel.loadMessages(chatRoom.id);
                            socket.on(chatRoom.channel, getChannelEventmitter());
                            socket.on("on" + chatRoom.room + "UsersUpdate", getUsersEmiiter());
                            Log.d("", "getJoinEventEmitter: " + "On" + chatRoom.room + "UsersUpdate");
                        } else {
                            Log.d("getJoinEventEmitter", "getJoinEventEmitter: " + jsonObject.getString("message"));
                            getActivity().onBackPressed();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
        };
    }

    @NonNull
    private Emitter.Listener getUsersEmiiter() {
        return args -> {
            JSONArray members = (JSONArray) args[0];
            Gson gson = new Gson();
            try {
                MemberItemList.getInst().clearList();
                for (int i = 0; i < members.length(); i++) {
                    MemberItem memberItem = gson.fromJson(members.getJSONObject(i).toString(), MemberItem.class);
                    MemberItemList.getInst().addToList(memberItem);
                }
            } catch (Exception s) {
                s.printStackTrace();
            }
        };
    }

    private Emitter.Listener getChannelEventmitter() {
        return args -> {
            if (getActivity() != null)
                getActivity().runOnUiThread(() -> {
                    JSONObject jsonObject = (JSONObject) args[0];
                    try {
                        Message message = new Gson().fromJson(jsonObject.toString(), Message.class);
                        if (message.getMessageSender().isCurrentuser()) {
                            message.setMessageBody(sentMsgText);
                        }
                        messageList.add(message);
                        adapter.notifyDataSetChanged();
                        b.rvMessaging.smoothScrollToPosition(messageList.size() - 1);

                        Log.d("getChannelEventmitter", "getChannelEventmitter: " + jsonObject.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        };
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onclick(View v, MessageFiles model) {
        AndroidNetworking.post(RetrofitClient.BASE_URL + "messageVoting")
                .addHeaders("Authorization", MyServiceInterceptor.getAuth())
                .addHeaders("Accept", "application/json ")
                .addBodyParameter("room_id", chatRoom.id + "")
                .addBodyParameter("message_file_id", model.message_file_id + "")
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Log.d("", "onResponse: " + response);
                adapter.notifyItemChanged(model.itemPostion);
            }

            @Override
            public void onError(ANError anError) {
                Log.d("", "ANError: " + anError.getErrorBody());
                adapter.notifyItemChanged(model.itemPostion);
            }
        });
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
            Log.d("RecordAudioFragment", "started recording to " + mOutputFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("RecordAudioFragment", "prepare() failed");
        } catch (IllegalStateException e) {
            Log.e("RecordAudioFragment", "Illegal state exception happened in start recording");
        }

    }

    private File getOutputFile() {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Records/Recording_"
                + "circleRecord"
                + ".m4a");
    }

    private void stopRecording() {
        if (mRecorder != null) {

            try {
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;


                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setRecordpath(getOutputFile().getAbsolutePath());
                if (getOutputFile().getAbsolutePath() != null) {
                    sendMessageBody.setFileList(getOutputFile().getAbsolutePath());
                    sendMessageBody.setMessageType(MESSAGE_MSG_TYPE);
                    sendMessageBody.setMessageFileType(VOICE_FILE_TYPE);
//                    Toast.makeText(getContext(), "your record is Attached", Toast.LENGTH_SHORT).show();


                    mViewModel.sendMessage2(sendMessageBody);
                } else {
                    Toast.makeText(getContext(), "No record selected", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
            }

        }
    }


    @Override
    public void onStop() {
        super.onStop();
        try {
            mRecorder = null;
        } catch (Exception e) {
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.resetAllVideos();
        try {
            if (null != mRecorder) {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }

        } catch (Exception e) {
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onRecordSubmitted(MessageEvent messageEvent) {
        String path = messageEvent.getRecordpath();
        if (path != null) {
            sendMessageBody.setFileList(path);
            sendMessageBody.setMessageType(MESSAGE_MSG_TYPE);
            sendMessageBody.setMessageFileType(VOICE_FILE_TYPE);
//            Toast.makeText(getContext(), "your record is Attached", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "No record selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPollSubmitted(SendMessageBody event) {
        List<File> fileList = event.getFileList();
        if (fileList != null) {
            sendMessageBody.setFileList(fileList);
            sendMessageBody.setMessageType(VOTING_MSG_TYPE);
            sendMessageBody.setMessageFileType(IMAGE_FILE_TYPE);
            sendMessageBody.setMessageBody(event.getMessageBody());
            sentMsgText = sendMessageBody.getMessageBody();
            mViewModel.sendMessage2(sendMessageBody);
        } else {
            Toast.makeText(getContext(), "No Poll Created", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMuteClicked(MemberItem event) {
        if (adapter != null) adapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        socket.disconnect();
        socket.off("connection", joinSocketEmitter());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationCenter.getInstance().removeObserver(this, NotificationCenter.emojiDidLoaded);
    }

    /**
     * Updates emoji views when they are complete loading
     *
     * @param id
     * @param args
     */

    @Override
    public void didReceivedNotification(int id, Object... args) {

        if (id == NotificationCenter.emojiDidLoaded) {
            if (emojiView != null) {
                emojiView.invalidateViews();
            }

//            if (chatListView != null) {
//                chatListView.invalidateViews();
//            }
        }

    }

    /**
     * Get the system status bar height
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void sendImageOrVideo() {
        openImagePicker();
    }

    private void openImagePicker() {

        Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 9);
        intent1.putExtra(IS_NEED_FOLDER_LIST, true);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case Constant.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                    ArrayList<File> fs = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        String path = list.get(i).getPath();
                        fs.add(new File(path));
                    }
                    sendMessageBody.setFileList(fs);
                    sendMessageBody.setMessageType(MESSAGE_MSG_TYPE);
                    sendMessageBody.setMessageFileType(IMAGE_FILE_TYPE);
//                    Toast.makeText(getContext(), "your message start to send" + String.valueOf(fs.size()), Toast.LENGTH_SHORT).show();
                    mViewModel.sendMessage2(sendMessageBody);

                    break;

                }

            case Constant.REQUEST_CODE_PICK_VIDEO:
                if (resultCode == RESULT_OK) {

                    ArrayList<VideoFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO);
                    ArrayList<File> fs = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        String path = list.get(i).getPath();
                        fs.add(new File(path));
                    }

                    sendMessageBody.setFileList(fs.get(0).getAbsolutePath());
                    sendMessageBody.setMessageType(MESSAGE_MSG_TYPE);
                    sendMessageBody.setMessageFileType(VIDEO_FILE_TYPE);
//                    Toast.makeText(getContext(), "your message start to send " + list.get(0).getName(), Toast.LENGTH_SHORT).show();
                    mViewModel.sendMessage2(sendMessageBody);


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

                    sendMessageBody.setFileList(fs.get(0).getAbsolutePath());
                    sendMessageBody.setMessageType(MESSAGE_MSG_TYPE);
                    sendMessageBody.setMessageFileType(VOICE_FILE_TYPE);
//                    Toast.makeText(getContext(), "your message start to send " + list.get(0).getName(), Toast.LENGTH_SHORT).show();
                    mViewModel.sendMessage2(sendMessageBody);

                    break;
                }
        }

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            String displayName = null;

            String contenet = FilePath.getPath(getActivity(), filePath);


            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                        sendMessageBody.setFileList(contenet);
                        sendMessageBody.setMessageType(MESSAGE_MSG_TYPE);
                        sendMessageBody.setMessageFileType(displayName);
//                        Toast.makeText(getContext(), "your pdf is Attached", Toast.LENGTH_SHORT).show();
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }


        }

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener

                    if (Jzvd.backPress()) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });
    }

    public void sendFiles() {

        openSounds();
    }

    private void openSounds() {


        Intent intent1 = new Intent(getActivity(), AudioPickActivity.class);
        intent1.putExtra(IS_NEED_RECORDER, true);
        intent1.putExtra(Constant.MAX_NUMBER, 1);
        intent1.putExtra(IS_NEED_FOLDER_LIST, true);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_AUDIO);

    }


    public void createPoll() {
        try {
            getActivity().getSupportFragmentManager().popBackStack();
        } catch (Exception d) {

            android.content.Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
            intent1.putExtra(IS_NEED_CAMERA, true);
            intent1.putExtra(Constant.MAX_NUMBER, 2);
            intent1.putExtra(IS_NEED_FOLDER_LIST, true);
            startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);

        }
        sendMessageBody.setMessageType(VOTING_MSG_TYPE);
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(android.R.id.content, PollFragment.newInstance(), "PollFragment").addToBackStack(null).commit();
        }

    }

}
