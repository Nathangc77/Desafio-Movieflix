package com.devsuperior.movieflix.dto;

public class ReviewMinDTO {

    private String userName;
    private String text;

    public ReviewMinDTO(String userName, String text) {
        this.userName = userName;
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }
}
