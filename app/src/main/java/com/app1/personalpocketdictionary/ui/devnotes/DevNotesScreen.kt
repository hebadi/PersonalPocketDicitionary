package com.app1.personalpocketdictionary.ui.devnotes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.app1.personalpocketdictionary.R
import com.app1.personalpocketdictionary.ui.theme.PersonalPocketDictionaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevNotesScreen(
    onVenmoClick: () -> Unit,
    onPaypalClick: () -> Unit,
    onEmailClick: () -> Unit,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.dev_notes)) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back_description)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.full_dev_notes),
                fontFamily = FontFamily.SansSerif, // Approximating sans-serif-black, might need custom font loading for exact match
                fontSize = 16.sp,
                lineHeight = 24.sp, // (16sp + 8sp lineSpacingExtra)
                textAlign = TextAlign.Start, // Default, but can be explicit
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.weight(1f)) // Pushes buttons to the bottom if Column has fixed height or is constrained

            // Buttons are stacked from bottom in XML, so reverse order or adjust layout for similar feel
            // Using a Column for buttons to ensure they are below the text and above each other
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Spacing between buttons
            ) {
                ImageButton( // Venmo
                    onClick = onVenmoClick,
                    painterResId = R.drawable.venmo, // Ensure this drawable exists
                    contentDescriptionResId = R.string.pay_venmo,
                    modifier = Modifier.fillMaxWidth()
                )
                ImageButton( // PayPal
                    onClick = onPaypalClick,
                    painterResId = R.drawable.paypal, // Ensure this drawable exists
                    contentDescriptionResId = R.string.pay_paypal,
                    modifier = Modifier.fillMaxWidth()
                )
                ImageButton( // Email
                    onClick = onEmailClick,
                    painterResId = R.drawable.ic_baseline_email_24,
                    contentDescriptionResId = R.string.email_me,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ImageButton(
    onClick: () -> Unit,
    painterResId: Int,
    contentDescriptionResId: Int,
    modifier: Modifier = Modifier
) {
    Button( // Using a standard Button, can be customized to look like an ImageButton
        onClick = onClick,
        modifier = modifier.height(50.dp),
        contentPadding = PaddingValues(0.dp) // Remove default padding to make image fit
    ) {
        Image(
            painter = painterResource(id = painterResId),
            contentDescription = stringResource(id = contentDescriptionResId),
            contentScale = ContentScale.Fit, // Was fitCenter in XML
            modifier = Modifier.fillMaxSize() // Image fills the button
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DevNotesScreenPreview() {
    PersonalPocketDictionaryTheme {
        DevNotesScreen(
            onVenmoClick = {},
            onPaypalClick = {},
            onEmailClick = {},
            navController = NavController(LocalContext.current)
        )
    }
}
