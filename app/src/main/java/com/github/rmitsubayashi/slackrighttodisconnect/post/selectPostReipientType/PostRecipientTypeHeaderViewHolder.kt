package com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostReipientType

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.RecipientType
import kotlinx.android.synthetic.main.row_post_recipient_type_header.view.*

class PostRecipientTypeHeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setListener(listener: HeaderListener) {
        itemView.select_post_recipient_type_channel_button.setOnClickListener {
            listener.onRecipientTypeClicked(RecipientType.CHANNEL)
        }
        itemView.select_post_recipient_type_thread_button.setOnClickListener {
            listener.onRecipientTypeClicked(RecipientType.THREAD)
        }
        itemView.select_post_recipient_type_user_button.setOnClickListener {
            listener.onRecipientTypeClicked(RecipientType.USER)
        }
    }

}