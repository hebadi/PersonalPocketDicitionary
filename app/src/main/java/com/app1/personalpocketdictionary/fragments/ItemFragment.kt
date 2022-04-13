package com.app1.personalpocketdictionary.fragments


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app1.personalpocketdictionary.R
import com.app1.personalpocketdictionary.data.DictionaryApplication
import com.app1.personalpocketdictionary.data.AppDataBase
import com.app1.personalpocketdictionary.data.DictionaryViewModel
import com.app1.personalpocketdictionary.data.DictionaryViewModelFactory
import com.app1.personalpocketdictionary.databinding.FragmentItemListBinding
import com.app1.personalpocketdictionary.listadapter.DictionaryListAdapter

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {

    private val viewModel: DictionaryViewModel by activityViewModels {
        DictionaryViewModelFactory(
            (activity?.application as DictionaryApplication).database.getDao()
        ) // sending a dao to the factory to get us the shared viewModel
    }

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true) // idk why tf hasMenuOptions(true) isn't working but fuck it this is working lets roll with it
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        Log.d("devNotes", "onCreateView of ItemFragment successfully completed")
        return binding.root
    }

    // used to set up the recycler view and assign layout manager if not done in xml
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("devNotes", "onViewCreated of ItemFragment successfully initiated")

        super.onViewCreated(view, savedInstanceState)

        // this passes in and takes care of moving from list fragment to detail fragment
        val adapter = DictionaryListAdapter {
            val action = ItemFragmentDirections.actionItemFragmentToItemDetailFragment(it.id)
            findNavController().navigate(action)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context) //xml can specify this too, but i chose to put it in code
            setAdapter(adapter)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
        binding.floatingActionButton.setOnClickListener {
            val action = ItemFragmentDirections.actionItemFragmentToAddAndEditFragment(getString(R.string.add_fragment_title))
            findNavController().navigate(action)
        }

        viewModel.allData.observe(viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
            //submitList() passes in the new list.
            // This will update the RecyclerView with the new items on the list.
        }
        Log.d("devNotes", "onViewCreated of ItemFragment successfully completed")
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    // for this to work the menu item id in the xml must be the same as the id of the fragment in the nav graph.
    // in this instance, they're both labeled as "devNotesFragment"
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        // close the primary database to ensure all the transactions are merged
        AppDataBase.getDatabase(this.requireContext()).close()
        Log.d("devNotes", "onDestroy of ItemFragment successfully completed")
    }
//bug fixes:
    // TODO: the app doesn't show the 12th term i believe despite it being added to the database. every subsequent addition will add the previous one that is in "que"
}