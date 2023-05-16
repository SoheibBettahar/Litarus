package com.example.guttenburg

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Singleton

@Singleton
@HiltAndroidApp
class GuttenburgApplication : Application()
