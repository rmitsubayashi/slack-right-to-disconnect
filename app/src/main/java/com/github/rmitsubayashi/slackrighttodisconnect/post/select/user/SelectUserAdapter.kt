package com.github.rmitsubayashi.slackrighttodisconnect.post.select.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.post.select.HeaderViewHolder

class SelectUserAdapter (private val listener: SelectUserRowClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val users = mutableListOf<Recipient>()
    // for selecting users
    private val toggleState = mutableListOf<Boolean>()
    private val headerCt = 1
    override fun getItemCount(): Int = users.size + headerCt

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewID = when (viewType) {
            VIEW_TYPE_HEADER -> R.layout.row__select__header
            else -> R.layout.row__select__user
        }
        val view =
            LayoutInflater.from(parent.context).inflate(viewID, parent, false)
        return when (viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(view)
            else -> SelectUserViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listIndex = position-headerCt
        when (getItemViewType(position)) {
            VIEW_TYPE_HEADER -> {
                (holder as HeaderViewHolder).setTitle(R.string.label__select_user__title)
            }
            VIEW_TYPE_USER -> {
                (holder as SelectUserViewHolder).setUser(
                    users[listIndex],
                    toggleState,
                    listIndex,
                    object: SelectUserRowClickListener {
                        override fun onUserClicked(user: Recipient, selected: Boolean) {
                            listener.onUserClicked(user, selected)
                            toggleState[listIndex] = selected
                        }
                    }
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return VIEW_TYPE_HEADER
        }
        return VIEW_TYPE_USER
    }

    fun setUsers(users: List<Recipient>) {
        this.users.clear()
        this.users.addAll(users)
        this.toggleState.clear()
        for (i in 0..this.users.size) this.toggleState.add(false)
        notifyDataSetChanged()
    }

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_USER = 1
    }
}