package com.example.coffeeshop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeshop.R
import com.example.coffeeshop.model.ItemsModel

class PreviewCartAdapter(
    private val list: ArrayList<ItemsModel>
) : RecyclerView.Adapter<PreviewCartAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val txtName = view.findViewById<TextView>(R.id.txtName)
        val txtQty = view.findViewById<TextView>(R.id.txtQty)
        val txtSize = view.findViewById<TextView>(R.id.txtSize)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_preview_cart, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.txtName.text = item.title
        holder.txtQty.text = "${item.numberInCart} pcs"

        holder.txtSize.text = when(item.extra) {
            "size_small" -> "Small"
            "size_medium" -> "Medium"
            "size_large" -> "Large"
            "size_xl" -> "Extra Large"
            else -> ""
        }
    }

    override fun getItemCount() = list.size
}
