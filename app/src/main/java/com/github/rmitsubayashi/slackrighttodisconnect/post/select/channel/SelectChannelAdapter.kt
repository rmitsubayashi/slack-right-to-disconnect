package com.github.rmitsubayashi.slackrighttodisconnect.post.select.channel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.post.select.HeaderViewHolder

class SelectChannelAdapter(
    private val listener: SelectChannelRowClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val channels = mutableListOf<Recipient>()
    private val headerCt = 1
    override fun getItemCount(): Int = channels.size + headerCt

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewID = when (viewType) {
            VIEW_TYPE_HEADER -> R.layout.row_post_recipient_header
            else -> R.layout.row_select_channel
        }
        val view =
            LayoutInflater.from(parent.context).inflate(viewID, parent, false)
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(view)
            else -> SelectChannelViewHolder(view, listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listIndex = position - headerCt
        when (getItemViewType(position)) {
            VIEW_TYPE_HEADER -> (holder as HeaderViewHolder).setTitle(R.string.post_recipient_title_channel)
            VIEW_TYPE_CHANNEL -> (holder as SelectChannelViewHolder).setChannel(channels[listIndex])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return VIEW_TYPE_HEADER
        }
        return VIEW_TYPE_CHANNEL
    }

    fun setChannels(channels: List<Recipient>) {
        this.channels.clear()
        this.channels.addAll(channels)
        notifyDataSetChanged()
    }

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_CHANNEL = 1
    }

}