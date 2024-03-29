package com.example.tic_tac_toe;

import java.security.PrivateKey;
import java.util.ArrayList;

public class ProductModel {
    private String username;
    private String blog;
    private String userid;
    private String blogid;
    private String noOfLike;
    private ArrayList likedby;


    private ProductModel() {}

    private ProductModel(String username, String blog, String userid,String blogid,String noOfLike,ArrayList likedby) {

        this.username = username;
        this.blog = blog;
        this.userid = userid;
        this.blogid = blogid;
        this.noOfLike = noOfLike;
        this.likedby = likedby;
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

    public String getBlogid() {
        return blogid;
    }

    public void setBlogid(String blogid) {
        this.blogid = blogid;
    }

    public String getNoOfLike() { return noOfLike; }

    public void setNoOfLike(String noOfLike) {
        this.noOfLike = noOfLike;
    }

    public ArrayList getLikedby() {
        return likedby;
    }

    public void setLikedby(ArrayList likedby) {
        this.likedby = likedby;
    }
}
