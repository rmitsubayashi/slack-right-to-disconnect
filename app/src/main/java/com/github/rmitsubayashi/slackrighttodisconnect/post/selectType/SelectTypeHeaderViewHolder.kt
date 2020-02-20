package com.github.rmitsubayashi.slackrighttodisconnect.post.selectType

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_post_recipient_type_header.view.*

class SelectTypeHeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setListener(listener: SelectTypeHeaderListener) {
        itemView.select_post_recipient_type_channel_button.setOnClickListener {
            listener.onSelectChannelClicked()
        }
        itemView.select_post_recipient_type_thread_button.setOnClickListener {
            listener.onSelectRecentThreadClicked()
        }
        itemView.select_post_recipient_type_user_button.setOnClickListener {
            listener.onSelectUserClicked()
        }
    }

}