package com.app1.personalpocketdictionary.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//Q: why do we need a factory for our viewModels? Why can't we just have the class and instantiate them in our fragments?
//A: the viewmodel should instantiated by an object that can respond to lifecycle events.
// Normally, livedata would take care of that but we're not using live data types here
// If you instantiate it directly in one of your fragments,
// then your fragment object will have to handle everything all the memory management, which is beyond the scope of what your app's code should do.
// Instead, you can create a class, called a factory, that will instantiate view model objects for you.

class DictionaryViewModelFactory(private val dictionaryDao: DictionaryDao) : ViewModelProvider.Factory {
    // the viewmodel factory has one abstract fun called create that we gotta override. thats really where the magic happens
    // remember the point of this is to make it lifecycle aware, else we'd need to place our viewmodel in onCreate, onViewCreated, etc in our fragment
    // this is boilerplate code, so really just a copy pasta everytime, little to no variation for all setups
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DictionaryViewModel(dictionaryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
    // with this, i can now:
    // instantiate a DictionaryViewModelFactory object with DictionaryViewModelFactory.create(),
    // so that your view model can be lifecycle aware without your fragment having to handle this directly.
    // remember this is a must for code that doesn't have livedata
}