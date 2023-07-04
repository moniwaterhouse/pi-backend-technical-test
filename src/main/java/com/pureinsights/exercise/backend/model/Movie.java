package com.pureinsights.exercise.backend.model;

/**
 * Definition for a Movie entity
 * @author Mónica Waterhouse
 */

public class Movie {

  public String alcohol;
  public String certificate;
  public int date;
  public String duration;
  public String episodes;
  public String frightening;
  public String genre;
  public String name;
  public String nudity;
  public String profanity;
  public String rate;
  public String type;
  public String violence;
  public String votes;

  public Movie() {}
  public Movie(String alcohol, String certificate, int date, String duration, String episodes, String frightening,
               String genre, String name, String nudity, String profanity, String rate, String type, String violence, String votes) {
    this.alcohol = alcohol;
    this.certificate = certificate;
    this.date = date;
    this.duration = duration;
    this.episodes = episodes;
    this.frightening = frightening;
    this.genre = genre;
    this.name = name;
    this.nudity = nudity;
    this.profanity = profanity;
    this.rate = rate;
    this.type = type;
    this.violence = violence;
    this.votes = votes;
  }

}
