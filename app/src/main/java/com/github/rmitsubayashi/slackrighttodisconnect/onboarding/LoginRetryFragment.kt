package com.github.rmitsubayashi.slackrighttodisconnect.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.rmitsubayashi.data.BuildConfig
import com.github.rmitsubayashi.presentation.onboarding.LoginRetryContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.fragment__login_retry.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class LoginRetryFragment: Fragment(), LoginRetryContract.View {
    private val loginRetryPresenter: LoginRetryContract.Presenter by inject { parametersOf(this@LoginRetryFragment) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__login_retry, container, false)
        view.button__login_retry.setOnClickListener { loginRetryPresenter.clickRetry() }
        return view
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

    override fun getSlackClientID(): String = BuildConfig.SLACK_CLIENT_ID

    override fun hideAppBar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun showGeneralError() {
        context?.showToast(R.string.error__core__general)
    }
}