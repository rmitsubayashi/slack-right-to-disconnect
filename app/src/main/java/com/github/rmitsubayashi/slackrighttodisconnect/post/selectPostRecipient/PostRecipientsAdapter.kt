package com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostRecipient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.model.SlackChannelInfo
import com.github.rmitsubayashi.domain.model.ThreadInfo
import com.github.rmitsubayashi.domain.model.UserInfo
import com.github.rmitsubayashi.slackrighttodisconnect.R

class PostRecipientsAdapter(
    private val listener: PostRecipientRowClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val list = mutableListOf<Recipient>()

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewID = when (viewType) {
            VIEW_TYPE_USER -> R.layout.row_post_recipient
            VIEW_TYPE_CHANNEL -> R.layout.row_post_recipient
            VIEW_TYPE_RECENT_THREAD -> R.layout.row_post_recipient_thread
            else -> R.layout.row_post_recipient
        }
        val view =
            LayoutInflater.from(parent.context).inflate(viewID, parent, false)
        return when (viewType) {
            VIEW_TYPE_USER -> PostRecipientViewHolder(
                view,
                listener
            )
            VIEW_TYPE_CHANNEL -> PostRecipientViewHolder(
                view,
                listener
            )
            VIEW_TYPE_RECENT_THREAD -> PostRecipientThreadViewHolder(
                view,
                listener
            )
            else -> PostRecipientViewHolder(
                view,
                listener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_USER -> {
                (holder as PostRecipientViewHolder).setRecipient(list[position])
            }
            VIEW_TYPE_CHANNEL -> (holder as PostRecipientViewHolder).setRecipient(list[position])
            VIEW_TYPE_RECENT_THREAD -> (holder as PostRecipientThreadViewHolder).setRecipient(list[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is UserInfo -> VIEW_TYPE_USER
            is SlackChannelInfo -> VIEW_TYPE_CHANNEL
            is ThreadInfo -> VIEW_TYPE_RECENT_THREAD
            else -> VIEW_TYPE_USER
        }
    }

    fun setRecipients(recipients: List<Recipient>) {
        this.list.clear()
        this.list.addAll(recipients)
        notifyDataSetChanged()
    }

    companion object {
        const val VIEW_TYPE_USER = 1
        const val VIEW_TYPE_CHANNEL = 2
        const val VIEW_TYPE_RECENT_THREAD = 3
    }
}