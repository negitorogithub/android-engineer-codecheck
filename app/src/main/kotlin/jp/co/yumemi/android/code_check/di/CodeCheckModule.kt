package jp.co.yumemi.android.code_check.di

import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import jp.co.yumemi.android.code_check.repository.GitHubSearchRepository
import jp.co.yumemi.android.code_check.repository.GitHubSearchRepositoryImpl
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CodeCheckModule {

    @Binds
    @Singleton
    abstract fun bindGitHubSearchRepositoryImpl(
        gitHubSearchRepositoryImpl: GitHubSearchRepositoryImpl
    ): GitHubSearchRepository

    companion object {
        @Provides
        @Singleton
        fun provideHttpClient(): HttpClient {
            return HttpClient(Android)
        }

        @Provides
        @Singleton
        fun provideMoshi(): Moshi {
            return Moshi.Builder().build()
        }
    }
}