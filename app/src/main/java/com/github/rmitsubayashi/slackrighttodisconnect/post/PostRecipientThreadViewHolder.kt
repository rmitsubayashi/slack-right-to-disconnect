package com.github.rmitsubayashi.slackrighttodisconnect.post

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.model.ThreadInfo
import kotlinx.android.synthetic.main.row_post_recipient_thread.view.*

class PostRecipientThreadViewHolder(itemView: View, private val listener: PostRecipientRowClickListener): RecyclerView.ViewHolder(itemView) {
    fun setRecipient(recipient: Recipient) {
        itemView.row_post_recipient_name.text = recipient.name
        itemView.setOnClickListener { listener.onItemClicked(recipient) }
        itemView.row_post_recipient_thread_date.text = (recipient as ThreadInfo).date.toString()
        itemView.row_post_recipient_thread_recipient_type.text = recipient.parentType.name
    }
}