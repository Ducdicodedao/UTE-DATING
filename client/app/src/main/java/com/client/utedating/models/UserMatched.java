package com.client.utedating.models;

import java.util.List;

public class UserMatched {
    private String _id;
    private List<User>   userMatched;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<User> getUserMatched() {
        return userMatched;
    }

    public void setUserMatched(List<User> userMatched) {
        this.userMatched = userMatched;
    }
}
