package com.github.rmitsubayashi.slackrighttodisconnect.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.MessageTemplate
import com.github.rmitsubayashi.slackrighttodisconnect.R

class MessageTemplateAdapter(
    private val listener: MessageTemplateRowClickListener
): RecyclerView.Adapter<MessageTemplateViewHolder>() {
    private val list = mutableListOf<MessageTemplate>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageTemplateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_message_template, parent, false)
        return MessageTemplateViewHolder(view, listener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MessageTemplateViewHolder, position: Int) {
        holder.setMessageTemplate(list[position])
    }

    fun setItems(messageTemplates: List<MessageTemplate>) {
        val diffCallback = DiffCallback(list, messageTemplates)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(messageTemplates)
        diffResult.dispatchUpdatesTo(this)
    }

    class DiffCallback(private val oldList: List<MessageTemplate>, private val newList: List<MessageTemplate>)
        : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
                = oldList[oldItemPosition].value == newList[newItemPosition].value

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
            = oldList[oldItemPosition].value == newList[newItemPosition].value
    }

}