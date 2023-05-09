package com.client.utedating.events;

import com.client.utedating.models.User;

public interface IConversationListener {
    void onConversationListener(User user, String conversationId);

}
