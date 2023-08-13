package com.client.utedating.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.client.utedating.R;
import com.client.utedating.adapters.ChatAdapter;
import com.client.utedating.fragments.SupportBottomSheetDialogFragment;
import com.client.utedating.models.Message;
import com.client.utedating.models.MessageModel;
import com.client.utedating.models.MessageSocket;
import com.client.utedating.models.NoResultModel;
import com.client.utedating.models.NotificatiionSocket;
import com.client.utedating.models.NotificationReceived;
import com.client.utedating.models.NotificationSend;
import com.client.utedating.models.User;
import com.client.utedating.models.UserModel;
import com.client.utedating.retrofit.ConversationApiService;
import com.client.utedating.retrofit.NotificationApiService;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.RetrofitNotification;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    ImageView imageViewAvatar;
    TextView textViewName;
    RecyclerView recyclerViewChat;
    EditText editTextChat;
    AppCompatImageButton buttonSentMessage;
    AppCompatImageView imageViewChatBack, imageViewSupport;
    LinearLayout linearLayoutUserName;

    SharedPreferencesClient sharedPreferencesClient;
    User user;
    UserApiService userApiService;
    ConversationApiService conversationApiService;
    NotificationApiService notificationApiService;
    String receiverId;
    String receiverToken;
    String receiverName;
    String receiverAvatar;
    String conversationId;

    List<Message> messageList = new ArrayList<>();
    ChatAdapter chatAdapter;

    Socket mSocket;

    LinearLayoutManager linearLayoutManager;

    Boolean isReceiverOffLine = true;
    Integer isFirstTimeLoading = 1;
    Boolean isLoading = false;
    Handler handler = new Handler();
    Integer page = 0;
    private Emitter.Listener onGetMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG", "onGetMessage");
                    JSONObject data = (JSONObject) args[0];
                     onLoadMessage(data.optString("conversationId"), data.optString("message"), data.optString("receiverId"));
                }
            });
        }
    };

    private Emitter.Listener onResultNotification = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG", "onResultNotification");
                    JSONObject data = (JSONObject) args[0];
                    isReceiverOffLine = data.optBoolean("result");
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();
        setData();
        setRecyclerView();
        setSocket();
        fetchFirstData();
        handleEvent();
    }

    private void initView() {
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textViewName = findViewById(R.id.textViewName);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextChat = findViewById(R.id.editTextChat);
        buttonSentMessage = findViewById(R.id.buttonSentMessage);
        imageViewSupport = findViewById(R.id.imageViewSupport);
        imageViewChatBack = findViewById(R.id.imageViewChatBack);
        linearLayoutUserName = findViewById(R.id.linearLayoutUserName);
        buttonSentMessage.setEnabled(false);

        sharedPreferencesClient = new SharedPreferencesClient(this);
        user = sharedPreferencesClient.getUserInfo("user");
        conversationApiService = RetrofitClient.getInstance().create(ConversationApiService.class);
        notificationApiService = RetrofitNotification.getInstance().create(NotificationApiService.class);
        userApiService = RetrofitClient.getInstance().create(UserApiService.class);
    }

    private void setData() {
        receiverId = getIntent().getStringExtra("receiverId");
        receiverToken = getIntent().getStringExtra("receiverToken");
        receiverName = getIntent().getStringExtra("name");
        receiverAvatar = getIntent().getStringExtra("avatar");
        conversationId = getIntent().getStringExtra("conversationId");

        Log.e("TAG", conversationId);

        textViewName.setText(receiverName);
        Glide
                .with(this)
                .load(receiverAvatar)
                .centerCrop()
                .into(imageViewAvatar);
    }

    private void setRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewChat.setLayoutManager(linearLayoutManager);
        recyclerViewChat.setHasFixedSize(true);

        chatAdapter = new ChatAdapter(messageList, receiverAvatar, user.get_id());
        recyclerViewChat.setAdapter(chatAdapter);

    }

    private void setSocket() {
        try {
            mSocket = IO.socket("http://10.0.2.2:5000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.connect();
        mSocket.on("getMessage", onGetMessage);
        mSocket.on("resultNotification", onResultNotification);
        mSocket.emit("addUser", user.get_id());
    }

    private void fetchFirstData() {
        conversationApiService.getMoreMessages(conversationId, page).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    Log.e("TAG", response.body().getMessage());
                    messageList.addAll(0, response.body().getResult());
                    chatAdapter.notifyDataSetChanged();
                    if(messageList.size() > 1){
                        recyclerViewChat.smoothScrollToPosition(messageList.size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }

    private void fetchData() {
//        conversationApiService.getMessages(conversationId).enqueue(new Callback<MessageModel>() {
//            @Override
//            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
//                if (response.isSuccessful()) {
//                    Log.e("TAG", response.body().getMessage());
//                    messageList.addAll(response.body().getResult());
//                    chatAdapter.notifyDataSetChanged();
//                    if(messageList.size() > 1){
//                        recyclerViewChat.smoothScrollToPosition(messageList.size() - 1);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MessageModel> call, Throwable t) {
//                Log.e("TAG", t.getMessage());
//            }
//        });
        conversationApiService.getMoreMessages(conversationId, page).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    Log.e("TAG", response.body().getMessage());
                    if(response.body().getResult().size() != 0){
                        messageList.addAll(0, response.body().getResult());
//                        chatAdapter.notifyItemRangeChanged(0, response.body().getResult().size());
                        chatAdapter.notifyItemRangeInserted(0, response.body().getResult().size());
                        isLoading = false;
                        if(response.body().getResult().size() > 1){
                            // ? Cuộn xuống 2 phần tử
                            recyclerViewChat.smoothScrollToPosition(response.body().getResult().size() - 1 + -1);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }

    private void handleEvent() {
        buttonSentMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conversationApiService.isExist(conversationId).enqueue(new Callback<NoResultModel>() {
                    @Override
                    public void onResponse(Call<NoResultModel> call, Response<NoResultModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getMessage().equals("noExist")) {
//                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                                ConversationNotExistDialogFragment dialog = new ConversationNotExistDialogFragment();
//                                dialog.show(ft, "dialog");
                                Dialog dialog = new Dialog(ChatActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_conversation_not_exist);
                                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.getWindow().setDimAmount(0.6f);
                                dialog.getWindow().setGravity(Gravity.CENTER);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();

                                Button btnOk = dialog.findViewById(R.id.btn_ok);
                                btnOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });

                            } else {
                                Gson gson = new Gson();
                                MessageSocket messageSocket = new MessageSocket(conversationId, editTextChat.getText().toString().trim(), receiverId);
                                mSocket.emit("sendMessage", gson.toJson(messageSocket));
                                onLoadMessage(conversationId, editTextChat.getText().toString().trim(), receiverId);

                                NotificatiionSocket notificatiionSocket = new NotificatiionSocket(user.get_id(), receiverId);
                                mSocket.emit("sendNotification", gson.toJson(notificatiionSocket));

                                Map<String, String> body = new HashMap<>();
                                body.put("receiver", receiverId);
                                body.put("message", editTextChat.getText().toString().trim());
                                conversationApiService.sendMessage(conversationId, body).enqueue(new Callback<NoResultModel>() {
                                    @Override
                                    public void onResponse(Call<NoResultModel> call, Response<NoResultModel> response) {
                                        if (response.isSuccessful()) {
                                            Log.e("TAG", response.body().getMessage());
                                            Log.e("TAG", "isReceiverOffLine: "+ isReceiverOffLine);
                                            //If receiver is offline, send notification to receiver
                                            if(isReceiverOffLine){
                                                //Send notification
                                                sendNotification();
                                            }
                                            editTextChat.setText("");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<NoResultModel> call, Throwable t) {
                                        Log.e("TAG", t.getMessage());
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NoResultModel> call, Throwable t) {
                        Log.e("TAG", t.getMessage());
                    }
                });
            }

            ;
        });


        editTextChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Lấy số ký tự đang nhập
                int length = s.toString().length();
                if (length == 0) {
                    buttonSentMessage.setEnabled(false);
                } else {
                    buttonSentMessage.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imageViewChatBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageViewSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportBottomSheetDialogFragment bottomSheet = new SupportBottomSheetDialogFragment();
                Bundle bundle = new Bundle();
                // Đặt giá trị cho Bundle, ví dụ:
                bundle.putString("receiverId", receiverId);
                bundle.putString("conversationId", conversationId);
                // Đặt Bundle vào Fragment
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
            }
        });

        linearLayoutUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ChatActivity.this);
                dialog.setContentView(R.layout.dialog_user_profile);

                setDataDialog(dialog);
                // Thiết lập các thuộc tính cho dialog
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.CENTER);
                // Cho phép ẩn dialog khi nhấn ra ngoài
                dialog.setCanceledOnTouchOutside(true);
                // Hiển thị dialog
                dialog.show();
            }
        });

        recyclerViewChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isLoading){
                    if(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0){
                        // ! Tại lần đầu khi fetchData bị dính trường hợp này nên cần bỏ qua
                        // ! Logic tạm thời xử lý như này, có thời gian sẽ tối ưu
                        if(isFirstTimeLoading>= 3){
                            Log.e("TAG", "findFirstCompletelyVisibleItemPosition:"+isFirstTimeLoading);
                            isLoading = true;
                            loadMoreMessage();
                        }
                        Log.e("TAG", "isFirstTimeLoading:"+isFirstTimeLoading);
                        isFirstTimeLoading++;
                    }
                }
            }
        });

    }

    private void onLoadMessage(String conversationId, String message, String receiverId) {
//        MessageSocket m = new MessageSocket(conversationId, message, receiverId);
        Message m = new Message();
        m.setReceiver(receiverId);
        m.setContent(message);
        m.setSentAt(new Date());

        addMessageToList(m);

    }

    private void addMessageToList(Message m) {
        messageList.add(m);
        chatAdapter.notifyItemInserted(messageList.indexOf(m));
        recyclerViewChat.smoothScrollToPosition(messageList.size() - 1);
    }

    private void sendNotification() {
        Map<String, String> notification = new HashMap<>();
        notification.put("title", user.getName());
        notification.put("body", editTextChat.getText().toString().trim());
        Log.e("TAG", "Received Token: " + receiverToken);
        NotificationSend notificationSend = new NotificationSend(receiverToken, notification);
        notificationApiService.sendNotification(notificationSend).enqueue(new Callback<NotificationReceived>() {
            @Override
            public void onResponse(Call<NotificationReceived> call, Response<NotificationReceived> response) {
                if (response.isSuccessful()) {
                    Log.e("TAG", "Notification Send Successfully");
                }
            }

            @Override
            public void onFailure(Call<NotificationReceived> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }

    public void setDataDialog(Dialog dialog){
        ImageView imageViewAvatar = dialog.findViewById(R.id.imageViewAvatar);
        TextView nameAgeTxt = dialog.findViewById(R.id.textViewNameAndAge);
        TextView textViewGender = dialog.findViewById(R.id.textViewGender);
        TextView textViewFaculty = dialog.findViewById(R.id.textViewFaculty);
        TextView textViewLocation = dialog.findViewById(R.id.textViewLocation);
        TextView textViewAbout = dialog.findViewById(R.id.textViewAbout);
        ChipGroup chipGroupInterest = dialog.findViewById(R.id.chipGroupInterest);

        userApiService.getInfo(receiverId).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()){
                    User u = response.body().getResult();

                    Glide
                            .with(dialog.getContext())
                            .load(u.getAvatar())
                            .centerCrop()
                            .into(imageViewAvatar);

                    nameAgeTxt.setText(u.getName() + ", " + getAge(u.getBirthday()));

                    textViewGender.setText(u.getGender().equals("male") ? "♂️ Nam" : "♀️ Nữ" );
                    textViewFaculty.setText("🎓 Khoa "+ u.getFaculty());
                    textViewLocation.setText("📌 Cách xa "+String.valueOf(calculateDistance((List<Double>) u.getLocation().get("coordinates"),(List<Double>) user.getLocation().get("coordinates")))+"km");
                    textViewAbout.setText(u.getAbout());

                    for (int i = 0; i < chipGroupInterest.getChildCount(); i++) {
                        Chip chip = (Chip) chipGroupInterest.getChildAt(i);
                        String chipText = chip.getText().toString();
                        int flag = 0;
                        for(int j = 0; j < u.getInterests().size(); j++){
                            if(chipText.equals(u.getInterests().get(j))){
                                flag = 1;
                            }
                        }
                        if(flag == 0){
                            chip.setVisibility(android.view.View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }

    private int getAge(String birthday) {
        // Chuyển đổi chuỗi ngày sinh thành đối tượng LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateOfBirth = LocalDate.parse(birthday, formatter);

        // Tính tuổi từ ngày sinh đến ngày hiện tại
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(dateOfBirth, currentDate).getYears();
        return age;
    }

    public static double calculateDistance(List<Double> location1, List<Double> location2) {
        final double RADIUS_OF_EARTH_KM = 6371;
        double latDistance = Math.toRadians(location2.get(1) - location1.get(1));
        double lonDistance = Math.toRadians(location2.get(0) - location1.get(0));
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(location1.get(1))) * Math.cos(Math.toRadians(location2.get(1) ))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = RADIUS_OF_EARTH_KM * c;
        return Math.round(distance * 100.0) / 100.0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("disconnection");
        mSocket.disconnect();
        Log.e("TAG","ChatActivity onDestroy");
    }

    void loadMoreMessage(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                messageList.add(0, null);
                chatAdapter.notifyItemInserted(0);
                recyclerViewChat.smoothScrollToPosition(0);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageList.remove(0);
                chatAdapter.notifyItemRemoved(0);
                page++;
                fetchData();
            }
        }, 2000);
    }
}