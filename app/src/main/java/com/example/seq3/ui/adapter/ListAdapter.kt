package com.example.seq3.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seq3.R
import com.example.seq3.data.model.Liste
import com.example.seq3.data.source.remote.api.ListResponse

class ListAdapter(
    private val listClickListener: OnListClickListener
): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private val lists: MutableList<Liste> = mutableListOf()

    fun show(lists: List<Liste>) {
        this.lists.addAll(lists)
        notifyDataSetChanged()
    }
    class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.list_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_list, parent, false)

        return ListViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val list = lists[position]
        holder.textView.text = list.label
        holder.view.setOnClickListener {
            listClickListener.onListClicked(list)
        }
    }

    override fun getItemCount() = lists.size

    interface OnListClickListener {
        fun onListClicked(list: Liste)
    }
}

