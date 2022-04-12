package com.app1.personalpocketdictionary.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app1.personalpocketdictionary.data.DictionaryApplication
import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.data.DictionaryViewModel
import com.app1.personalpocketdictionary.data.DictionaryViewModelFactory
import com.app1.personalpocketdictionary.databinding.FragmentAddAndEditBinding


class AddAndEditFragment : Fragment() {

    private val viewModel: DictionaryViewModel by activityViewModels {
        DictionaryViewModelFactory((activity?.application as DictionaryApplication).database.getDao())
    }

    private var _binding : FragmentAddAndEditBinding? = null
    private val binding get() = _binding!!


    private fun hideKeyBoard(){
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }

    private fun makeChanges(num: Int){
        val inputWord = binding.inputWord.text.toString()
        val inputSpeech = binding.inputSpeech.text.toString()
        val inputDefinition = binding.inputDefinition.text.toString()
        val inputExample = binding.inputExample.text.toString()
        if (viewModel.isEntryValid(inputWord, inputSpeech, inputDefinition, inputExample)){
            if(num == 0) {
                addWord(inputWord, inputSpeech, inputDefinition, inputExample)
            }else updateWord(inputWord, inputSpeech, inputDefinition, inputExample)

            val action = AddAndEditFragmentDirections.actionAddAndEditFragmentToItemFragment()
            findNavController().navigate(action)
        }
        else{
            hideKeyBoard()
            Toast.makeText(this.context,"Ayo you left one empty fam", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddAndEditBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (navigationArgs.itemId > 0) editTasks()
        else binding.saveAction.setOnClickListener { makeChanges(0) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /*
    code needed for add word screen
     */
    private fun addWord(inputWord: String, inputSpeech: String, inputDefinition: String, inputExample: String){
        viewModel.addNewItem(inputWord, inputSpeech, inputDefinition, inputExample)
    }

    /*
    code needed for edit word screen
     */

    private lateinit var word: DictionaryData

    private val navigationArgs: AddAndEditFragmentArgs by navArgs()

    private fun updateWord(inputWord: String, inputSpeech: String, inputDefinition: String, inputExample: String){
        viewModel.updateItem(navigationArgs.itemId, inputWord, inputSpeech, inputDefinition, inputExample)
    }

    //this fun sets the live observer for our live data to display on the edit screen
    private fun editTasks(){
        viewModel.retrieveData(navigationArgs.itemId).observe(viewLifecycleOwner) { selectedWord ->
            word = selectedWord
            bind(word)
        }
    }

    private fun bind(word: DictionaryData) {
        binding.apply {
            inputWord.setText(word.word, TextView.BufferType.SPANNABLE)
            inputSpeech.setText(word.partOfSpeech, TextView.BufferType.SPANNABLE)
            inputDefinition.setText(word.definition, TextView.BufferType.SPANNABLE)
            inputExample.setText(word.example, TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { makeChanges(1) }
        }
    }

}