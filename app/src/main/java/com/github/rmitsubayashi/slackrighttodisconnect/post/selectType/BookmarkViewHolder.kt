package com.github.rmitsubayashi.slackrighttodisconnect.post.selectType

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.slackrighttodisconnect.R
import kotlinx.android.synthetic.main.row__select_type__bookmark.view.*

class BookmarkViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setBookmark(bookmarkedRecipient: Recipient, listener: BookmarkListener) {
        itemView.setOnClickListener {
            listener.onBookmarkClicked(bookmarkedRecipient)
        }
        itemView.setOnLongClickListener {
            listener.onBookmarkLongClicked(bookmarkedRecipient)
            true
        }
        val drawableID = when (bookmarkedRecipient.recipientType) {
            RecipientType.USER -> R.drawable.ic_users
            RecipientType.CHANNEL -> R.drawable.ic_channel
        }
        itemView.image__row_select_type__bookmark_type.setImageDrawable(
            itemView.context.getDrawable(drawableID)
        )
        itemView.label__row_select_type__bookmark.text = bookmarkedRecipient.displayName
    }
}