package com.soheibbettahar.litarus.di

import com.soheibbettahar.litarus.download.AndroidDownloader
import com.soheibbettahar.litarus.download.Downloader
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