package com.app1.personalpocketdictionary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app1.personalpocketdictionary.databinding.FragmentDevNotesBinding

class testFragment : Fragment() {

    private var _binding: FragmentDevNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDevNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

}