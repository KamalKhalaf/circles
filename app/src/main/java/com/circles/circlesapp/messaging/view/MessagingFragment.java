package com.circles.circlesapp.messaging.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.circles.circlesapp.R;
import com.circles.circlesapp.addgroup.MessageEvent;
import com.circles.circlesapp.chatlist.ChatRoom;
import com.circles.circlesapp.databinding.FragmentMessagingBinding;
import com.circles.circlesapp.group.groupMember.MemberItem;
import com.circles.circlesapp.group.groupMember.MemberItemList;
import com.circles.circlesapp.helpers.BroadcastHelper;
import com.circles.circlesapp.helpers.FilePath;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.circles.circlesapp.helpers.ui.ActivityUtils;
import com.circles.circlesapp.helpers.utilities.CustomOnClickListener;
import com.circles.circlesapp.messaging.SingletonSocket;
import com.circles.circlesapp.messaging.model.Message;
import com.circles.circlesapp.messaging.model.MessageFiles;
import com.circles.circlesapp.messaging.model.SendMessageBody;
import com.circles.circlesapp.messaging.model.SocketJoinObject;
import com.circles.circlesapp.messaging.view.adapter.MessagesAdapter;
import com.circles.circlesapp.messaging.viewmodel.MessagesViewModel;
import com.circles.circlesapp.retrofit.RetrofitClient;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.filter.entity.ImageFile;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.vincent.filepicker.activity.BaseActivity.IS_NEED_FOLDER_LIST;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class MessagingFragment extends Fragment implements CustomOnClickListener<MessageFiles> {


    private static final String SOCKET_URL = "http://ec2-18-216-242-74.us-east-2.compute.amazonaws.com:6001";
    private static final String IMAGE_FILE_TYPE = "image";
    private static final String VOICE_FILE_TYPE = "voice";
    private static final String VOTING_MSG_TYPE = "voting";
    private static final String MESSAGE_MSG_TYPE = "message";
    private static final int PICK_PDF_REQUEST = 20;
    private Uri filePath;
    private static String CONVERSATION_ID = "conversationId";
    FragmentMessagingBinding b;
    MessagesViewModel mViewModel;
    private SendMessageBody sendMessageBody;
    Socket socket;
    private ChatRoom chatRoom;
    private List<Message> messageList = new ArrayList<>();

    MessagesAdapter adapter;
    private String sentMsgText;

    {
        socket = SingletonSocket.newInstance();
    }

    public MessagingFragment() {
        // Required empty public constructor
    }

    public static MessagingFragment newInstance(ChatRoom chatRoom) {
        MessagingFragment fragment = new MessagingFragment();
        Bundle args = new Bundle();
        args.putParcelable("chatRoom", chatRoom);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chatRoom = getArguments().getParcelable("chatRoom");
            if (chatRoom == null) getActivity().onBackPressed();
            initializingSendMsgBody();
        }
    }

    private void initializingSendMsgBody() {
        sendMessageBody = new SendMessageBody();
        sendMessageBody.setId(chatRoom.id);
        sendMessageBody.setLatitude(chatRoom.latitude);
        sendMessageBody.setLongitude(chatRoom.longitude);
        sendMessageBody.setMessageType(MESSAGE_MSG_TYPE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_messaging, container, false);
        mViewModel = ViewModelProviders.of(this).get(MessagesViewModel.class);
        b.setViewModel(mViewModel);
        b.setLifecycleOwner(this);
        //  mViewModel.loadMessages(chatHisId);
        joinSocket();
        if (getActivity() != null) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        mViewModel.toastMsg.observe(this, o -> {
            Toast.makeText(getContext(), o, Toast.LENGTH_SHORT).show();
        });
        b.ibAttachImage.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.add(android.R.id.content, ChooseToSendFragment.newInstance(chatRoom.is_group), "").commit();
        });
        b.ibRecord.setOnClickListener(v -> {
            RecordAudioDialogFragment f = RecordAudioDialogFragment.newInstance();
            ActivityUtils.replaceFragmentInActivity(getActivity().getSupportFragmentManager(), f, android.R.id.content, true);

        });
        b.etMessageContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                sendMessageBody.setMessageBody(editable.toString());
            }
        });
        b.ibSendMessage.setOnClickListener(v -> {
            sentMsgText = sendMessageBody.getMessageBody();
            if (sendMessageBody.getFileList().isEmpty()) {
                if (sendMessageBody.getMessageBody().trim().length() == 0) {
                    Toast.makeText(getContext(), "empty message", Toast.LENGTH_SHORT).show();
                } else {
                    mViewModel.sendMessage2(sendMessageBody);
                }
                b.etMessageContent.setText("");
            } else {
                mViewModel.sendMessage2(sendMessageBody);
            }

        });
        mViewModel.mesSentSuccessfully.observe(this, bool -> {
            if (bool != null) {
                if (bool) {
                    b.etMessageContent.setText("");
                    String s = new Gson().toJson(sendMessageBody);
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        Log.d("mesSentSuccessfully", "onCreateView: " + s);
                        socket.emit(chatRoom.channel, jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    initializingSendMsgBody();
                } else {
                    Toast.makeText(getContext(), "message not Sent,Press send again", Toast.LENGTH_SHORT).show();

                }
            }
        });
        adapter = new MessagesAdapter(getActivity(), messageList, this);
        b.rvMessaging.setAdapter(adapter);


        mViewModel.messagesListItems.observe(this, messages -> {
            if (messages != null) {
                messageList.clear();
                messageList.addAll(messages);
                adapter.notifyDataSetChanged();
                b.rvMessaging.smoothScrollToPosition(messageList.size() - 1);

            }
        });
        return b.getRoot();
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
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                    Toast.makeText(getContext(), "your photo is Attached" + String.valueOf(fs.size()), Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(getContext(), "your pdf is Attached", Toast.LENGTH_SHORT).show();
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }


        }

    }

    public void sendImageOrVideo() {
        openImagePicker();
    }

    public void sendFiles() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    public void createPoll() {
        try {
            getActivity().getSupportFragmentManager().popBackStack();
        } catch (Exception d) {
//            new ImagePicker.Builder(getActivity())
//                    .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
//                    .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
//                    .directory(ImagePicker.Directory.DEFAULT)
//                    .extension(ImagePicker.Extension.PNG)
//                    .allowMultipleImages(false)
//                    .build();

            Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
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

    private void openImagePicker() {
//        new ImagePicker.Builder(getActivity())
//                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
//                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
//                .directory(ImagePicker.Directory.DEFAULT)
//                .extension(ImagePicker.Extension.PNG)
//                .allowMultipleImages(false)
//                .build();


        Intent intent1 = new Intent(getActivity(), ImagePickActivity.class);
        intent1.putExtra(IS_NEED_CAMERA, true);
        intent1.putExtra(Constant.MAX_NUMBER, 9);
        intent1.putExtra(IS_NEED_FOLDER_LIST, true);
        startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onRecordSubmitted(MessageEvent messageEvent) {
        String path = messageEvent.getRecordpath();
        if (path != null) {
            sendMessageBody.setFileList(path);
            sendMessageBody.setMessageType(MESSAGE_MSG_TYPE);
            sendMessageBody.setMessageFileType(VOICE_FILE_TYPE);
            Toast.makeText(getContext(), "your record is Attached", Toast.LENGTH_SHORT).show();
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
}
