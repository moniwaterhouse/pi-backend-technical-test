package com.pureinsights.exercise.backend.service;

import com.pureinsights.exercise.backend.model.Movie;
import org.springframework.data.domain.Page;

/**
 * A search service with methods to execute queries in the movie collection
 * @author Mónica Waterhouse
 */
public interface MovieService {

  Page<Movie> searchByGenre(String query);

  Page<Movie> searchByRate(String query);
}
