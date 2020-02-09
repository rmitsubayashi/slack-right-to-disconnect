package com.github.rmitsubayashi.slackrighttodisconnect.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.slackrighttodisconnect.R

class PostRecipientsAdapter(
    private val listener: PostRecipientRowClickListener
) : RecyclerView.Adapter<PostRecipientViewHolder>() {
    private val list = mutableListOf<Recipient>()

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostRecipientViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_post_recipient, parent, false)
        return PostRecipientViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: PostRecipientViewHolder, position: Int) {
        holder.setRecipient(list[position])
    }

    fun setRecipients(recipients: List<Recipient>) {
        this.list.clear()
        this.list.addAll(recipients)
        notifyDataSetChanged()
    }
}