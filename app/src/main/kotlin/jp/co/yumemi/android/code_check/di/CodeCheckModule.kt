package jp.co.yumemi.android.code_check.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}