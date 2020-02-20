package com.github.rmitsubayashi.slackrighttodisconnect.post.select.user

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import kotlinx.android.synthetic.main.row_select_user.view.*

class SelectUserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setUser(user: Recipient, toggleState: List<Boolean>, index: Int, listener: SelectUserRowClickListener) {
        itemView.row_select_user_checkbox.isChecked = toggleState[index]
        itemView.setOnClickListener {
            listener.onUserClicked(user, !toggleState[index])
            itemView.row_select_user_checkbox.toggle()
        }
        itemView.row_select_user_name.text = user.displayName
    }
}