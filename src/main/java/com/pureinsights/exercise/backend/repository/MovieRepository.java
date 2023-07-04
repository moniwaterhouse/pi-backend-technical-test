package com.pureinsights.exercise.backend.repository;

import com.pureinsights.exercise.backend.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

/**
 * Repository for {@link Movie} entities
 * @author Andres Marenco
 */
public interface MovieRepository {
  /**
   * @param query the query to execute
   * @return a page with the results of the search
   */
  Page<Movie> searchByGenre(String query);
  Page<Movie> searchByRate(double query);

}
