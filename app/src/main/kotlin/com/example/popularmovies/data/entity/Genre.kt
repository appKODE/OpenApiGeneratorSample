package com.example.popularmovies.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
  val id: Long,
  val name: String
)
