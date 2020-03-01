package com.github.rmitsubayashi.slackrighttodisconnect.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.rmitsubayashi.presentation.onboarding.SlackLoginResultContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.fragment__slack_login_result.*
import kotlinx.android.synthetic.main.fragment__slack_login_result.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SlackLoginResultFragment: Fragment(), SlackLoginResultContract.View {
    private val slackLoginResultPresenter: SlackLoginResultContract.Presenter by inject{ parametersOf(this@SlackLoginResultFragment) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__slack_login_result, container, false)
        view.button__slack_login_success.setOnClickListener {
            slackLoginResultPresenter.clickFinish()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        slackLoginResultPresenter.start()
        val uri = activity?.intent?.data
        // slack api response
        val code = uri?.getQueryParameter("code")
        val state = uri?.getQueryParameter("state")
        slackLoginResultPresenter.receiveLoginResult(code, state)
    }

    override fun navigateToSelectType() {
        findNavController().navigate(R.id.action_slackTokenSuccessFragment_to_selectPostRecipientTypeFragment)
    }

    override fun showGeneralError() {
        context?.showToast(R.string.error__core__general)
    }

    override fun showLoading(loading: Boolean) {
        progressbar__slack_login.visibility = if (loading) { View.VISIBLE } else { View.INVISIBLE }
    }

    override fun showSuccess() {
        button__slack_login_success.visibility = View.VISIBLE
        label__slack_login_success.visibility = View.VISIBLE
    }

    override fun hideAppbar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun showAppbar() {
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}