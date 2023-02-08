package com.example.popularmovies.ui.screen.popular

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.popularmovies.di.Prod
import com.example.popularmovies.domain.MoviesRepository
import com.example.popularmovies.ui.screen.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
  @Prod private val repository: MoviesRepository,
  private val navHostController: NavHostController,
) : ViewModel() {
  private val _state = MutableStateFlow(PopularMoviesViewState())
  val state: StateFlow<PopularMoviesViewState> = _state.asStateFlow()

  init {
    load()
    repository.popularMovies
      .onEach { movies ->
        _state.update { it.copy(movies = movies.map { movie -> movie.toUiModel() }) }
      }
      .launchIn(viewModelScope)
  }

  fun reloadRequested() {
    _state.update { it.copy(showError = false) }
    load()
  }

  fun onMovieDetailsRequested(id: UiMoviePreview.Id) {
    navHostController.navigate("movie-details/${id.value}")
  }

  private fun load() {
    viewModelScope.launch {
      runCatching {
        val locale = Locale.getDefault()
        repository.fetchPopularMovies(
          language = locale.toLanguageTag(),
          page = 1,
          region = locale.country
        )
      }.onFailure {
        _state.update { it.copy(showError = true) }
      }
    }
  }
}

@Immutable
data class PopularMoviesViewState(
  val movies: List<UiMoviePreview> = emptyList(),
  val showError: Boolean = false
) {
  val showLoading: Boolean get() = movies.isEmpty() && !showError
}

@Immutable
data class UiMoviePreview(
  val id: Id,
  val localizedTitle: String,
  val originalTitle: String,
  val posterUrl: String? = null,
  val releaseDate: String? = null,
  val rating: Rating? = null,
) {
  @JvmInline
  value class Id(val value: Any)

  @Immutable
  data class Rating(
    val average: String,
    val voteCount: String,
  )
}
