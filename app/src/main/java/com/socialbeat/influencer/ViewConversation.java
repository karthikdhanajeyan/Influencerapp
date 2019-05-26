package com.socialbeat.influencer;


public class ViewConversation {
    private String id_post_conversation;
    private String message;
    private String conversation_author;
    private String conversation_thumbnail_url;
    private String added_date;

    private String id_post_attach;
    private String file_name;
    private String file_location;
    private String thumbnail_url;
    private String approved_status;

    ViewConversation() {

        this.id_post_conversation=id_post_conversation;
        this.message=message;
        this.conversation_author=conversation_author;
        this.conversation_thumbnail_url=conversation_thumbnail_url;
        this.added_date=added_date;
        this.id_post_attach=id_post_attach;
        this.file_name=file_name;
        this.file_location=file_location;
        this.thumbnail_url=thumbnail_url;
        this.approved_status=approved_status;

    }

    public String getId_post_conversation() {
        return id_post_conversation;
    }

    public void setId_post_conversation(String id_post_conversation) {
        this.id_post_conversation = id_post_conversation;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getConversation_thumbnail_url() {
        return conversation_thumbnail_url;
    }

    public void setConversation_thumbnail_url(String conversation_thumbnail_url) {
        this.conversation_thumbnail_url = conversation_thumbnail_url;
    }

    public String getConversation_author() {
        return conversation_author;
    }

    public void setConversation_author(String conversation_author) {
        this.conversation_author = conversation_author;
    }


    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }


    public String getId_post_attach() {
        return id_post_attach;
    }

    public void setId_post_attach(String id_post_attach) {
        this.id_post_attach = id_post_attach;
    }


    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }


    public String getFile_location() {
        return file_location;
    }

    public void setFile_location(String file_location) { this.file_location = file_location; }


    public String gethumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) { this.thumbnail_url = thumbnail_url; }


    public String getApproved_status() {
        return approved_status;
    }

    public void setApproved_status(String approved_status) {
        this.approved_status = approved_status;
    }



}