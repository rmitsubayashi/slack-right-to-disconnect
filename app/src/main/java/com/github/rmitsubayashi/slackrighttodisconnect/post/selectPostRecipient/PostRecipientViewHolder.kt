package com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostRecipient

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostRecipient.PostRecipientRowClickListener
import kotlinx.android.synthetic.main.row_post_recipient.view.*

class PostRecipientViewHolder(itemView: View, private val listener: PostRecipientRowClickListener): RecyclerView.ViewHolder(itemView) {
    fun setRecipient(recipient: Recipient) {
        itemView.row_post_recipient_name.text = recipient.name
        itemView.setOnClickListener { listener.onItemClicked(recipient) }
    }
}