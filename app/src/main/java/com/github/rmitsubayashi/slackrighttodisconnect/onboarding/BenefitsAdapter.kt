package com.github.rmitsubayashi.slackrighttodisconnect.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.slackrighttodisconnect.R

class BenefitsAdapter(private val benefitItems: List<BenefitItem>): RecyclerView.Adapter<BenefitViewHolder>() {
    override fun getItemCount(): Int = benefitItems.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BenefitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.view__onboarding__benefit, parent, false
        )
        return BenefitViewHolder(view)
    }

    override fun onBindViewHolder(holder: BenefitViewHolder, position: Int) {
        val benefit = benefitItems[position]
        holder.setBenefit(benefit)
    }
}