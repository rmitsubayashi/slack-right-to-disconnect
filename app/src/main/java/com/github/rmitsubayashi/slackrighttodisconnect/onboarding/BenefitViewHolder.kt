package com.github.rmitsubayashi.slackrighttodisconnect.onboarding

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view__onboarding__benefit.view.*

class BenefitViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun setBenefit(item: BenefitItem) {
        itemView.image__benefits.setImageDrawable(
            itemView.context.getDrawable(item.imageID)
        )
        itemView.label__benefits__title.text = item.title
        itemView.label__benefits__description.text = item.description
    }
}