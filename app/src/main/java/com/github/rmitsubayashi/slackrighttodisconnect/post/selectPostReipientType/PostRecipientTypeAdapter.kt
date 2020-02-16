package com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostReipientType

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.BookmarkedRecipient
import com.github.rmitsubayashi.slackrighttodisconnect.R

class PostRecipientTypeAdapter(
    private val headerListener: HeaderListener,
    private val bookmarkListener: BookmarkListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val list: MutableList<BookmarkedRecipient> = mutableListOf()
    private val headerCt = 1

    override fun getItemCount(): Int = list.size + headerCt

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_HEADER
            else -> VIEW_TYPE_BOOKMARK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewID = when (viewType) {
            VIEW_TYPE_HEADER -> R.layout.row_post_recipient_type_header
            else -> R.layout.row_bookmark
        }
        val view = LayoutInflater.from(parent.context).inflate(viewID, parent, false)
        return when (viewType) {
            VIEW_TYPE_HEADER -> PostRecipientTypeHeaderViewHolder(view).apply { setListener(headerListener) }
            else -> BookmarkViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            return
        }

        val bookmark = list[position-1]
        (holder as BookmarkViewHolder).setBookmark(bookmark, bookmarkListener)
    }

    fun setBookmarks(bookmarks: List<BookmarkedRecipient>) {
        list.clear()
        list.addAll(bookmarks)
        notifyDataSetChanged()
    }

    companion object {
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_BOOKMARK = 2
    }
}