package com.example.guttenburg.di

import com.example.guttenburg.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class])
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
}