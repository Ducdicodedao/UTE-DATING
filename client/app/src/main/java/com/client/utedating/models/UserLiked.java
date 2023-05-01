package com.client.utedating.models;

import java.util.List;

public class UserLiked {
    private String _id;
    private List<User> userSwipedRight;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<User> getUserSwipedRight() {
        return userSwipedRight;
    }

    public void setUserSwipedRight(List<User> userSwipedRight) {
        this.userSwipedRight = userSwipedRight;
    }
}
