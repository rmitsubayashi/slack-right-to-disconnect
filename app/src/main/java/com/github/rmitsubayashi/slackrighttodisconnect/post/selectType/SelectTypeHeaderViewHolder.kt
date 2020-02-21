package com.github.rmitsubayashi.slackrighttodisconnect.post.selectType

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row__select_type__header.view.*

class SelectTypeHeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setListener(listener: SelectTypeHeaderListener) {
        itemView.button__select_type__channel.setOnClickListener {
            listener.onSelectChannelClicked()
        }
        itemView.button__select_type__thread.setOnClickListener {
            listener.onSelectRecentThreadClicked()
        }
        itemView.button__select_type__user.setOnClickListener {
            listener.onSelectUserClicked()
        }
    }

}