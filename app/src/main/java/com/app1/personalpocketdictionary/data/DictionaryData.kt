package com.app1.personalpocketdictionary.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "dictionary")
data class DictionaryData(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "word") val word: String,
    @ColumnInfo(name = "part_of_speech") val partOfSpeech: String, // sql has a naming convention where words are separated by _
    @ColumnInfo(name = "definition") val definition: String,
    @ColumnInfo(name = "example") val example: String
)