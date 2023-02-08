package com.example.popularmovies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.popularmovies.ui.screen.details.MovieDetailsScreen
import com.example.popularmovies.ui.screen.details.MovieDetailsViewModel
import com.example.popularmovies.ui.screen.popular.PopularMoviesScreen
import com.example.popularmovies.ui.screen.popular.PopularMoviesViewModel
import com.example.popularmovies.ui.theme.PopularMoviesTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject
  lateinit var navHostController: NavHostController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      PopularMoviesTheme {
        NavHost(
          modifier = Modifier.fillMaxSize(),
          navController = navHostController,
          startDestination = "popular-movies"
        ) {
          composable("popular-movies") {
            val viewModel = hiltViewModel<PopularMoviesViewModel>()
            PopularMoviesScreen(viewModel)
          }
          composable(
            route = "movie-details/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
          ) {
            val viewModel = hiltViewModel<MovieDetailsViewModel>()
            MovieDetailsScreen(viewModel)
          }
        }
      }
    }
  }
}
