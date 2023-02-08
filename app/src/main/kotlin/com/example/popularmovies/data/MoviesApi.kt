package com.example.popularmovies.data

import com.example.popularmovies.data.entity.MovieDetailsResponse
import com.example.popularmovies.data.entity.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
  @GET("/3/movie/popular")
  suspend fun getPopularMovies(
    @Query("language") language: String? = null,
    @Query("page") page: Int? = null,
    @Query("region") region: String? = null,
  ): MoviesResponse

  @GET("/3/movie/{movie_id}")
  suspend fun getMovieDetails(
    @Path("movie_id") movieId: String,
    @Query("language") language: String? = null,
  ): MovieDetailsResponse
}
