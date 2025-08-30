package com.app1.personalpocketdictionary.di

import android.content.Context
import androidx.room.Room
import com.app1.personalpocketdictionary.data.AppDataBase
import com.app1.personalpocketdictionary.data.DictionaryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides database-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "app_database2.db"
        )
            .createFromAsset("database/app_database.db") // Pre-populated database
            .build()
    }

    @Provides
    fun provideDictionaryDao(database: AppDataBase): DictionaryDao {
        return database.getDao()
    }
}
