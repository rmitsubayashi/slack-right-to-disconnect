package com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostRecipient

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.slackrighttodisconnect.R
import kotlinx.android.synthetic.main.row_post_recipient_header.view.*

class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setTitle(recipientType: RecipientType?) {
        val text = when (recipientType) {
            null -> ""
            RecipientType.USER -> itemView.context.getString(R.string.post_recipient_title_user)
            RecipientType.CHANNEL -> itemView.context.getString(R.string.post_recipient_title_channel)
            RecipientType.THREAD -> itemView.context.getString(R.string.post_recipient_title_thread)
        }
        itemView.row_post_recipient_title.text = text
    }
}