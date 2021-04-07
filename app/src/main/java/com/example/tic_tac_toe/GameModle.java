package com.example.tic_tac_toe;

import java.security.PublicKey;

public class GameModle {
    String username1;
    String username2;
    String button00;
    String button01;
    String button02;
    String button10;
    String button11;
    String button12;
    String button20;
    String button21;
    String button22;

    public GameModle(){

    }

    public GameModle(String username1, String username2, String button00, String button01, String button02, String button10, String button11, String button12, String button20, String button21, String button22) {
        this.username1 = username1;
        this.username2 = username2;
        this.button00 = button00;
        this.button01 = button01;
        this.button02 = button02;
        this.button10 = button10;
        this.button11 = button11;
        this.button12 = button12;
        this.button20 = button20;
        this.button21 = button21;
        this.button22 = button22;
    }

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getButton00() {
        return button00;
    }

    public void setButton00(String button00) {
        this.button00 = button00;
    }

    public String getButton01() {
        return button01;
    }

    public void setButton01(String button01) {
        this.button01 = button01;
    }

    public String getButton02() {
        return button02;
    }

    public void setButton02(String button02) {
        this.button02 = button02;
    }

    public String getButton10() {
        return button10;
    }

    public void setButton10(String button10) {
        this.button10 = button10;
    }

    public String getButton11() {
        return button11;
    }

    public void setButton11(String button11) {
        this.button11 = button11;
    }

    public String getButton12() {
        return button12;
    }

    public void setButton12(String button12) {
        this.button12 = button12;
    }

    public String getButton20() {
        return button20;
    }

    public void setButton20(String button20) {
        this.button20 = button20;
    }

    public String getButton21() {
        return button21;
    }

    public void setButton21(String button21) {
        this.button21 = button21;
    }

    public String getButton22() {
        return button22;
    }

    public void setButton22(String button22) {
        this.button22 = button22;
    }
}
