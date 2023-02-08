package com.example.popularmovies.data

import com.example.popularmovies.data.mapper.toDomainModel
import com.example.popularmovies.domain.MoviesRepository
import com.example.popularmovies.domain.entity.MovieDetails
import com.example.popularmovies.domain.entity.MovieId
import com.example.popularmovies.domain.entity.MoviePreview
import com.example.popularmovies.schema.api.MovieServiceApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MoviesDataRepository @Inject constructor(
  private val api: MovieServiceApi,
) : MoviesRepository {
  private val _popularMovies = MutableStateFlow<List<MoviePreview>>(emptyList())
  private val _movieDetails = MutableStateFlow<Map<MovieId, MovieDetails>>(emptyMap())
  override val popularMovies: Flow<List<MoviePreview>> = _popularMovies
  override fun observeMovieDetailsById(id: MovieId): Flow<MovieDetails?> {
    return _movieDetails.map { it[id] }
  }

  override suspend fun fetchPopularMovies(
    language: String,
    page: Long,
    region: String,
  ) {
    _popularMovies.update {
      api.getPopularMovies(language, page, region).results.map { it.toDomainModel() }
    }
  }

  override suspend fun fetchMovieDetailsById(id: MovieId, language: String) {
    val details = api.getMovieDetails(id.value.toString(), language).toDomainModel()
    _movieDetails.update { it.plus(id to details) }
  }
}
