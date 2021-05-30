package com.example.seq1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(
    private val dataset: List<ListeToDo>,
    private val listClickListener: OnListClickListener): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.list_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_list, parent, false)

        return ListViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val list = dataset[position]
        holder.textView.text = list.titreListeToDo
        holder.view.setOnClickListener {
            listClickListener.onListClicked(list)
        }
    }

    override fun getItemCount() = dataset.size
}

interface OnListClickListener {
    fun onListClicked(list: ListeToDo)
}