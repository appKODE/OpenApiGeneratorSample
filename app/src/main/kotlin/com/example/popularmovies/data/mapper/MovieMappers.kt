package com.example.popularmovies.data.mapper

import com.example.popularmovies.schema.model.MovieDetails as NetworkMovieDetails
import com.example.popularmovies.schema.model.MoviePreview as NetworkMoviePreview
import com.example.popularmovies.domain.entity.MovieDetails
import com.example.popularmovies.domain.entity.MovieId
import com.example.popularmovies.domain.entity.MoviePreview
import com.example.popularmovies.domain.entity.MovieRating
import com.example.popularmovies.domain.entity.Url
import java.time.Duration

fun NetworkMoviePreview.toDomainModel(): MoviePreview {
  return MoviePreview(
    id = MovieId(id),
    localizedTitle = title.orEmpty(),
    originalTitle = originalTitle.orEmpty(),
    posterUrl = posterPath?.let { Url("$POSTER_BASE_URL$it") },
    releaseDate = releaseDate,
    rating = if (voteAverage != null && voteCount != null) {
      MovieRating(
        average = voteAverage.toFloat(),
        voteCount = voteCount
      )
    } else {
      null
    }
  )
}

fun NetworkMovieDetails.toDomainModel(): MovieDetails {
  return MovieDetails(
    id = MovieId(id),
    localizedTitle = title.orEmpty(),
    originalTitle = originalTitle.orEmpty(),
    overview = overview,
    posterUrl = posterPath?.let { Url("$POSTER_BASE_URL$it") },
    releaseDate = releaseDate,
    budgetUsd = budget?.takeIf { it.amount != 0L },
    revenueUsd = revenue?.takeIf { it.amount != 0L },
    genres = genres?.mapNotNull { it.name }.orEmpty(),
    duration = runtime?.let(Duration::ofMinutes),
    rating = if (voteAverage != null && voteCount != null) {
      MovieRating(
        average = voteAverage.toFloat(),
        voteCount = voteCount
      )
    } else {
      null
    }
  )
}

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
