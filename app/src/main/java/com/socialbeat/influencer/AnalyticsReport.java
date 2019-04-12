package com.socialbeat.influencer;


public class AnalyticsReport {



    private String campid;
    private String campname;
    private String contentid;
    private String social_media;
    private String posted_date;
    private String posted_link;
    private String from_date;
    private String to_date;
    private String status;
    private String reach;
    private String reach_attach;
    private String engagement;
    private String engage_attach;


    public AnalyticsReport() {

        this.campid=campid;
        this.campname=campname;
        this.contentid=contentid;
        this.social_media=social_media;
        this.posted_date=posted_date;
        this.posted_link=posted_link;
        this.from_date=from_date;
        this.to_date=to_date;
        this.status=status;
        this.reach=reach;
        this.reach_attach=reach_attach;
        this.engagement=engagement;
        this.engage_attach=engage_attach;
    }

    public String getCampid() {
        return campid;
    }

    public void setCampid(String campid) {
        this.campid = campid;
    }


    public String getCampname() {
        return campname;
    }

    public void setCampname(String campname) {
        this.campname = campname;
    }


    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid;
    }


    public String getSocial_media() {
        return social_media;
    }

    public void setSocial_media(String social_media) {
        this.social_media = social_media;
    }


    public String getPosted_date() {
        return posted_date;
    }

    public void setPosted_date(String posted_date) {
        this.posted_date = posted_date;
    }


    public String getPosted_link() {
        return posted_link;
    }

    public void setPosted_link(String posted_link) {
        this.posted_link = posted_link;
    }


    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) { this.from_date = from_date; }


    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) { this.status = status; }


    public String getReach() {
        return reach;
    }

    public void setReach(String reach) { this.reach = reach; }


    public String getReach_attach() {
        return reach_attach;
    }

    public void setReach_attach(String reach_attach) {
        this.reach_attach = reach_attach;
    }


    public String getEngagement() {
        return engagement;
    }

    public void setEngagement(String engagement) {
        this.engagement = engagement;
    }


    public String getEngage_attach() {
        return engage_attach;
    }

    public void setEngage_attach(String engage_attach) {
        this.engage_attach = engage_attach;
    }
}