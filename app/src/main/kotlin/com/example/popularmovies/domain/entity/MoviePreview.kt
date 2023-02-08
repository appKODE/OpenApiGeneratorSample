package com.example.popularmovies.domain.entity

import java.time.LocalDate

data class MoviePreview(
  val id: MovieId,
  val localizedTitle: String,
  val originalTitle: String,
  val posterUrl: Url? = null,
  val releaseDate: LocalDate? = null,
  val rating: MovieRating? = null
)
