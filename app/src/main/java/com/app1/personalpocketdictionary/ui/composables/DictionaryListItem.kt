package com.app1.personalpocketdictionary.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.ui.theme.PersonalPocketDictionaryTheme

@Composable
fun DictionaryListItem(
    itemData: DictionaryData,
    itemNumber: Int,
    onItemClick: (DictionaryData) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemClick(itemData) }
            .padding(horizontal = 16.dp, vertical = 8.dp), // Corresponds to @dimen/text_margin roughly
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = itemNumber.toString(),
            style = MaterialTheme.typography.titleMedium, // Approximating textAppearanceListItem
            modifier = Modifier.padding(end = 16.dp) // Spacing between number and word
        )
        Text(
            text = itemData.word,
            style = MaterialTheme.typography.bodyLarge // Approximating textAppearanceListItem
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DictionaryListItemPreview() {
    PersonalPocketDictionaryTheme {
        DictionaryListItem(
            itemData = DictionaryData(id = 1, word = "Compose", partOfSpeech = "Noun", definition = "A UI toolkit", example = "Jetpack Compose is fun!"),
            itemNumber = 1,
            onItemClick = {}
        )
    }
}
