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

    public Country(CountryBuilder builder){
        this.name = builder.name;
        this.internetUsers = builder.internetUsers;
        this.adultLiteracyRate = builder.adultLiteracyRate;
    }

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

    // builder pattern
    public static class CountryBuilder {
        private String name;
        private float internetUsers;
        private float adultLiteracyRate;

        public CountryBuilder(String name){
            this.name = name;
        }

        public CountryBuilder withInternetUsers(float internetUsers){
            this.internetUsers = internetUsers;
            return this;
        }

        public CountryBuilder withAdultLiteracyRate(float adultLiteracyRate){
            this.adultLiteracyRate = adultLiteracyRate;
            return this;
        }

        public Country build(){
            return new Country(this);
        }


    }

};