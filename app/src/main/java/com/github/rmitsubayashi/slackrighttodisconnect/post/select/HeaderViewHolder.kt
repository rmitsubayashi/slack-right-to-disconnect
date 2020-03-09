package com.github.rmitsubayashi.slackrighttodisconnect.post.select

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row__select__header.view.*

class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setTitle(resID: Int) {
        itemView.label__row_select__title.text = itemView.context.getString(resID)
    }
}