package com.github.rmitsubayashi.slackrighttodisconnect.post.selectType

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.slackrighttodisconnect.R

class SelectTypeAdapter(
    private val headerListener: SelectTypeHeaderListener,
    private val bookmarkListener: BookmarkListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val bookmarks: MutableList<Recipient> = mutableListOf()
    private val headerCt = 1

    override fun getItemCount(): Int = bookmarks.size + headerCt

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_TYPE_HEADER
            else -> VIEW_TYPE_BOOKMARK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewID = when (viewType) {
            VIEW_TYPE_HEADER -> R.layout.row__select_type__header
            else -> R.layout.row__select_type__bookmark
        }
        val view = LayoutInflater.from(parent.context).inflate(viewID, parent, false)
        return when (viewType) {
            VIEW_TYPE_HEADER -> SelectTypeHeaderViewHolder(view).apply { setListener(headerListener) }
            else -> BookmarkViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            (holder as SelectTypeHeaderViewHolder).setEmptyBookmarks(bookmarks.isEmpty())
            return
        }

        val bookmark = bookmarks[position-1]
        (holder as BookmarkViewHolder).setBookmark(bookmark, bookmarkListener)
    }

    fun setBookmarks(newBookmarks: List<Recipient>) {
        this.bookmarks.clear()
        this.bookmarks.addAll(newBookmarks)
        notifyDataSetChanged()
    }

    fun removeBookmark(bookmark: Recipient) {
        val position = this.bookmarks.indexOf(bookmark)
        this.bookmarks.removeAt(position)
        notifyItemRemoved(position+headerCt)
        notifyItemChanged(0)
    }

    companion object {
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_BOOKMARK = 2
    }
}