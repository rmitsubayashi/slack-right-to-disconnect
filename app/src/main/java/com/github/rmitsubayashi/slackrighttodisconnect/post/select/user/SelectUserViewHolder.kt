package com.github.rmitsubayashi.slackrighttodisconnect.post.select.user

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import kotlinx.android.synthetic.main.row__select__user.view.*

class SelectUserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setUser(user: Recipient, toggleState: List<Boolean>, index: Int, listener: SelectUserRowClickListener) {
        itemView.checkbox__row_select_user__select.isChecked = toggleState[index]
        itemView.setOnClickListener {
            listener.onUserClicked(user, !toggleState[index])
            itemView.checkbox__row_select_user__select.toggle()
        }
        itemView.label__row_select_user__name.text = user.displayName
    }
}