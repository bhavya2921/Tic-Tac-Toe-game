package com.example.tic_tac_toe;

public class ProductModel {
    private String username;
    private String blog;
    private String userid;


    private ProductModel() {}

    private ProductModel(String username, String blog, String userid) {

        this.username = username;
        this.blog = blog;
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getUserid() { return userid; }

    public void setUserid(String userid) { this.userid = userid; }
}
