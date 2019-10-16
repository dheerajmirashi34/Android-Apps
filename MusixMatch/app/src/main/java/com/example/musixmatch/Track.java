package com.example.musixmatch;

/**
 * File Name: RetrieveTracksAsync.java
 * Group Number: 50
 * Author: Jatin Narayan Gupte, Dheeraj Sanjay Mirashi
 */
public class Track {
    private String trackName;
    private String artistName;
    private String albumName;
    private String date;
    private String url;

    public Track() {

    }

    public Track(String trackName, String artistName, String albumName, String date, String url) {
        this.trackName = trackName;
        this.artistName = artistName;
        this.albumName = albumName;
        this.date = date;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getDate() {
        return date;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setDate(String date) {
        String yyyy = date.split("T")[0].split("-")[0];
        String mm = date.split("T")[0].split("-")[1];
        String dd = date.split("T")[0].split("-")[2];
        this.date = mm+"-"+dd+"-"+yyyy;
    }

    @Override
    public String toString() {
        return "Track{" +
                "trackName='" + trackName + '\'' +
                '}';
    }
}

