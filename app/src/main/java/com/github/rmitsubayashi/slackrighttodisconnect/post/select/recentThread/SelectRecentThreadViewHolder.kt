package com.github.rmitsubayashi.slackrighttodisconnect.post.select.recentThread

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Message
import kotlinx.android.synthetic.main.row_select_thread.view.*

class SelectRecentThreadViewHolder(itemView: View, private val listener: SelectRecentThreadRowClickListener): RecyclerView.ViewHolder(itemView) {
    fun setRecentThread(message: Message) {
        itemView.row_select_thread_message.text = message.message
        itemView.row_select_thread_date.text = message.date.toString()
        itemView.row_select_thread_name.text = message.recipient.recipientType.toString()
        itemView.setOnClickListener { listener.onRecentThreadClicked(message) }
    }
}