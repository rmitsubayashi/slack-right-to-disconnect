package com.github.rmitsubayashi.slackrighttodisconnect.post.select.recentThread

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.slackrighttodisconnect.R
import kotlinx.android.synthetic.main.row__select__thread.view.*
import java.text.SimpleDateFormat
import java.util.*

class SelectRecentThreadViewHolder(
    itemView: View,
    private val listener: SelectRecentThreadRowClickListener
) : RecyclerView.ViewHolder(itemView) {
    fun setRecentThread(message: Message) {
        itemView.label__row_select_thread__message.text = message.message
        itemView.label__row_select_thread__date.text = formatDate(message.date)
        itemView.label__row_select_thread__name.text = formatRecipient(message.recipient)
        itemView.setOnClickListener { listener.onRecentThreadClicked(message) }
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat(
            itemView.context.getString(R.string.label__select_thread__date_format),
            Locale.getDefault()
        )
        return dateFormat.format(date)
    }

    private fun formatRecipient(recipient: Recipient): String {
        return when (recipient.recipientType) {
            RecipientType.USER -> {
                itemView.context.getString(R.string.label__select_thread__user_format, recipient.displayName)
            }
            RecipientType.CHANNEL -> {
                itemView.context.getString(R.string.label__select_thread__channel_format, recipient.displayName)
            }
        }
    }
}