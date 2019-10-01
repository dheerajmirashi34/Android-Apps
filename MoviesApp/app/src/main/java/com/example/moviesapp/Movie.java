package com.example.moviesapp;
import androidx.annotation.NonNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * File Name: Movie.java
 * Authors: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 * Group No: 50
 */
public class Movie implements Serializable {

    String name;
    String description;
    Genre genre;
    int rating;
    int year;
    String imdb;

    public Movie(String name, String description, Genre genre, int rating, int year, String imdb) {
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.imdb = imdb;
    }

    //Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    //tostring

    @NonNull
    @Override
    public String toString() {
        return this.name+" : "+this.year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return year == movie.year &&
                name.equals(movie.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year);
    }
}

