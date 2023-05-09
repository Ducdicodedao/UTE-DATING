package com.client.utedating.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.client.utedating.R;
import com.client.utedating.activities.ChatActivity;
import com.client.utedating.activities.MainActivity;
import com.client.utedating.adapters.ConversationAdapter;
import com.client.utedating.adapters.LikeAdapter;
import com.client.utedating.adapters.MatchedAdapter;
import com.client.utedating.adapters.MessageListAdapter;
import com.client.utedating.events.IConversationListener;
import com.client.utedating.events.IUserListener;
import com.client.utedating.models.Conversation;
import com.client.utedating.models.ConversationModel;
import com.client.utedating.models.Like;
import com.client.utedating.models.MessageItem;
import com.client.utedating.models.MessageModel;
import com.client.utedating.models.User;
import com.client.utedating.models.UsersLikedModel;
import com.client.utedating.models.UsersMatchedModel;
import com.client.utedating.retrofit.ConversationApiService;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
//    View rootLayout;
//    private static final String TAG = MainActivity.class.getSimpleName();
//    private List<MessageItem> messageList;
//    private List<Like> likeList;
//    private MessageListAdapter mAdapter;
//    private String[] messages = {"Ah d'accord", "Juste par habitude en tout cas", "Hey!", "6946743263", "Give me your number, I will call you"};
//    private int[] counts = {0, 3, 0, 0, 1};
//    private int[] messagePictures = {R.drawable.user_woman_3, R.drawable.user_woman_4, R.drawable.user_woman_5, R.drawable.user_woman_6 , R.drawable.user_woman_7};
//    private int[] likePictures = {R.drawable.user_woman_1, R.drawable.user_woman_2};
//    private String[] messageNames = {"Fanelle", "Chloe", "Cynthia", "Kate", "Angele"};
//    private String[] likeNames = {"Sophie", "Clara"};

    RecyclerView recyclerViewMatched;
    RecyclerView recyclerViewMessage;

    SharedPreferencesClient sharedPreferencesClient;
    User user;
    UserApiService userApiService;
    ConversationApiService conversationApiService;

    List<User> userMatchedList = new ArrayList<>();
    List<Conversation> conversationList = new ArrayList<>();

    MatchedAdapter matchedAdapter;
    ConversationAdapter conversationAdapter;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);

//        RecyclerView recyclerView = rootLayout.findViewById(R.id.recycler_view_messages);
//        messageList = new ArrayList<>();
//        mAdapter = new MessageListAdapter(getContext(), messageList);
//
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
//        recyclerView.setAdapter(mAdapter);
//
//        prepareMessageList();
//
//
//        prepareContactList();
//        LikeAdapter contactAdapter = new LikeAdapter(getContext(), likeList);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        RecyclerView recyclerViewContact =  rootLayout.findViewById(R.id.recycler_view_likes);
//        recyclerViewContact.setLayoutManager(layoutManager);
//        recyclerViewContact.setAdapter(contactAdapter);
//        //new HorizontalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerViewContact));
//
//
//        return rootLayout;
    }

