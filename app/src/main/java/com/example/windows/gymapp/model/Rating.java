package com.example.windows.gymapp.model;

import java.io.Serializable;

/**
 * Created by windows on 07/08/2019.
 */

public class Rating implements Serializable{
    private Integer ratingValue;
    private String userId;

    public Rating(String userId, Integer ratingValue) {
        this.ratingValue = ratingValue;
        this.userId = userId;
    }

    public Rating() {
    }

    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
