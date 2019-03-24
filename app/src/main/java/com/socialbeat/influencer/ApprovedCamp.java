package com.socialbeat.influencer;


public class ApprovedCamp {

    private String campid;
    private String campImg;
    private String campname;
    private String status;
    private String approveddate;
    private String campbrief;



    public ApprovedCamp() {

        this.campid=campid;
        this.campImg=campImg;
        this.campname=campname;
        this.status=status;
        this.approveddate=approveddate;
        this.campbrief=campbrief;
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getApproveddate() {
        return approveddate;
    }

    public void setApproveddate(String approveddate) {
        this.approveddate = approveddate;
    }


    public String getCampbrief() {
        return campbrief;
    }

    public void setCampbrief(String campbrief) {
        this.campbrief = campbrief;
    }
}