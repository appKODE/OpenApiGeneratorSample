package com.example.popularmovies.ui.screen.mapper

import com.example.popularmovies.domain.entity.MovieDetails
import com.example.popularmovies.domain.entity.MoviePreview
import com.example.popularmovies.ui.screen.details.UiMovieDetails
import com.example.popularmovies.ui.screen.popular.UiMoviePreview
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun MoviePreview.toUiModel(): UiMoviePreview {
  return UiMoviePreview(
    id = UiMoviePreview.Id(id.value),
    localizedTitle = localizedTitle,
    originalTitle = originalTitle,
    posterUrl = posterUrl?.value,
    releaseDate = releaseDate?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)),
    rating = rating?.let {
      UiMoviePreview.Rating(
        average = String.format("%.1f", it.average),
        voteCount = it.voteCount.toString()
      )
    }
  )
}

fun MovieDetails.toUiModel(): UiMovieDetails {
  return UiMovieDetails(
    localizedTitle = localizedTitle,
    originalTitle = originalTitle,
    overview = overview,
    posterUrl = posterUrl?.value,
    releaseDate = releaseDate?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)),
    budget = budgetUsd?.let { "$ ${it.amount}" },
    revenue = revenueUsd?.let { "$ ${it.amount}" },
    genres = genres,
    duration = duration?.format(),
    rating = rating?.let {
      UiMovieDetails.Rating(
        average = String.format("%.1f", it.average),
        voteCount = it.voteCount.toString()
      )
    }
  )
}

private fun Duration.format(): String {
  val hours = toHours()
  val minutes = toMinutes() % 60
  return buildString {
    if (hours != 0L) append("$hours ч. ")
    if (minutes != 0L) append("$minutes мин.")
  }.trim()
}
