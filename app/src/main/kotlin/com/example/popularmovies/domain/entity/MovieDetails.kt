package com.example.popularmovies.domain.entity

import java.time.Duration
import java.time.LocalDate

data class MovieDetails(
  val id: MovieId,
  val localizedTitle: String,
  val originalTitle: String,
  val overview: String? = null,
  val posterUrl: Url? = null,
  val releaseDate: LocalDate? = null,
  val budgetUsd: Usd? = null,
  val revenueUsd: Usd? = null,
  val genres: List<String> = emptyList(),
  val duration: Duration? = null,
  val rating: MovieRating? = null
)
