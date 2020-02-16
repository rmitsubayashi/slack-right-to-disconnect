package com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostRecipient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.*
import com.github.rmitsubayashi.slackrighttodisconnect.R

class PostRecipientsAdapter(
    private val listener: PostRecipientRowClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val list = mutableListOf<Recipient>()
    // for selecting users
    private val toggleState = mutableListOf<Boolean>()
    private var recipientType: RecipientType? = null
    private val headerCt = 1
    override fun getItemCount(): Int = list.size + headerCt

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewID = when (viewType) {
            VIEW_TYPE_HEADER -> R.layout.row_post_recipient_header
            VIEW_TYPE_USER -> R.layout.row_post_recipient_user
            VIEW_TYPE_CHANNEL -> R.layout.row_post_recipient
            VIEW_TYPE_RECENT_THREAD -> R.layout.row_post_recipient_thread
            else -> R.layout.row_post_recipient
        }
        val view =
            LayoutInflater.from(parent.context).inflate(viewID, parent, false)
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(view)
            VIEW_TYPE_USER -> PostRecipientUserViewHolder(
                view
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
        val actualPosition = position-1
        when (getItemViewType(position)) {
            VIEW_TYPE_HEADER -> {
                (holder as HeaderViewHolder).setTitle(recipientType)
            }
            VIEW_TYPE_USER -> {
                (holder as PostRecipientUserViewHolder).setRecipient(
                    list[actualPosition] as UserInfo,
                    toggleState,
                    actualPosition,
                    object: PostRecipientRowClickListener {
                        override fun onItemClicked(recipient: Recipient) {
                            listener.onItemClicked(recipient)
                        }

                        override fun onUserItemClicked(user: UserInfo, selected: Boolean) {
                            listener.onUserItemClicked(user, selected)
                            toggleState[actualPosition] = selected
                        }
                    }
                )
            }
            VIEW_TYPE_CHANNEL -> (holder as PostRecipientViewHolder).setRecipient(list[actualPosition])
            VIEW_TYPE_RECENT_THREAD -> (holder as PostRecipientThreadViewHolder).setRecipient(list[actualPosition])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return VIEW_TYPE_HEADER
        }
        return when (list[position-1]) {
            is UserInfo -> VIEW_TYPE_USER
            is SlackChannelInfo -> VIEW_TYPE_CHANNEL
            is ThreadInfo -> VIEW_TYPE_RECENT_THREAD
            else -> VIEW_TYPE_USER
        }
    }

    fun setRecipientType(recipientType: RecipientType) {
        this.recipientType = recipientType
        notifyItemChanged(0)
    }

    fun setRecipients(recipients: List<Recipient>) {
        this.list.clear()
        this.list.addAll(recipients)
        if (this.recipientType == RecipientType.USER) {
            this.toggleState.clear()
            for (i in 0..this.list.size) this.toggleState.add(false)
        }
        notifyDataSetChanged()
    }

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_USER = 1
        const val VIEW_TYPE_CHANNEL = 2
        const val VIEW_TYPE_RECENT_THREAD = 3
    }
}