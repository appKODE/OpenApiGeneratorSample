package com.example.popularmovies.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class MoviesResponse(
  val results: List<MoviePreviewNM>
)
