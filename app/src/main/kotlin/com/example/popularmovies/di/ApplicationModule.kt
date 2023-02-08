package com.example.popularmovies.di

import android.content.Context
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import com.example.popularmovies.BuildConfig
import com.example.popularmovies.data.MoviesApi
import com.example.popularmovies.data.MoviesDataRepository
import com.example.popularmovies.data.MoviesMockRepository
import com.example.popularmovies.data.interceptor.ApiKeyQueryInterceptor
import com.example.popularmovies.domain.MoviesRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

@Suppress("Unused")
@Module
@InstallIn(SingletonComponent::class)
interface ApplicationModule {
  @Binds
  @Singleton
  @Mock
  fun bindMoviesMockRepository(impl: MoviesMockRepository): MoviesRepository

  @Binds
  @Singleton
  @Prod
  fun bindMoviesProdRepository(impl: MoviesDataRepository): MoviesRepository

  companion object {
    @Provides
    @Singleton
    fun provideNavHostController(@ApplicationContext context: Context): NavHostController {
      return NavHostController(context).apply {
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())
      }
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
      return HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
      }
    }

    @Provides
    @ApiKeyInterceptor
    fun provideApiKeyQueryInterceptor(): Interceptor {
      return ApiKeyQueryInterceptor()
    }

    @Provides
    fun provideHttpClient(
      loggingInterceptor: HttpLoggingInterceptor,
      @ApiKeyInterceptor apiKeyQueryInterceptor: Interceptor
    ): OkHttpClient {
      return OkHttpClient.Builder()
        .addInterceptor(apiKeyQueryInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
    }

    @Provides
    fun provideJson(): Json {
      return Json {
        ignoreUnknownKeys = true
        prettyPrint = BuildConfig.DEBUG
        coerceInputValues = true
      }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideTmdbRetrofit(client: OkHttpClient, json: Json): Retrofit {
      return Retrofit.Builder()
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_TMBD_URL)
        .build()
    }

    @Provides
    @Singleton
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi = retrofit.create()
  }
}

private const val BASE_TMBD_URL = "https://api.themoviedb.org/"
