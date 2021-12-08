package com.app1.personalpocketdictionary.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app1.personalpocketdictionary.R
import com.app1.personalpocketdictionary.databinding.FragmentDevNotesBinding

class DevNotesFragment : Fragment() {

    private var _binding: FragmentDevNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDevNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            venmo.setOnClickListener { venmoMe() }
            paypal.setOnClickListener { paypalMe() }
            emailButton.setOnClickListener { emailMe() }
        }
    }


    /*
val packageManager: PackageManager = requireActivity().packageManager
            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY)
//            if(resolvedActivity== null){
//                isEnabled= false
//            }
intent.resolveActivity(packageManager)
     */


    private fun venmoMe(){
        // small codelab that dives deeper into implicit intents here:
        //https://developer.android.com/codelabs/basic-android-kotlin-training-activities-intents?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-kotlin-unit-3-pathway-1%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-training-activities-intents#6
        val paypalURL = "https://venmo.com/u/hamidebadi"
        val queryUrl: Uri = Uri.parse(paypalURL)
        val intent = Intent(Intent.ACTION_VIEW, queryUrl)

        startActivity(intent)

    }

    private fun paypalMe(){
        // small codelab that dives deeper into implicit intents here:
        //https://developer.android.com/codelabs/basic-android-kotlin-training-activities-intents?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-kotlin-unit-3-pathway-1%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-training-activities-intents#6
        val paypalURL = "https://paypal.me/HamidEbadi?country.x=US&locale.x=en_US"
        val queryUrl: Uri = Uri.parse(paypalURL)
        val intent = Intent(Intent.ACTION_VIEW, queryUrl)

        startActivity(intent)
    }

    private fun emailMe(){
        // small codelab that dives deeper into implicit intents here:
        //https://developer.android.com/codelabs/basic-android-kotlin-training-activities-intents?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-kotlin-unit-3-pathway-1%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-training-activities-intents#6
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // this line of code makes it so only email apps (excludes messaging apps) handle the intent
                putExtra(Intent.EXTRA_EMAIL, getString(R.string.email_address))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException){
            Toast.makeText(this.context,getString(R.string.email_exception_activity_not_found), Toast.LENGTH_LONG).show()
        }
    }
}

        /*
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // this line of code makes it so only email apps (excludes messaging apps) handle the intent
            putExtra(Intent.EXTRA_EMAIL, getString(R.string.email_address))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
        }
        startActivity(intent)
        Log.d("devNotes", "${intent.resolveActivity(packageManager)}")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
         */