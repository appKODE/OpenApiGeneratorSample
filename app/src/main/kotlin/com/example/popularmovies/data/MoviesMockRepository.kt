package com.example.popularmovies.data

import com.example.popularmovies.domain.MoviesRepository
import com.example.popularmovies.domain.entity.MovieDetails
import com.example.popularmovies.domain.entity.MovieId
import com.example.popularmovies.domain.entity.MoviePreview
import com.example.popularmovies.domain.entity.MovieRating
import com.example.popularmovies.domain.entity.Url
import com.example.popularmovies.domain.entity.Usd
import java.time.Duration
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MoviesMockRepository @Inject constructor() : MoviesRepository {
  private val _popularMovies = MutableStateFlow<List<MoviePreview>>(emptyList())
  private val _movieDetails = MutableStateFlow<Map<MovieId, MovieDetails>>(emptyMap())
  override val popularMovies: Flow<List<MoviePreview>> = _popularMovies
  override fun observeMovieDetailsById(id: MovieId): Flow<MovieDetails?> {
    return _movieDetails.map { it[id] }
  }

  override suspend fun fetchPopularMovies(language: String, page: Long, region: String) {
    _popularMovies.update {
      (0 until 10).map { number -> generateRandomMoviePreview(MovieId(number)) }
    }
  }

  override suspend fun fetchMovieDetailsById(id: MovieId, language: String) {
    val moviePreview =
      _popularMovies.value.firstOrNull { it.id.value.toString() == id.value.toString() }
    _movieDetails.update {
      it.plus(id to generateRandomMovieDetails(id, moviePreview))
    }
  }

  companion object {
    private val words =
      setOf("Lorem", "Ipsum", "Simply", "Dummy", "Printing", "Typesetting", "Industry")
    private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
    private val posterPaths = setOf(
      "/kuf6dutpsT0vSVehic3EZIqkOBt.jpg",
      "/d9nBoowhjiiYc4FBNtQkPY7c11H.jpg",
      "/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg",
      "/26yQPXymbWeCLKwcmyL8dRjAzth.jpg",
      "/72V1r1G8S87ELagVxjqAUdChMCt.jpg",
      "/6sMnY4fEVAfdadhANhGnNckxsmx.jpg",
      "/1XSYOP0JjjyMz1irihvWywro82r.jpg",
      "/z2nfRxZCGFgAnVhb9pZO87TyTX5.jpg",
      "/sv1xJUazXeYqALzczSZ3O6nkH75.jpg",
      "/9z4jRr43JdtU66P0iy8h18OyLql.jpg",
    )
    private val genres = listOf(
      "Ужасы", "Комедия", "Триллер", "Боевик", "Драма", "Вестерн", "Приключенческий фильм"
    )

    private fun generateRandomMoviePreview(id: MovieId): MoviePreview {
      return MoviePreview(
        id = id,
        localizedTitle = words.random(),
        originalTitle = words.random(),
        posterUrl = Url("$POSTER_BASE_URL${posterPaths.random()}"),
        releaseDate = LocalDate.now().minusDays(Random.nextLong(30, 200)),
        rating = MovieRating(
          average = Random.nextDouble(6.0, 10.0).toFloat(),
          voteCount = Random.nextLong(2000, 200000),
        )
      )
    }

    private fun generateRandomMovieDetails(
      id: MovieId,
      moviePreview: MoviePreview? = null
    ): MovieDetails {
      return MovieDetails(
        id = id,
        localizedTitle = moviePreview?.localizedTitle ?: words.random(),
        originalTitle = moviePreview?.originalTitle ?: words.random(),
        overview = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
          "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an " +
          "unknown printer took a galley of type and scrambled it to make a type specimen book.",
        posterUrl = moviePreview?.posterUrl ?: Url("$POSTER_BASE_URL${posterPaths.random()}"),
        releaseDate = moviePreview?.releaseDate
          ?: LocalDate.now().minusDays(Random.nextLong(30, 200)),
        budgetUsd = Usd(Random.nextLong(1_000_000, 300_000_000)),
        revenueUsd = Usd(Random.nextLong(1_000_000, 300_000_000)),
        genres = generateRandomGenres(),
        duration = Duration.ofMinutes(Random.nextLong(80, 180)),
        rating = moviePreview?.rating ?: MovieRating(
          average = Random.nextDouble(6.0, 10.0).toFloat(),
          voteCount = Random.nextLong(2000, 200000),
        )
      )
    }

    private fun generateRandomGenres(): List<String> {
      val count = Random.nextInt(1, 5)
      val tempGenres = genres.toMutableList()
      val result = mutableListOf<String>()
      repeat(count) {
        val randomIndex = Random.nextInt(tempGenres.size)
        val randomGenre = tempGenres.removeAt(randomIndex)
        result.add(randomGenre)
      }
      return result
    }
  }
}
