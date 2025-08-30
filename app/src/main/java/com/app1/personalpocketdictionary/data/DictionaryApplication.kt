package com.app1.personalpocketdictionary.data

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class that enables Hilt dependency injection.
 * Hilt will manage database and repository instances automatically.
 */
@HiltAndroidApp
class DictionaryApplication : Application()