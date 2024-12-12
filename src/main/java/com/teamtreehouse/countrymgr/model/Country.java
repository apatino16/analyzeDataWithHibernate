package com.teamtreehouse.countrymgr.model;

import jakarta.persistence.*;

@Entity
public class Country{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code;

    @Column(length = 32)
    private String name;

    @Column(precision = 11, scale = 8)
    private float internetUsers;

    @Column(precision = 11, scale = 8)
    private float adultLiteracyRate;

    // Default Constructor for JPA
    public Country(){}

    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", internetUsers=" + internetUsers +
                ", adultLiteracyRate=" + adultLiteracyRate +
                '}';
    }

    // Getters and Setters
    public float getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(float internetUsers) {
        this.internetUsers = internetUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(float adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }

};