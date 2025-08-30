package com.app1.personalpocketdictionary.presentation.state

/**
 * Enum representing different sorting options for dictionary words
 */
enum class SortOrder {
    ALPHABETICAL,           // A-Z
    REVERSE_ALPHABETICAL,   // Z-A  
    DATE_ADDED,            // Order by date added (ID)
    DATE_ADDED_REVERSE     // Reverse date added
}
