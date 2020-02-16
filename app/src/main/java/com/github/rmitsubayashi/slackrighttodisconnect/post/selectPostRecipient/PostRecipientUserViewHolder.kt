package com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostRecipient

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.UserInfo
import kotlinx.android.synthetic.main.row_post_recipient_user.view.*

class PostRecipientUserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setRecipient(userInfo: UserInfo, toggleState: List<Boolean>, index: Int, listener: PostRecipientRowClickListener) {
        itemView.row_post_recipient_checkbox.isChecked = toggleState[index]
        itemView.setOnClickListener {
            listener.onUserItemClicked(userInfo, !toggleState[index])
            itemView.row_post_recipient_checkbox.toggle()
        }
        itemView.row_post_recipient_name.text = userInfo.name
    }
}