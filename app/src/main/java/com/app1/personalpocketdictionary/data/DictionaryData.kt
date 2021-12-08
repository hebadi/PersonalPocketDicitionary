package com.app1.personalpocketdictionary.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "dictionary")
data class DictionaryData(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    @NonNull @ColumnInfo (name = "word") val word: String,
    @NonNull @ColumnInfo (name = "part_of_speech") val partOfSpeech: String, // sql has a naming convention where words are separated by _
    @NonNull @ColumnInfo (name = "definition") val definition: String,
    @NonNull @ColumnInfo (name = "example") val example: String
    )
// i'm explicitly declaring the columns ass nonnull because SQL columns can be null by default, but my vals aren't String? they're string, i'm passing nonnull values in so SQL better recognize we aint playing
