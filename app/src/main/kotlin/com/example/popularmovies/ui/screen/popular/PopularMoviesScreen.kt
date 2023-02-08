package com.example.popularmovies.ui.screen.popular

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PopularMoviesScreen(viewModel: PopularMoviesViewModel) {
  val state by viewModel.state.collectAsState()

  when {
    state.showError -> ContentError(
      onTryAgainClick = viewModel::reloadRequested,
      modifier = Modifier.fillMaxSize()
    )
    state.showLoading -> ContentLoading(Modifier.fillMaxSize())
    else -> ContentReady(
      movies = state.movies,
      onMovieClick = viewModel::onMovieDetailsRequested,
      modifier = Modifier.fillMaxSize()
    )
  }
}


@Composable
private fun ContentError(
  onTryAgainClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "Ошибка при загрузке. Пожалуйста, проверьте ваше соединение с интернетом и попробуйте еще раз",
      style = MaterialTheme.typography.h6,
      textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(12.dp))
    Button(
      onClick = onTryAgainClick,
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
      Text(text = "Еще раз")
    }
  }
}

@Composable
private fun ContentLoading(
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    CircularProgressIndicator()
  }
}

@Composable
private fun ContentReady(
  movies: List<UiMoviePreview>,
  onMovieClick: (UiMoviePreview.Id) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Text(
        modifier = Modifier.padding(bottom = 4.dp),
        text = "Популярные фильмы",
        style = MaterialTheme.typography.h5
      )
    }
    items(movies) { movie ->
      MovieCard(
        movie = movie,
        onClick = { onMovieClick(movie.id) },
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}

@Composable
private fun MovieCard(
  movie: UiMoviePreview,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .shadow(elevation = 4.dp, shape = MovieCardShape)
      .clip(MovieCardShape)
      .clickable(onClick = onClick)
      .background(color = Color.White, shape = MovieCardShape)
      .padding(bottom = 16.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    if (movie.posterUrl?.isNotBlank() == true) {
      AsyncImage(
        modifier = Modifier.fillMaxWidth(),
        model = movie.posterUrl,
        contentDescription = "movie poster image",
        contentScale = ContentScale.FillWidth
      )
      Spacer(modifier = Modifier.height(4.dp))
    }
    Row(
      Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    ) {
      Column(
        Modifier
          .weight(1f)
          .padding(end = 16.dp)
      ) {
        if (movie.localizedTitle.isNotBlank()) {
          Text(
            text = movie.localizedTitle,
            style = MaterialTheme.typography.h6
          )
        }
        if (movie.originalTitle != movie.localizedTitle && movie.originalTitle.isNotBlank()) {
          Text(
            text = movie.originalTitle,
            style = MaterialTheme.typography.caption
          )
        }
      }
      if (movie.releaseDate != null) {
        Text(
          text = movie.releaseDate,
          modifier = Modifier.padding(top = 4.dp),
          style = MaterialTheme.typography.subtitle1
        )
      }
    }
    if (movie.rating != null) {
      Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = "Рейтинг: ${movie.rating.average} (${movie.rating.voteCount} оценок)"
      )
    }
  }
}

private val MovieCardShape = RoundedCornerShape(16.dp)