//
//    private void prepareMessageList(){
//
//        Random rand = new Random();
//        int id = rand.nextInt(100);
//        int i;
//        for(i=0; i<5; i++) {
//            MessageItem message = new MessageItem(id, messageNames[i], messages[i], counts[i], messagePictures[i]);
//            messageList.add(message);
//        }
//    }
//
//    private void prepareContactList(){
//        likeList = new ArrayList<>();
//        Random rand = new Random();
//        int id = rand.nextInt(100);
//        int i;
//        for(i=0; i<2; i++) {
//            Like like = new Like(id, likeNames[i], likePictures[i]);
//            likeList.add(like);
//        }
//    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("TAG", "ChatFragment onViewCreated");
        initView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "ChatFragment onResume");
        fetchData(getView());
    }

    private void fetchData(View view) {
        sharedPreferencesClient = new SharedPreferencesClient(view.getContext());
        user = sharedPreferencesClient.getUserInfo("user");
        userApiService = RetrofitClient.getInstance().create(UserApiService.class);
        conversationApiService = RetrofitClient.getInstance().create(ConversationApiService.class);

//        userApiService.getUserMatched(user.get_id()).enqueue(new Callback<UsersMatchedModel>() {
//            @Override
//            public void onResponse(Call<UsersMatchedModel> call, Response<UsersMatchedModel> response) {
//                if (response.isSuccessful()) {
//                    userMatchedList = response.body().getResult().getUserMatched();
//
//                    matchedAdapter = new MatchedAdapter(userMatchedList, new IUserListener() {
//                        @Override
//                        public void onUserListener(User u) {
//                            conversationApiService.getConversationIdByUserId(user.get_id(), u.get_id()).enqueue(new Callback<String>() {
//                                @Override
//                                public void onResponse(Call<String> call, Response<String> response) {
//                                    if(response.isSuccessful()){
//                                        Intent i = new Intent(view.getContext(), ChatActivity.class);
//                                        i.putExtra("receiverId", u.get_id());
//                                        i.putExtra("name", u.getName());
//                                        i.putExtra("avatar", u.getAvatar());
//                                        i.putExtra("conversationId", response.body());
//                                        startActivity(i);
//                                    }
//                                }
//                                @Override
//                                public void onFailure(Call<String> call, Throwable t) {
//                                    Log.e("TAG",t.getMessage() );
//                                }
//                            });
//                        }
//                    });
//                    recyclerViewMatched.setAdapter(matchedAdapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UsersMatchedModel> call, Throwable t) {
//                Log.e("TAG", t.getMessage());
//            }
//        });
        userMatchedList.clear();
        conversationApiService.getUserMatched(user.get_id()).enqueue(new Callback<ConversationModel>() {
            @Override
            public void onResponse(Call<ConversationModel> call, Response<ConversationModel> response) {
                if (response.isSuccessful()) {
                    List<Conversation> conversations = response.body().getResult();
                    for (Conversation conversation : conversations) {
                        userMatchedList.add(conversation.getParticipants().get(0));
                    }
                    matchedAdapter = new MatchedAdapter(userMatchedList, new IUserListener() {
                        @Override
                        public void onUserListener(User u) {
                            conversationApiService.getConversationIdByUserId(user.get_id(), u.get_id()).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.isSuccessful()) {
                                        Intent i = new Intent(view.getContext(), ChatActivity.class);
                                        i.putExtra("receiverId", u.get_id());
                                        i.putExtra("name", u.getName());
                                        i.putExtra("avatar", u.getAvatar());
                                        i.putExtra("conversationId", response.body());
                                        startActivity(i);
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.e("TAG", t.getMessage());
                                }
                            });
                        }
                    });
                    recyclerViewMatched.setAdapter(matchedAdapter);
                }
            }

            @Override
            public void onFailure(Call<ConversationModel> call, Throwable t) {

            }
        });

        conversationApiService.getConversationByUserId(user.get_id()).enqueue(new Callback<ConversationModel>() {
            @Override
            public void onResponse(Call<ConversationModel> call, Response<ConversationModel> response) {
                if (response.isSuccessful()) {
                    Log.e("TAG", response.body().getMessage());
                    conversationList = response.body().getResult();
                    Log.e("TAG", conversationList.size() + "");
                    conversationAdapter = new ConversationAdapter(conversationList, user.get_id(), new IConversationListener() {
                        @Override
                        public void onConversationListener(User u, String conversationId) {
                            Log.e("TAG", "onConversationListener");
                            Intent i = new Intent(view.getContext(), ChatActivity.class);
                            i.putExtra("receiverId", u.get_id());
                            i.putExtra("name", u.getName());
                            i.putExtra("avatar", u.getAvatar());
                            i.putExtra("conversationId", conversationId);
                            startActivity(i);
                        }
                    });
                    recyclerViewMessage.setAdapter(conversationAdapter);
                }
            }

            @Override
            public void onFailure(Call<ConversationModel> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }

    private void initView(View view) {
        recyclerViewMatched = view.findViewById(R.id.recyclerViewMatched);
        recyclerViewMessage = view.findViewById(R.id.recyclerViewMessages);

        LinearLayoutManager layoutManagerMatched = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMatched.setLayoutManager(layoutManagerMatched);

        LinearLayoutManager layoutManagerConversation = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewMessage.setLayoutManager(layoutManagerConversation);
    }
}
