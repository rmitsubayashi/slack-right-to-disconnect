package com.github.rmitsubayashi.slackrighttodisconnect.post.select

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.slackrighttodisconnect.R
import kotlinx.android.synthetic.main.row_post_recipient_header.view.*

class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setTitle(resID: Int) {
        itemView.row_post_recipient_title.text = itemView.context.getString(resID)
    }
}