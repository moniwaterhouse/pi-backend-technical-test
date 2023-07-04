package com.pureinsights.exercise.backend.repository;

import com.pureinsights.exercise.backend.model.Movie;
import org.springframework.data.domain.Page;

/**
 * Repository for {@link Movie} entities
 * @author Mónica Waterhouse
 */
public interface MovieRepository {
  /**
   * @param genre the genre to filter the movies
   * @return a page with the movies filtered by the given gender
   */
  Page<Movie> searchByGenre(String genre);

  /**
   * @param rate the floor rate to filter the movies
   * @return a page with the movies which rate is greater than the floor rate
   */
  Page<Movie> searchByRate(double rate);

}
