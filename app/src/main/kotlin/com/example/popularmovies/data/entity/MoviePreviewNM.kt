package com.example.popularmovies.data.entity

import com.example.popularmovies.data.LocalDateSerializer
import java.time.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviePreviewNM(
  @SerialName("id") val id: Long,
  @SerialName("title") val title: String?,
  @SerialName("original_title") val originalTitle: String?,
  @SerialName("poster_path") val posterPath: String?,
  @Serializable(with = LocalDateSerializer::class)
  @SerialName("release_date") val releaseDate: LocalDate?,
  @SerialName("vote_count") val voteCount: Long?,
  @SerialName("vote_average") val voteAverage: Double?,
)
