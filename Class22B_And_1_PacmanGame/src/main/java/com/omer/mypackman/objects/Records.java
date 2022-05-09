package com.omer.mypackman.objects;

public class Records implements Comparable{
    private int score = 0;
    private String name = "";
    private double lat = 0.0;
    private double lon = 0.0;

    public Records() {
    }

    public Records(int score, String name, double lat, double lon) {
        this.score = score;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public int getScore() {
        return score;
    }

    public Records setScore(int score) {
        this.score = score;
        return this;
    }

    public String getName() {
        return name;
    }

    public Records setName(String name) {
        this.name = name;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Records setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public Records setLon(double lon) {
        this.lon = lon;
        return this;
    }

    @Override
    public int compareTo(Object o) {
        Records other = (Records) o;
        if (this.score> other.score)
            return -1;
        return 1;
    }
}
