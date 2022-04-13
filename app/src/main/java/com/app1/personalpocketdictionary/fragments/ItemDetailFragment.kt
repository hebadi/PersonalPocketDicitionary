package com.app1.personalpocketdictionary.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.fragment.findNavController
import com.app1.personalpocketdictionary.data.DictionaryApplication
import com.app1.personalpocketdictionary.R
import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.data.DictionaryViewModel
import com.app1.personalpocketdictionary.data.DictionaryViewModelFactory
import com.app1.personalpocketdictionary.databinding.ItemDetailFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ItemDetailFragment : Fragment() {

    // Property delegation in Kotlin helps you to handoff the getter-setter responsibility to a different class.
    // this delegation is made by using "by" followed by the delegate class, in this case "viewModels()"
    // The delegate class creates the viewModel object for you on the first access, and retains its value through configuration changes and returns the value when requested.

    private val viewModel: DictionaryViewModel by activityViewModels{
        DictionaryViewModelFactory(
            (activity?.application as DictionaryApplication).database.getDao()
        ) // not sure what all that above is but its boilerplate code copy pasta
    }
//    below are other ways to make a late init view model
//    private val viewModel: DictionaryViewModel by lazy {
//        ViewModelProvider(this).get(DictionaryViewModel::class.java)
//    }

//    private lateinit var viewModel: DictionaryViewModel

    private lateinit var word: DictionaryData

    private val navigationArgs: ItemDetailFragmentArgs by navArgs() // this is added in the navGraph


    private var _binding: ItemDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private fun bind(word: DictionaryData) {
        binding.apply {
            words.text = word.word.uppercase()
            speech2.text = word.partOfSpeech
            definition2.text = word.definition
            example2.text = word.example
            deleteItem.setOnClickListener { showConfirmationDialog() }
            editItem.setOnClickListener { editItem() }
            imageButton.setOnClickListener { googleIt() }
        }
    }

    private fun googleIt(){
        // small codelab that dives deeper into implicit intents here:
        //https://developer.android.com/codelabs/basic-android-kotlin-training-activities-intents?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-kotlin-unit-3-pathway-1%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-training-activities-intents#6
        val searchPrefix = "https://www.google.com/search?q="
        val queryUrl: Uri = Uri.parse("${searchPrefix}${word.word}+definition")
        val intent = Intent(Intent.ACTION_VIEW, queryUrl)
        startActivity(intent)
//        this.startActivity(intent)
//        context.startActivity(intent)
    }

    private fun showConfirmationDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }
            .show()
    }

    private fun editItem() {
        val action = ItemDetailFragmentDirections.actionItemDetailFragmentToAddAndEditFragment(
            getString(R.string.edit_fragment_title),
            word.id
        )
        findNavController().navigate(action)
    }

    private fun deleteItem() {
        viewModel.deleteWord(word)
        findNavController().navigateUp()
    }
    /*
        // once the word is deleted, we're already in the ItemFragment so the UI updates the changes and we're left with a jump in sequence
        // ex. if we have list 1-5 words and delete item 3 the list will now show 1,2,4,5 instead of 1,2,3,4
     */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ItemDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        Log.d("devNotes", "nav args ID: $id")
        viewModel.retrieveData(id).observe(viewLifecycleOwner) { selectedItem ->
            selectedItem?.let {
                word = selectedItem
                bind(word)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}