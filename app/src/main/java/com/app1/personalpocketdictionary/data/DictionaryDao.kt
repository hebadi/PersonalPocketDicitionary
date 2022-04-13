package com.app1.personalpocketdictionary.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface DictionaryDao {
    @Query("SELECT * FROM dictionary") // can alter this to order by word for example
    fun getAll(): Flow<List<DictionaryData>>
    @Query("SELECT * from dictionary WHERE id=:id")
    fun getContents(id: Int): Flow<DictionaryData>
    @Query("SELECT word FROM dictionary")
    fun getWordsList(): Flow<List<String>>

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: DictionaryData)
    @Update
    suspend fun update(word: DictionaryData)
    @Delete
    suspend fun delete(word: DictionaryData)
}