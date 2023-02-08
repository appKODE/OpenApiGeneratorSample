package com.example.popularmovies.domain

import com.example.popularmovies.domain.entity.MovieDetails
import com.example.popularmovies.domain.entity.MovieId
import com.example.popularmovies.domain.entity.MoviePreview
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
  val popularMovies: Flow<List<MoviePreview>>
  fun observeMovieDetailsById(id: MovieId): Flow<MovieDetails?>
  suspend fun fetchPopularMovies(language: String, page: Long, region: String)
  suspend fun fetchMovieDetailsById(id: MovieId, language: String)
}
