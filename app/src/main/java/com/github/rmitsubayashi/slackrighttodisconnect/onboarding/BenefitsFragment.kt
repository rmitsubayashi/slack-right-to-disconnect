package com.github.rmitsubayashi.slackrighttodisconnect.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.github.rmitsubayashi.presentation.onboarding.BenefitsContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.fragment__benefits.*
import kotlinx.android.synthetic.main.fragment__benefits.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class BenefitsFragment : Fragment(), BenefitsContract.View {
    private lateinit var pagerAdapter: BenefitsAdapter
    private lateinit var dots: List<ImageView>
    private val benefitsPresenter: BenefitsContract.Presenter by inject{ parametersOf(this@BenefitsFragment) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__benefits, container, false)
        pagerAdapter = BenefitsAdapter(getPagerItems())
        view.pager__benefits.adapter = pagerAdapter
        addDots(view)
        view.pager__benefits.currentItem = 0
        dots[0].setImageDrawable(context?.getDrawable(R.drawable.dot_active))
        view.pager__benefits.registerOnPageChangeCallback(
            object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    for (i in dots) {
                        i.setImageDrawable(context?.getDrawable(R.drawable.dot_inactive))
                    }
                    dots[position].setImageDrawable(context?.getDrawable(R.drawable.dot_active))
                    button__benefits__get_started.visibility = if (position == pagerAdapter.itemCount - 1) {
                        View.VISIBLE
                    } else {
                        View.INVISIBLE
                    }
                }
            }
        )
        view.button__benefits__get_started.setOnClickListener {
            benefitsPresenter.clickNext()
        }
        return view
    }

    private fun getPagerItems(): List<BenefitItem> =
        listOf(
            BenefitItem(
                R.drawable.plug,
                getString(R.string.label__onboarding__unplug_title),
                getString(R.string.label__onboarding__unplug_description)
            ),
            BenefitItem(
                R.drawable.block,
                getString(R.string.label__onboarding__receive_title),
                getString(R.string.label__onboarding__receive_description)
            ),
            BenefitItem(
                R.drawable.good,
                getString(R.string.label__onboarding__send_title),
                getString(R.string.label__onboarding__send_description)
            )
        )

    private fun addDots(parent: View) {
        val tempDots = mutableListOf<ImageView>()
        for (i in 0 until pagerAdapter.itemCount) {
            val imageView = ImageView(context)
            imageView.setImageDrawable(context?.getDrawable(R.drawable.dot_inactive))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            tempDots.add(imageView)
            parent.dots__benefits.addView(imageView, params)
        }
        dots = tempDots
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        benefitsPresenter.start()
    }

    override fun hideAppBar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun openURL(url: String) {
        val page = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, page)
        activity?.let {
            if (intent.resolveActivity(it.packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    override fun showGeneralError() {
        context?.showToast(R.string.error__core__general)
    }
}