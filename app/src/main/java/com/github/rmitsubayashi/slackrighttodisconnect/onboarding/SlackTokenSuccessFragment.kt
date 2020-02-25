package com.github.rmitsubayashi.slackrighttodisconnect.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.rmitsubayashi.presentation.onboarding.SlackTokenSuccessContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.fragment__slack_token_success.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SlackTokenSuccessFragment: Fragment(), SlackTokenSuccessContract.View {
    private val slackTokenSuccessPresenter: SlackTokenSuccessContract.Presenter by inject{ parametersOf(this@SlackTokenSuccessFragment) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__slack_token_success, container, false)
        view.button__slack_token_success.setOnClickListener {
            slackTokenSuccessPresenter.clickFinish()
        }
        return view
    }

    override fun navigateToSelectType() {
        findNavController().navigate(R.id.action_slackTokenSuccessFragment_to_selectPostRecipientTypeFragment)
    }

    override fun showGeneralError() {
        context?.showToast(R.string.error__core__general)
    }
}