package com.example.popularmovies.ui.screen.details

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun MovieDetailsScreen(viewModel: MovieDetailsViewModel) {
  val state by viewModel.state.collectAsState()

  when {
    state.showError -> ContentError(
      showTryAgainButton = state.showTryAgainErrorButton,
      onTryAgainClick = viewModel::reloadRequested,
      modifier = Modifier.fillMaxSize()
    )
    state.showLoading -> ContentLoading(Modifier.fillMaxSize())
    else -> ContentReady(
      movie = requireNotNull(state.movie),
      modifier = Modifier.fillMaxSize()
    )
  }
}

@Composable
private fun ContentError(
  showTryAgainButton: Boolean,
  onTryAgainClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = CenterHorizontally
  ) {
    val text = remember(showTryAgainButton) {
      if (showTryAgainButton) {
        "Ошибка при загрузке. Пожалуйста, проверьте ваше соединение с интернетом и попробуйте еще раз"
      } else {
        "Ошибка при загрузке. Пожалуйста, попробуйте открыть страницу еще раз"
      }
    }
    Text(
      text = text,
      style = MaterialTheme.typography.h6,
      textAlign = TextAlign.Center
    )
    if (showTryAgainButton) {
      Spacer(modifier = Modifier.height(12.dp))
      Button(
        onClick = onTryAgainClick,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
      ) {
        Text(text = "Еще раз")
      }
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
  movie: UiMovieDetails,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .padding(horizontal = 16.dp)
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))
    if (movie.posterUrl != null) {
      AsyncImage(
        model = movie.posterUrl,
        contentDescription = "movie poster",
        modifier = Modifier
          .align(CenterHorizontally)
          .clip(RoundedCornerShape(16.dp))
          .fillMaxWidth(),
        contentScale = ContentScale.FillWidth
      )
    }
    Row(Modifier.fillMaxWidth()) {
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
    if (movie.genres.isNotEmpty()) {
      HorizontalDivider(Modifier.fillMaxWidth())
      Text(
        text = movie.genres.joinToString(", "),
        style = MaterialTheme.typography.subtitle2
      )
    }
    if (movie.overview?.isNotBlank() == true) {
      HorizontalDivider(Modifier.fillMaxWidth())
      Text(text = movie.overview)
    }
    if (movie.budget != null) {
      HorizontalDivider(Modifier.fillMaxWidth())
      Text(text = "Бюджет: ${movie.budget}")
    }
    if (movie.revenue != null) {
      HorizontalDivider(Modifier.fillMaxWidth())
      Text(text = "Сборы: ${movie.revenue}")
    }
    if (movie.duration != null) {
      HorizontalDivider(Modifier.fillMaxWidth())
      Text(text = "Продолжительность: ${movie.duration}")
    }
    if (movie.rating != null) {
      HorizontalDivider(Modifier.fillMaxWidth())
      Text(text = "Рейтинг: ${movie.rating.average} (${movie.rating.voteCount} оценок)")
    }
    Spacer(modifier = Modifier.height(12.dp))
  }
}

@Composable
private fun HorizontalDivider(
  modifier: Modifier = Modifier,
  thickness: Dp = 1.dp
) {
  Box(
    modifier = modifier
      .height(thickness)
      .background(
        brush = Brush.linearGradient(
          0f to Color.Gray,
          0.2f to Color.Gray.copy(alpha = 0.5f),
          0.4f to Color.Transparent
        )
      )
  )
}
