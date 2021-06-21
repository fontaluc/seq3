package com.example.seq3.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.seq3.R
import com.example.seq3.data.model.Item
import com.example.seq3.data.source.remote.api.ItemResponse

class ItemAdapter(private val itemClickListener: OnItemClickListener): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    val items: MutableList<Item> = mutableListOf()

    fun show(items: List<Item>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.checkBox.text = item.label
        holder.checkBox.isChecked = when(item.checked) {
            1 -> true
            else -> false
        }

        holder.view.setOnClickListener{
            itemClickListener.onItemClicked(holder.checkBox, position)
        }
    }

    override fun getItemCount() = items.size

    interface OnItemClickListener {
        fun onItemClicked(v: View, pos: Int)
    }
}

