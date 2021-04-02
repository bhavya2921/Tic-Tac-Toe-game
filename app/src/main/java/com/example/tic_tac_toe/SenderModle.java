package com.example.tic_tac_toe;

public class SenderModle {

    String senderID;
    String reciverID;
    String senderState;

    public SenderModle(){

    }

    public SenderModle(String senderID,String reciverID,String senderState) {
        this.senderID = senderID;
        this.reciverID = reciverID;
        this.senderState = senderState;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
    public String getReciverID() {
        return reciverID;
    }

    public void setReciverID(String reciverID) {
        this.reciverID = reciverID;
    }

    public String getSenderState() {
        return senderState;
    }

    public void setSenderState(String senderState) {
        this.senderState = senderState;
    }
}
