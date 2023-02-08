package com.example.popularmovies.ui.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.popularmovies.di.Prod
import com.example.popularmovies.domain.MoviesRepository
import com.example.popularmovies.domain.entity.MovieId
import com.example.popularmovies.ui.screen.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.annotation.concurrent.Immutable
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
  @Prod private val repository: MoviesRepository,
  navHostController: NavHostController
) : ViewModel() {

  private val _state = MutableStateFlow(MovieDetailsViewState())
  val state: StateFlow<MovieDetailsViewState> = _state.asStateFlow()

  init {
    val movieId =
      navHostController.currentBackStackEntry?.arguments?.getString("movieId")?.let(::MovieId)
    if (movieId != null) {
      _state.update { it.copy(movieId = movieId) }
      load(movieId)
      repository.observeMovieDetailsById(movieId)
        .filterNotNull()
        .map { it.toUiModel() }
        .onEach { details ->
          _state.update { it.copy(movie = details) }
        }
        .launchIn(viewModelScope)
    } else {
      _state.update {
        it.copy(showError = true)
      }
    }
  }

  fun reloadRequested() {
    _state.update {
      it.copy(
        showError = false,
        showTryAgainErrorButton = false
      )
    }
    load(requireNotNull(_state.value.movieId))
  }

  private fun load(id: MovieId) {
    viewModelScope.launch {
      runCatching {
        repository.fetchMovieDetailsById(
          id = id,
          language = Locale.getDefault().toLanguageTag()
        )
      }.onFailure {
        _state.update {
          it.copy(
            showError = true,
            showTryAgainErrorButton = true
          )
        }
      }
    }
  }
}

@Immutable
data class MovieDetailsViewState(
  val movieId: MovieId? = null,
  val movie: UiMovieDetails? = null,
  val showError: Boolean = false,
  val showTryAgainErrorButton: Boolean = false,
) {
  val showLoading: Boolean get() = movie == null && !showError
}

@Immutable
data class UiMovieDetails(
  val localizedTitle: String,
  val originalTitle: String,
  val overview: String? = null,
  val posterUrl: String? = null,
  val releaseDate: String? = null,
  val budget: String? = null,
  val revenue: String? = null,
  val genres: List<String> = emptyList(),
  val duration: String? = null,
  val rating: Rating? = null,
) {
  @Immutable
  data class Rating(
    val average: String,
    val voteCount: String,
  )
}
