package com.example.coffeeshop.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeshop.R
import com.example.coffeeshop.databinding.ItemNotificationBinding
import com.example.coffeeshop.model.NotificationModel
import com.example.coffeeshop.ui.notification.NotificationDetailActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter: ListAdapter<NotificationModel, NotificationAdapter.ViewHolder>(DIFF_CALLBACK)  {

    class ViewHolder(private val binding: ItemNotificationBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: NotificationModel) {
            binding.apply {
                tvItemTitle.text = data.title
                tvItemDescription.text = convertTimestamp(data.timestamp)


                itemView.setOnClickListener {
                    val intentDetail = Intent(itemView.context, NotificationDetailActivity::class.java)
                    intentDetail.putExtra(NotificationDetailActivity.EXTRA_NOTIFICATION, data)
                    itemView.context.startActivity(intentDetail)
                }


            }
        }

        private fun convertTimestamp(ts: Long): String {
            if (ts == 0L) return "-"
            val sdf = SimpleDateFormat("dd MMM yyyy : HH:mm", Locale.getDefault())
            return sdf.format(Date(ts))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<NotificationModel> =
            object : DiffUtil.ItemCallback<NotificationModel>() {


                override fun areItemsTheSame(oldItem: NotificationModel, storyItem: NotificationModel): Boolean {
                    return oldItem.id == storyItem.id
                }


                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: NotificationModel, storyItem: NotificationModel): Boolean {
                    return oldItem == storyItem
                }
            }
    }


}