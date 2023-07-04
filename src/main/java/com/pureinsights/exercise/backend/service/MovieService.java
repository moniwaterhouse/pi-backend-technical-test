package com.pureinsights.exercise.backend.service;

import com.pureinsights.exercise.backend.model.Movie;
import org.springframework.data.domain.Page;

/**
 * A search service with methods to execute queries in the imd-movies index in Elasticsearch
 * @author Mónica Waterhouse
 */
public interface MovieService {

  Page<Movie> searchByGenre(String genre);

  Page<Movie> searchByRate(double rate);
}
