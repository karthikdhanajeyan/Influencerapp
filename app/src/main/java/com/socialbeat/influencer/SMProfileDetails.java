package com.socialbeat.influencer;


public class SMProfileDetails {

    private String name;
    private String link;
    private String profile_image;
    private String key1;
    private String key2;
    private String key3;
    private String key1_text;
    private String key2_text;
    private String key3_text;
    private String socialmedia;




    public SMProfileDetails() {

        this.name=name;
        this.link=link;
        this.profile_image=profile_image;
        this.key1=key1;
        this.key2=key2;
        this.key3=key3;
        this.key1_text=key1_text;
        this.key2_text=key2_text;
        this.key3_text=key3_text;
        this.socialmedia=socialmedia;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getLink() { return link; }

    public void setLink(String link) {this.link = link;}


    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }


    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }


    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

//
//    public String getKey3() {
//        return key3;
//    }
//
//    public void setKey3(String key3) {
//        this.key3 = key3;
//    }
//
//
    public String getKey1_text() {
        return key1_text;
    }

    public void setKey1_text(String key1_text) {
        this.key1_text = key1_text;
    }


    public String getKey2_text() {
        return key2_text;
    }

    public void setKey2_text(String key2_text) {
        this.key2_text = key2_text;
    }


//    public String getKey3_text() {
//        return key3_text;
//    }
//
//    public void setKey3_text(String key3_text) {
//        this.key3_text = key3_text;
//    }


    public String getSocialmedia() {
        return socialmedia;
    }

    public void setSocialmedia(String socialmedia) {
        this.socialmedia = socialmedia;
    }
}