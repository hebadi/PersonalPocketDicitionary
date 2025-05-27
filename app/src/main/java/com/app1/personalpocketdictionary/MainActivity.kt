package com.app1.personalpocketdictionary

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.app1.personalpocketdictionary.ui.navigation.AppNavHost
import com.app1.personalpocketdictionary.ui.theme.PersonalPocketDictionaryTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PersonalPocketDictionaryTheme {
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    onVenmoClick = { venmoMe() },
                    onPaypalClick = { paypalMe() },
                    onEmailClick = { emailMe() }
                )
            }
        }
    }

    // These methods are kept in MainActivity as they require Context to start Intents
    private fun venmoMe() {
        val venmoURL = "https://venmo.com/u/hamidebadi"
        val queryUrl: Uri = Uri.parse(venmoURL)
        val intent = Intent(Intent.ACTION_VIEW, queryUrl)
        startActivity(intent)
    }

    private fun paypalMe() {
        val paypalURL = "https://paypal.me/HamidEbadi?country.x=US&locale.x=en_US"
        val queryUrl: Uri = Uri.parse(paypalURL)
        val intent = Intent(Intent.ACTION_VIEW, queryUrl)
        startActivity(intent)
    }

    private fun emailMe() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_address)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.email_exception_activity_not_found), Toast.LENGTH_LONG).show()
        }
    }

    // onSupportNavigateUp is typically not needed with Compose Navigation,
    // as TopAppBars handle their own navigation icons and actions.
    // If it were needed for some specific interop, it would involve the Compose NavController.
}