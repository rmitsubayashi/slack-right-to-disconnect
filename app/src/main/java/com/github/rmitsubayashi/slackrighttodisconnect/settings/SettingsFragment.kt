package com.github.rmitsubayashi.slackrighttodisconnect.settings

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.github.rmitsubayashi.domain.model.SlackToken
import com.github.rmitsubayashi.domain.model.SlackTokenInfo
import com.github.rmitsubayashi.presentation.settings.SettingsContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.dialog__set_slack_token.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SettingsFragment : PreferenceFragmentCompat(), SettingsContract.View {
    private val settingsPresenter: SettingsContract.Presenter by inject { parametersOf(this@SettingsFragment) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_settings, rootKey)
        val slackTokenPref: Preference? =
            findPreference(getString(R.string.key__settings_slack_token))
        slackTokenPref?.setOnPreferenceClickListener {
            showSlackTokenEditor()
            false
        }
    }

    override fun onStart() {
        super.onStart()
        settingsPresenter.start()
    }

    override fun onStop() {
        super.onStop()
        settingsPresenter.stop()
    }

    private fun showSlackTokenEditor() {
        context?.let {
            val view = LayoutInflater.from(it).inflate(R.layout.dialog__set_slack_token, null)
            view.set_slack_token_link.movementMethod = LinkMovementMethod.getInstance()
            AlertDialog.Builder(it)
                .setView(view)
                .setTitle(R.string.label__settings__slack_token)
                .setPositiveButton(R.string.button__settings__slack_token_confirm) { _, _ ->
                    val token = view.set_slack_token_edit_text.text.toString()
                    settingsPresenter.saveSlackToken(SlackToken(token))

                }.show()
        }
    }

    override fun showInvalidSlackToken() {
        context?.showToast(R.string.error__settings__invalid_slack_token)
    }

    override fun updateSlackTokenSettingSummary(slackTokenInfo: SlackTokenInfo?) {
        findPreference<Preference>(getString(R.string.key__settings_slack_token))?.summary =
            if (slackTokenInfo == null) {
                getString(R.string.label__settings__slack_token_not_set)
            } else {
                getString(R.string.label__settings__slack_token_info, slackTokenInfo.user, slackTokenInfo.team)
            }
    }

    override fun showGeneralError() {
        context?.showToast(R.string.error__core__general)
    }

    override fun showSaved() {
        context?.showToast(R.string.toast__settings__saved)
    }
}