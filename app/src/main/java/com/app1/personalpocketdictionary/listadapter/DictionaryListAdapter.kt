package com.app1.personalpocketdictionary.listadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app1.personalpocketdictionary.data.DictionaryData
import com.app1.personalpocketdictionary.databinding.FragmentItemBinding

class DictionaryListAdapter(private val onItemClicked: (DictionaryData) -> Unit):
    ListAdapter<DictionaryData, DictionaryListAdapter.ViewHolder>(DiffCallback) {
// this DiffCallback is the magic of the listAdapter that makes it different from recycler views,
// it'll help tell whats different between the new and old list
// this way as soon as something is different it'll update the list live. recyclerview won't do that
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DictionaryData>() {
            override fun areItemsTheSame(oldItem: DictionaryData, newItem: DictionaryData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DictionaryData, newItem: DictionaryData): Boolean {
                return oldItem.word == newItem.word
            }
        }
    }

    inner class ViewHolder(private var binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var wordCountView = binding.itemNumber
        var contentView = binding.content
//        fun bind(word: DictionaryData){
//            binding.apply {
////                itemNumber.text = word.id.toString()
////                content.text = word.word
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPosition = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(currentPosition)
        }
//        holder.bind(currentPosition)
        holder.wordCountView.text = (position + 1).toString()
        holder.contentView.text = getItem(position).word
        // where is the navigation to the detail?
//        A: that navigation action is passed into the onItemClicked and will execute once that itemView has been clicked :)
    }
}