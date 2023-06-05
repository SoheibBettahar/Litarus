package com.example.guttenburg.di

import com.example.guttenburg.download.AndroidDownloader
import com.example.guttenburg.download.Downloader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DownloadModule {

    @Binds
    abstract fun bindDownloader(downloader: AndroidDownloader): Downloader
}