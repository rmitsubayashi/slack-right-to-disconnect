package com.github.rmitsubayashi.slackrighttodisconnect.post.select.channel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import kotlinx.android.synthetic.main.row__select__channel.view.*

class SelectChannelViewHolder(itemView: View, private val listener: SelectChannelRowClickListener): RecyclerView.ViewHolder(itemView) {
    fun setChannel(channel: Recipient) {
        itemView.label__row_select_channel__name.text = channel.displayName
        itemView.setOnClickListener {
            listener.onChannelClicked(channel)
        }
    }
}