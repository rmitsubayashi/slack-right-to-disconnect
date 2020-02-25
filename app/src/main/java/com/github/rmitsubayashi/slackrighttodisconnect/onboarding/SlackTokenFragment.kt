package com.github.rmitsubayashi.slackrighttodisconnect.onboarding

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.rmitsubayashi.presentation.onboarding.SlackTokenContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.fragment__slack_token.*
import kotlinx.android.synthetic.main.fragment__slack_token.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SlackTokenFragment: Fragment(),SlackTokenContract.View {
    private val slackTokenPresenter: SlackTokenContract.Presenter by inject{ parametersOf(this@SlackTokenFragment) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__slack_token, container, false)
        view.button__slack_token__verify.setOnClickListener {
            val text = edittext__slack_token.text.toString()
            slackTokenPresenter.verifyToken(text)
        }
        view.label__slack_token__guide.movementMethod = LinkMovementMethod.getInstance()
        return view
    }

    override fun navigateToSlackTokenSuccess() {
        val action = SlackTokenFragmentDirections.actionSlackTokenFragmentToSlackTokenSuccessFragment()
        findNavController().navigate(action)
    }

    override fun showValidationError() {
        context?.showToast(R.string.error__settings__invalid_slack_token)
    }

    override fun showNoNetwork() {
        context?.showToast(R.string.error__core__no_network)
    }

    override fun showGeneralError() {
        context?.showToast(R.string.error__core__general)
    }
}