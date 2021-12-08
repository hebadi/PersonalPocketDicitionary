package com.app1.personalpocketdictionary.data

import android.app.Application
import com.app1.personalpocketdictionary.data.AppDataBase

// this summons or builds the database from the AppDataBase class we made earlier
class DictionaryApplication : Application() {
    val database: AppDataBase by lazy {AppDataBase.getDatabase(this)}
}

// to use this class, (instead of the default Application class) we need to assert that in the manifest