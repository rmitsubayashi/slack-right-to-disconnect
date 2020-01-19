package com.github.rmitsubayashi.slackrighttodisconnect.settings

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.MessageTemplate
import kotlinx.android.synthetic.main.row_message_template.view.*

class MessageTemplateViewHolder(itemView: View, private val listener: MessageTemplateRowClickListener)
    : RecyclerView.ViewHolder(itemView) {

    fun setMessageTemplate(messageTemplate: MessageTemplate) {
        itemView.message_template_row.text = messageTemplate.value
        itemView.setOnClickListener {
            listener.onItemClicked(messageTemplate)
        }
    }
}