package com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostReipientType

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.BookmarkedRecipient
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.slackrighttodisconnect.R
import kotlinx.android.synthetic.main.row_bookmark.view.*

class BookmarkViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setBookmark(bookmarkedRecipient: BookmarkedRecipient, listener: BookmarkListener) {
        itemView.setOnClickListener {
            listener.onBookmarkClicked(bookmarkedRecipient)
        }
        val drawableID = when (bookmarkedRecipient.recipientType) {
            RecipientType.USER -> R.drawable.ic_users
            RecipientType.CHANNEL -> R.drawable.ic_channel
            RecipientType.THREAD -> R.drawable.ic_thread
        }
        itemView.row_bookmark_icon.setImageDrawable(
            itemView.context.getDrawable(drawableID)
        )
        itemView.row_bookmark_label.text = bookmarkedRecipient.name
    }
}