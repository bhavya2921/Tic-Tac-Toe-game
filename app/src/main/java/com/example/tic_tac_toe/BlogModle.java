package com.example.tic_tac_toe;

public class BlogModle {

    String username;
    String userBlog;
    String userID;

    public BlogModle(){
    }

    public BlogModle(String username,String userBlog,String userID) {
        this.username = username;
        this.userBlog = userBlog;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserBlog() {
        return userBlog;
    }

    public void setUserBlog(String userBlog) {
        this.userBlog = userBlog;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
