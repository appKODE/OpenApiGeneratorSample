package com.example.popularmovies.data.entity

import com.example.popularmovies.data.LocalDateSerializer
import java.time.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsResponse(
  @SerialName("id") val id: Long,
  @SerialName("title") val title: String?,
  @SerialName("original_title") val originalTitle: String?,
  @SerialName("overview") val overview: String?,
  @SerialName("poster_path") val posterPath: String?,
  @Serializable(with = LocalDateSerializer::class)
  @SerialName("release_date") val releaseDate: LocalDate?,
  @SerialName("budget") val budget: Long?,
  @SerialName("revenue") val revenue: Long?,
  @SerialName("runtime") val runtime: Long?,
  @SerialName("genres") val genres: List<Genre>,
  @SerialName("vote_count") val voteCount: Long?,
  @SerialName("vote_average") val voteAverage: Double?,
)
