package com.example.windows.gymapp.model;

import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by windows on 06/08/2019.
 */

public class Activity implements Serializable{

    private String id;
    private String name;
    private String id_creator;
    private String name_creator;
    private Boolean fromExercises;
    private String id_section;
    private List<Rating> ratingList = new ArrayList<>();
    private String url;
    private String explanation;


    public Activity(String name, String url, String explanation, String name_creator) {
        this.name = name;
        this.url = url;
        this.explanation = explanation;
        this.name_creator = name_creator;
    }
    public Activity(String id, String name, String id_creator, String name_creator, List<Rating> ratingList) {
        this.id = id;
        this.name = name;
        this.id_creator = id_creator;
        this.name_creator = name_creator;
        this.ratingList = ratingList;
    }

    public Activity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFromExercises() {
        return fromExercises;
    }

    public void setFromExercises(Boolean fromExercises) {
        this.fromExercises = fromExercises;
    }

    public String getId_section() {
        return id_section;
    }

    public void setId_section(String id_section) {
        this.id_section = id_section;
    }

    public String getId_creator() {
        return id_creator;
    }

    public void setId_creator(String id_creator) {
        this.id_creator = id_creator;
    }

    public String getName_creator() {
        return name_creator;
    }

    public void setName_creator(String name_creator) {
        this.name_creator = name_creator;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public boolean addRating(String userId, Integer ratingValue){
        boolean found = false;
        if(ratingList.size()!=0 && ratingList.get(0).getUserId().equals("init")){
            ratingList.remove(0);
        }
        /*for(int i=0; i<ratingList.size(); i++){
            if(ratingList.get(i).getUserId().equals(userId)){
                ratingList.remove(i);
                found = true;
            }
        }*/
        for(Rating rating: ratingList){
            if(rating.getUserId().equals(userId)){
                ratingList.remove(rating);
                found = true;
            }
        }
        ratingList.add(new Rating(userId,ratingValue));
        return found;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public  Double returnMeanRating(){
        Double result;
        Double sum = 0.0;
        for(Rating rating: ratingList){
            sum = sum + rating.getRatingValue();
        }
        if(ratingList.size()!=0 && sum != 0.0) {
            result = sum / ratingList.size();
        }else{
            result = 0.0;
        }
        return result;
    }

    public Integer returnRatingById(String userId){
        Integer rating = 0;
        for(Rating r: ratingList){
            if (r.getUserId().equals(userId)) {
                rating = r.getRatingValue();
            }
        }
        return rating;
    }
}
