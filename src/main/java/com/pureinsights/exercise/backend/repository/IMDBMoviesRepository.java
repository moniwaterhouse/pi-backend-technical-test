package com.pureinsights.exercise.backend.repository;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.pureinsights.exercise.backend.model.Movie;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.List;

public class IMDBMoviesRepository {

    public static void main(String[] args) throws IOException {
        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);


        SearchResponse<Movie> actionMovies = client.search(s -> s
                        .index("imdb-movies")
                        .query(q -> q
                                .wildcard(t -> t
                                        .field("genre")
                                        .wildcard("*Action*")
                                )
                        )
                        .size(2000),
                Movie.class
        );

        TotalHits totalActionMovies = actionMovies.hits().total();

        System.out.println("Number of action movies: " + totalActionMovies.value());

        System.out.println(actionMovies.hits());


        List<Hit<Movie>> actionMoviesList = actionMovies.hits().hits();

        int count = 1;

        System.out.println("List of action movies:");
        for(Hit<Movie> actionMovie: actionMoviesList){
            Movie movie = actionMovie.source();
            System.out.println(count + ". " + movie.name);
            count++;
        }

        Query byName = MatchQuery.of(m -> m
                .field("rate")
                .query("No Rate")
        )._toQuery();

// Search by max price
        Query byMaxPrice = RangeQuery.of(r -> r
                .field("rate")
                .gt(JsonData.of(8.0))
        )._toQuery();

// Combine name and price queries to search the product index
        SearchResponse<Movie> response = client.search(s -> s
                        .index("imdb-movies")
                        .query(q -> q
                                .bool(b -> b
                                        .must(byMaxPrice)
                                        .mustNot(byName)
                                )
                        ).size(2000),
                Movie.class
        );

        List<Hit<Movie>> moviesList = response.hits().hits();

        count = 1;

        System.out.println("List of movies with rate greater than 8.0:");
        for(Hit<Movie> ratedMovie: moviesList){
            Movie otherMovie = ratedMovie.source();
            System.out.println(count + ". " + otherMovie.name + " - Rate: " + otherMovie.rate);
            count++;
        }

        restClient.close();

    }
}
