package com.pureinsights.exercise.backend.repository;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.pureinsights.exercise.backend.model.Movie;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class IMDBMoviesRepository {

    public static void main(String[] args) throws IOException {

        int searchMaxSize = 10000;

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the movie genre: ");
        String genre= scanner.nextLine();

        double rate = 0.0;
        boolean isValidInput = false;

        while (!isValidInput) {
            System.out.print("Enter rate: ");

            if (scanner.hasNextDouble()) {
                rate = scanner.nextDouble();
                isValidInput = true;
            } else {
                System.out.println("Invalid input! Please enter a valid double for rate.");
                scanner.nextLine();  // Clear the input buffer
            }
        }

// Remember to close the scanner when you're done with it
        scanner.close();

        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);


        SearchResponse<Movie> genreMovies = client.search(s -> s
                        .index("imdb-movies")
                        .query(q -> q
                                .wildcard(t -> t
                                        .field("genre")
                                        .wildcard("*"+genre+"*")
                                )
                        )
                        .size(searchMaxSize),
                Movie.class
        );


        List<Hit<Movie>> genreMovieList = genreMovies.hits().hits();

        int count = 1;

        System.out.println("List of "+genre+" movies:");
        for(Hit<Movie> genreMovie: genreMovieList){
            Movie movie = genreMovie.source();
            System.out.println(count + ". " + movie.name);
            count++;
        }

        Query byNoRateLabel = MatchQuery.of(m -> m
                .field("rate")
                .query("No Rate")
        )._toQuery();

// Search by max price
        double finalRate = rate;
        Query byRate = RangeQuery.of(r -> r
                .field("rate")
                .gt(JsonData.of(finalRate))
        )._toQuery();

// Combine name and price queries to search the product index
        SearchResponse<Movie> response = client.search(s -> s
                        .index("imdb-movies")
                        .query(q -> q
                                .bool(b -> b
                                        .must(byRate)
                                        .mustNot(byNoRateLabel)
                                )
                        ).size(searchMaxSize),
                Movie.class
        );

        List<Hit<Movie>> reatedMoviesList = response.hits().hits();

        count = 1;

        System.out.println("List of movies with rate greater than "+rate+":");
        for(Hit<Movie> reatedMovieHit: reatedMoviesList){
            Movie ratedMovie = reatedMovieHit.source();
            System.out.println(count + ". " + ratedMovie.name + " - Rate: " + ratedMovie.rate);
            count++;
        }

        restClient.close();

    }
}
