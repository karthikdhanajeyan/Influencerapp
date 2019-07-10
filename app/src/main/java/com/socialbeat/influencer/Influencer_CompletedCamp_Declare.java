package com.socialbeat.influencer;


public class Influencer_CompletedCamp_Declare {

    private String campid;
    private String campImg;
    private String campname;
    private String status;
    private String completeddate;
    private String campbrief;
    private String paymentstatus;
    private String transactionstatus;
    private String transactionid;
    private String amount;
    private String date;


    public Influencer_CompletedCamp_Declare() {

        this.campid=campid;
        this.campImg=campImg;
        this.campname=campname;
        this.status=status;
        this.completeddate=completeddate;
        this.campbrief=campbrief;
        this.paymentstatus=paymentstatus;
        this.transactionstatus=transactionstatus;
        this.transactionid=transactionid;
        this.amount=amount;
        this.date=date;
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


    public String getcompleteddate() {
        return completeddate;
    }

    public void setcompleteddate(String completeddate) {
        this.completeddate = completeddate;
    }


    public String getCampbrief() {
        return campbrief;
    }

    public void setCampbrief(String campbrief) { this.campbrief = campbrief; }


    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }


    public String getTransactionstatus() {
        return transactionstatus;
    }

    public void setTransactionstatus(String transactionstatus) { this.transactionstatus = transactionstatus; }


    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}