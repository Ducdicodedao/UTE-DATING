package com.client.utedating.models;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String _id;
    private String name;
    private String email;
    private String avatar;
    private String birthday;
    private String gender;
    private String dateWith;
    private String about;
    private String facebook;
    private String faculty;
    private List<String> interests;
    private List<String> userMatched;
    private List<String> userSwipedRight;
    private Boolean isAuthenticated;
    private Locat location;

    public User() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateWith() {
        return dateWith;
    }

    public void setDateWith(String dateWith) {
        this.dateWith = dateWith;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getUserMatched() {
        return userMatched;
    }

    public void setUserMatched(List<String> userMatched) {
        this.userMatched = userMatched;
    }

    public List<String> getUserSwipedRight() {
        return userSwipedRight;
    }

    public void setUserSwipedRight(List<String> userSwipedRight) {
        this.userSwipedRight = userSwipedRight;
    }

    public Boolean getAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public Locat getLocation() {
        return location;
    }

    public void setLocation(Locat location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender='" + gender + '\'' +
                ", dateWith='" + dateWith + '\'' +
                ", about='" + about + '\'' +
                ", facebook='" + facebook + '\'' +
                ", faculty='" + faculty + '\'' +
                ", interests=" + interests +
                ", userMatched=" + userMatched +
                ", userSwipedRight=" + userSwipedRight +
                ", isAuthenticated=" + isAuthenticated +
                ", location=" + location +
                '}';
    }
}
