package com.example.lx.aidldemo.bean;

public class User {
    public int userId;
    public String userName;
    public boolean isMale;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", isMale=" + isMale +
                '}';
    }
}
