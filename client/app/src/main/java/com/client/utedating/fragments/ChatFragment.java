package com.client.utedating.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.client.utedating.R;
import com.client.utedating.activities.ChatActivity;
import com.client.utedating.adapters.ConversationAdapter;
import com.client.utedating.adapters.MatchedAdapter;
import com.client.utedating.events.IConversationListener;
import com.client.utedating.events.IUserListener;
import com.client.utedating.models.Conversation;
import com.client.utedating.models.ConversationModel;
import com.client.utedating.models.User;
import com.client.utedating.retrofit.ConversationApiService;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.utils.MySharedPreferences;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    RecyclerView recyclerViewMatched;
    RecyclerView recyclerViewMessage;

    User user;
    UserApiService userApiService;
    ConversationApiService conversationApiService;

    List<User> userMatchedList = new ArrayList<>();
    List<Conversation> conversationList = new ArrayList<>();

    MatchedAdapter matchedAdapter;
    ConversationAdapter conversationAdapter;

    AppCompatTextView text_count_messsage;
    TextView textViewEmptyMatched;
    LinearLayout linearLayoutEmptyMessage;

//    ShimmerFrameLayout shimmerFrameLayout;
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
        user = MySharedPreferences.getUserInfo(getActivity(), "user");
        userApiService = RetrofitClient.getInstance().create(UserApiService.class);
        conversationApiService = RetrofitClient.getInstance().create(ConversationApiService.class);

        userMatchedList.clear();
        conversationApiService.getUserMatched(user.get_id()).enqueue(new Callback<ConversationModel>() {
            @Override
            public void onResponse(Call<ConversationModel> call, Response<ConversationModel> response) {
                if (response.isSuccessful()) {
                    List<Conversation> conversations = response.body().getResult();
                    if(conversations.size()  == 0){
                        textViewEmptyMatched.setVisibility(View.VISIBLE);
                        recyclerViewMatched.setVisibility(View.GONE);
                    }
                    else{
                        textViewEmptyMatched.setVisibility(View.GONE);
                        recyclerViewMatched.setVisibility(View.VISIBLE);
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
                                            i.putExtra("receiverToken", u.getToken());
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
            }

            @Override
            public void onFailure(Call<ConversationModel> call, Throwable t) {

            }
        });

        conversationApiService.getConversationByUserId(user.get_id()).enqueue(new Callback<ConversationModel>() {
            @Override
            public void onResponse(Call<ConversationModel> call, Response<ConversationModel> response) {
                if (response.isSuccessful()) {
                    conversationList = response.body().getResult();
                    text_count_messsage.setText(conversationList.size()+"");
                    if(conversationList.size() == 0){
                        linearLayoutEmptyMessage.setVisibility(View.VISIBLE);
                        recyclerViewMessage.setVisibility(View.GONE);
                    }
                    else {
                        linearLayoutEmptyMessage.setVisibility(View.GONE);
                        recyclerViewMessage.setVisibility(View.VISIBLE);
                        conversationAdapter = new ConversationAdapter(conversationList, user.get_id(), new IConversationListener() {
                            @Override
                            public void onConversationListener(User u, String conversationId) {
                                Log.e("TAG", "onConversationListener");
                                Intent i = new Intent(view.getContext(), ChatActivity.class);
                                i.putExtra("receiverId", u.get_id());
                                i.putExtra("receiverToken", u.getToken());
                                i.putExtra("name", u.getName());
                                i.putExtra("avatar", u.getAvatar());
                                i.putExtra("conversationId", conversationId);
                                startActivity(i);
                            }
                        });
                        recyclerViewMessage.setAdapter(conversationAdapter);
                    }
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
        text_count_messsage = view.findViewById(R.id.text_count_messsage);
        textViewEmptyMatched = view.findViewById(R.id.textViewEmptyMatched);
        linearLayoutEmptyMessage = view.findViewById(R.id.linearLayoutEmptyMessage);

        LinearLayoutManager layoutManagerMatched = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMatched.setLayoutManager(layoutManagerMatched);

        LinearLayoutManager layoutManagerConversation = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewMessage.setLayoutManager(layoutManagerConversation);
    }
}
