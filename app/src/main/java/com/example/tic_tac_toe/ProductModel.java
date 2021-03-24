package com.example.tic_tac_toe;

public class ProductModel {
    private String username;

    private ProductModel() {}

    private ProductModel(String username) {
         this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
