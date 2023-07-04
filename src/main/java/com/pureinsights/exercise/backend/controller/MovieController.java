package com.pureinsights.exercise.backend.controller;

import com.pureinsights.exercise.backend.model.Movie;
import com.pureinsights.exercise.backend.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for the search endpoints
 * @author Mónica Waterhouse
 */
@Tag(name = "Movie")
@RestController("/movie")
public class MovieController {

  @Autowired
  private MovieService movieService;

  @Operation(summary = "Search movies according to a genre", description = "Executes a search of movies that include a specific genre")
  @GetMapping(value = "/searchByGenre", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Movie>> searchByGenre(@RequestParam("genre") String genre) {
    return ResponseEntity.ok(movieService.searchByGenre(genre));
  }

  @Operation(summary = "Search movies according to its rate", description = "Executes a search which of movies which rate is greater than the imputed rate")
  @GetMapping(value = "/searchByRate", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Movie>> searchByRate(@RequestParam("floorRate") double rate) {
    return ResponseEntity.ok(movieService.searchByRate(rate));
  }
}
