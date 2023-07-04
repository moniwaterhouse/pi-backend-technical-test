package com.pureinsights.exercise.backend.service;
import com.pureinsights.exercise.backend.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link MovieService}
 * @author Andres Marenco
 */
@Service
public class MovieServiceImpl implements MovieService {


  @Override
  public Page<Movie> searchByGenre(String query) {
    return null;
  }

  @Override
  public Page<Movie> searchByRate(String query) {
    return null;
  }
}
