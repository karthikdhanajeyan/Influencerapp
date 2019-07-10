package com.socialbeat.influencer;


public class Influencer_AppliedCamp_Declare {

    private String cid;
    private String campid;
    private String campImg;
    private String campname;
    private String campappliedstatus;
    private String campaignquote;
    private String campapplieddate;
    private String campdeliverystatus;
    private String camppaymentstatus;



    public Influencer_AppliedCamp_Declare() {
        this.cid=cid;
        this.campid=campid;
        this.campImg=campImg;
        this.campname=campname;
        this.campappliedstatus=campappliedstatus;
        this.campaignquote=campaignquote;
        this.campapplieddate=campapplieddate;
        this.campdeliverystatus=campdeliverystatus;
        this.camppaymentstatus=camppaymentstatus;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCampid() {
        return campid;
    }

    public void setCampid(String campid) {
        this.campid = campid;
    }

    public String getCampImg() { return campImg; }

    public void setCampImg(String campImg) {this.campImg = campImg;}

    public String getCampname() {
        return campname;
    }

    public void setCampname(String campname) {
        this.campname = campname;
    }


    public String getCampappliedstatus() {
        return campappliedstatus;
    }

    public void setCampappliedstatus(String campappliedstatus) { this.campappliedstatus = campappliedstatus; }


    public String getCampaignquote() {
        return campaignquote;
    }

    public void setCampaignquote(String campaignquote) {
        this.campaignquote = campaignquote;
    }


    public String getCampapplieddate() {
        return campapplieddate;
    }

    public void setCampapplieddate(String campapplieddate) { this.campapplieddate = campapplieddate; }


    public String getCampdeliverystatus() {
        return campdeliverystatus;
    }

    public void setCampdeliverystatus(String campdeliverystatus) { this.campdeliverystatus = campdeliverystatus; }


    public String getCamppaymentstatus() {
        return camppaymentstatus;
    }

    public void setCamppaymentstatus(String camppaymentstatus) { this.camppaymentstatus = camppaymentstatus; }
}