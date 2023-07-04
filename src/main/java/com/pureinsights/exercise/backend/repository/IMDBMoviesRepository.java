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
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of {@link MovieRepository}
 * @author Mónica Waterhouse
 */
@Repository
@Slf4j
public class IMDBMoviesRepository implements MovieRepository, Closeable {

    private static final int MAX_SEARCH_COUNT = 10000;

    // Create the low-level client
    RestClient restClient = RestClient.builder(
            new HttpHost("localhost", 9200)).build();

    // Create the transport with a Jackson mapper
    ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

    // And create the API client
    ElasticsearchClient client = new ElasticsearchClient(transport);


    @PostConstruct
    void init() {

    }


    @Override
    public Page<Movie> searchByGenre(String query) {
        SearchResponse<Movie> movies = null;
        try {
            movies = client.search(s -> s
                            .index("imdb-movies")
                            .query(q -> q
                                    .wildcard(t -> t
                                            .field("genre")
                                            .wildcard("*"+query+"*")
                                    )
                            )
                            .size(MAX_SEARCH_COUNT),
                    Movie.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        List<Hit<Movie>> movieHits = movies.hits().hits();
        List<Movie> moviesList = new ArrayList<>();

        for(Hit<Movie> movieHit: movieHits){
            Movie movie = movieHit.source();
            moviesList.add(movie);
        }

        return new PageImpl<>(moviesList);
    }

    @Override
    public Page<Movie> searchByRate(double query) {
        Query byNoRateLabel = MatchQuery.of(m -> m
                .field("rate")
                .query("No Rate")
        )._toQuery();

// Search by max price
        Query byRate = RangeQuery.of(r -> r
                .field("rate")
                .gt(JsonData.of(query))
        )._toQuery();

// Combine name and price queries to search the product index
        SearchResponse<Movie> movies = null;
        try {
            movies = client.search(s -> s
                            .index("imdb-movies")
                            .query(q -> q
                                    .bool(b -> b
                                            .must(byRate)
                                            .mustNot(byNoRateLabel)
                                    )
                            ).size(MAX_SEARCH_COUNT),
                    Movie.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Hit<Movie>> movieHits = movies.hits().hits();
        List<Movie> moviesList = new ArrayList<>();

        for(Hit<Movie> movieHit: movieHits){
            Movie movie = movieHit.source();
            moviesList.add(movie);
        }

        return new PageImpl<>(moviesList);
    }

    @Override
    public void close() throws IOException {
        restClient.close();
    }


}
