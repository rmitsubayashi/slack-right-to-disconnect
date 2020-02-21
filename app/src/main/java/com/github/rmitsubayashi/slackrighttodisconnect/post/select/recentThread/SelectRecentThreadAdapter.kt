package com.github.rmitsubayashi.slackrighttodisconnect.post.select.recentThread

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.post.select.HeaderViewHolder

class SelectRecentThreadAdapter(
    private val listener: SelectRecentThreadRowClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val recentThreads = mutableListOf<Message>()
    private val headerCt = 1
    override fun getItemCount(): Int = recentThreads.size + headerCt

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewID = when (viewType) {
            VIEW_TYPE_HEADER -> R.layout.row__select__header
            else -> R.layout.row__select__thread
        }
        val view =
            LayoutInflater.from(parent.context).inflate(viewID, parent, false)
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(view)
            else -> SelectRecentThreadViewHolder(view, listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listIndex = position - headerCt
        when (getItemViewType(position)) {
            VIEW_TYPE_HEADER -> (holder as HeaderViewHolder).setTitle(R.string.label__select_thread__title)
            VIEW_TYPE_THREAD -> (holder as SelectRecentThreadViewHolder).setRecentThread(recentThreads[listIndex])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return VIEW_TYPE_HEADER
        }
        return VIEW_TYPE_THREAD
    }

    fun setRecentThreads(threads: List<Message>) {
        this.recentThreads.clear()
        this.recentThreads.addAll(threads)
        notifyDataSetChanged()
    }

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_THREAD = 1
    }

}