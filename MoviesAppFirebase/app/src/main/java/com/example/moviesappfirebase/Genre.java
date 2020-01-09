package com.example.moviesappfirebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * File Name: Movie.java
 * Author: Dheeraj Sanjay Mirashi
 * Group No: 50
 */

enum Genre {
    Action("Action"), Animation("Animation"),Comedy("Comedy"), Documentary("Documentary"),Family("Family"), Horror("Horror"),Crime("Crime"), Others("Others"),;

    private String genre;
    private static List<String> genreList = new ArrayList<>();
    private static HashMap<String,Genre> lookup = new HashMap<>();

    Genre(String genre) {
        this.genre = genre;
    }

    static {
        for (Genre genre : Genre.values()) {
            genreList.add(genre.getGenre());
            lookup.put(genre.getGenre(),genre);
        }
    }

    public static Genre get(String str){
        return lookup.get(str);
    }

    public static List<String> getGenreList(){
        return genreList;
    }

    public String getGenre() {
        return this.genre;
    }
}