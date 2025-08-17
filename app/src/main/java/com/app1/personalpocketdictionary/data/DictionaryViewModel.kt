package com.app1.personalpocketdictionary.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

open class DictionaryViewModel(private val dictionaryDao: DictionaryDao): ViewModel() {
    // backing property, used to allow access to only retrieve data outside of this class and at the same time have its internal variables modifiable
//
//    private var _dataSet = mutableListOf<DictionaryData>() // interestingly MutableLiveData<>() is capitalized, thats cause we're making an object (yes everything in kotlin is an object but the default data collection types act like methods instead of objects)
//    val dataSet get() = _dataSet

    val allData : LiveData<List<DictionaryData>> = dictionaryDao.getAll().asLiveData()

    fun retrieveData(id: Int) : LiveData<DictionaryData>{
        return dictionaryDao.getContents(id).asLiveData()
    }

    // this commented code below was used once to generate a db file to use to prepopulate the db upon initial install
//    init{
//        addNewItem("Parable", "noun", "a story with life lessons used to illustrate a moral or spiritual lesson.", "the para deez nuts on your chin. Gotem")
//        addNewItem("Hyponatremia", "noun", "hypo- meaning less than normal, na- sodium, think of the element Na, and emia- related to blood. All together, it means insufficient sodium in the blood", "yo if you're raving tonight add some electrolytes in your water pack. Especially if you're rolling watch out for hyponatremia")
//        addNewItem("Venerable", "adjective", "the epithet we yearn for. It means respected", "the great, the venerable, the honorable, Muhammad Ali")
//        }

    fun addNewItem(word: String, partOfSpeech: String, definition: String,example: String) {
        val newWord = DictionaryData(word = word, partOfSpeech = partOfSpeech, definition = definition, example = example)
        insertWord(newWord)
    }

    fun updateItem(id:Int, word: String, partOfSpeech: String, definition: String,example: String) {
        val newWord = DictionaryData(id, word, partOfSpeech, definition, example)
        updateWord(newWord)
    }

    fun isEntryValid(word: String, speech: String, definition: String, example: String): Boolean{
        if (word.isNotEmpty() && speech.isNotEmpty() && definition.isNotEmpty() &&  example.isNotEmpty()) return true
        return false
    }

    private fun insertWord(word: DictionaryData){
        viewModelScope.launch {
            dictionaryDao.insert(word) // this line is a suspend fun
        }
    }

    private fun updateWord(word: DictionaryData){
        viewModelScope.launch {
            dictionaryDao.update(word)
        }
    }

    fun deleteWord(word: DictionaryData){
        viewModelScope.launch {
            dictionaryDao.delete(word)
            Log.d("devNotes", "doa delete successful")
        }
    }

}