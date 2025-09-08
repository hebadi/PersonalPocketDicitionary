package com.app1.personalpocketdictionary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// class serves 3 purposes:
//Specify which entities are defined in the database.
//Provide access to a single instance of each DAO class.
//Perform any additional setup, such as pre-populating the database.

//The database class allows other classes easy access to the DAO class
@Database(entities = [DictionaryData::class], version = 1, exportSchema = false) //version number is incremented everytime a schema change is made
abstract class AppDataBase : RoomDatabase() {


    abstract fun getDao(): DictionaryDao

//When using an AppDatabase class,
// you want to ensure that only one instance of the database exists to prevent race conditions or other potential issues.
// The instance is stored in the companion object,

    companion object{
//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//            }
//        }
        @Volatile
        private var INSTANCE: AppDataBase? = null
        fun getDatabase(context: Context) : AppDataBase {
            // elvis operator = if instance not null return it, else do the following which in this case is to create an instance
            return INSTANCE ?: synchronized(this) {
                // i.e. synchronized ensures only 1 database is made using coroutines
                val instance = Room.databaseBuilder(
                    context, // context or context.applicationContext? I seen two versions
                    AppDataBase::class.java,
                    "app_database2.db")
//                    .fallbackToDestructiveMigration() // notes on this at the bottom
                    .createFromAsset("database/app_database.db") // loads the pre-populated database from assets
                    .build()
                INSTANCE = instance

                instance // i think this line is what the elvis operator finally returns. can also write return before this for more clarity
            }
        }
    }
}

/*
@fallbackToDestructiveMigration()
Normally, you would have to provide a migration object with a migration strategy
for when the schema changes. A migration object is an object that defines
how you take all rows with the old schema and convert them to rows in the new schema,
so that no data is lost. Migration is beyond the scope of this codelab.
A simple solution is to destroy and rebuild the database,
which means that the data is lost.
 */