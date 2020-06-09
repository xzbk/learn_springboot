package com.group.entity;

public class Pet {
    private String nickName;
    private String strain;
    public String getStrain() {
        return strain;
    }
    public void setStrain(String strain) {
        this.strain = strain;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    @Override
    public String toString() {
        return "Pet{" +
                "nickName='" + nickName + '\'' +
                ", strain='" + strain + '\'' +
                '}';
    }
}
