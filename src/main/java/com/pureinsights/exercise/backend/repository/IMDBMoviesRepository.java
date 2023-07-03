package com.pureinsights.exercise.backend.repository;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
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

        

        restClient.close();

    }
}
