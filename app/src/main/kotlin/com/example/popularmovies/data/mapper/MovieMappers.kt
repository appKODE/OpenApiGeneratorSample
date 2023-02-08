package com.example.popularmovies.data.mapper

import com.example.popularmovies.data.entity.MovieDetailsResponse
import com.example.popularmovies.data.entity.MoviePreviewNM
import com.example.popularmovies.domain.entity.MovieDetails
import com.example.popularmovies.domain.entity.MovieId
import com.example.popularmovies.domain.entity.MoviePreview
import com.example.popularmovies.domain.entity.MovieRating
import com.example.popularmovies.domain.entity.Url
import java.time.Duration

fun MoviePreviewNM.toDomainModel(): MoviePreview {
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

fun MovieDetailsResponse.toDomainModel(): MovieDetails {
  return MovieDetails(
    id = MovieId(id),
    localizedTitle = title.orEmpty(),
    originalTitle = originalTitle.orEmpty(),
    overview = overview,
    posterUrl = posterPath?.let { Url("$POSTER_BASE_URL$it") },
    releaseDate = releaseDate,
    budgetUsd = budget?.takeIf { it != 0L },
    revenueUsd = revenue?.takeIf { it != 0L },
    genres = genres.map { it.name },
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
