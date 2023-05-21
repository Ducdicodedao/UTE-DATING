package com.client.utedating.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Profile {
    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url")
    @Expose
    private String imageUrl;

    @SerializedName("age")
    @Expose
    private Integer age;

    @SerializedName("faculty")
    @Expose
    private String faculty;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("about")
    @Expose
    private String about;

    @SerializedName("interests")
    @Expose
    private List<String> interests;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("token")
    @Expose
    private String token;

    public Profile(String _id, String name, String imageUrl, Integer age, String faculty, String location, String about, List<String> interests, String gender, String token) {
        this._id = _id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.age = age;
        this.faculty = faculty;
        this.location = location;
        this.about = about;
        this.interests = interests;
        this.gender = gender;
        this.token = token;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}