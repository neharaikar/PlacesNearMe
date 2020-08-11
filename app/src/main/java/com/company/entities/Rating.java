package com.company.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rating implements Serializable {
    @SerializedName("rating")
    private Double rating;
    @SerializedName("user_ratings_total")
    private Integer user_ratings_total;

    public Integer getUser_ratings_total() {
        return user_ratings_total;
    }

    public void setUser_ratings_total(Integer user_ratings_total) {
        this.user_ratings_total = user_ratings_total;
    }





    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
