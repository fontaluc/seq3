package com.example.seq1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val dataset: List<ItemToDo>, private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.checkBox.text = item.description
        holder.checkBox.isChecked = item.fait
        holder.view.setOnClickListener{
            itemClickListener.onItemClicked(holder.checkBox, position)
        }
    }

    override fun getItemCount() = dataset.size
}

interface OnItemClickListener {
    fun onItemClicked(v: View, pos: Int)
}