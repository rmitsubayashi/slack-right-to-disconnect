package com.github.rmitsubayashi.slackrighttodisconnect.settings

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.github.rmitsubayashi.domain.model.SlackChannel
import com.github.rmitsubayashi.domain.model.SlackChannelID
import com.github.rmitsubayashi.domain.model.SlackToken
import com.github.rmitsubayashi.presentation.settings.SettingsContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.dialog_set_slack_token.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SettingsFragment : PreferenceFragmentCompat(), SettingsContract.View {
    private val settingsPresenter: SettingsContract.Presenter by inject { parametersOf(this@SettingsFragment) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_settings, rootKey)
        val slackTokenPref: Preference? =
            findPreference(getString(R.string.key_slack_token))
        slackTokenPref?.setOnPreferenceClickListener {
            showSlackTokenEditor()
            false
        }
        val slackChannelPref: ListPreference? =
            findPreference(getString(R.string.key_slack_channel_id))
        slackChannelPref?.setOnPreferenceChangeListener { _, newValue ->
            val str: String = newValue as String
            settingsPresenter.saveSlackChannel(SlackChannelID(str))
            false
        }
        val messageTemplatesPref: Preference? =
            findPreference(getString(R.string.key_message_template))
        messageTemplatesPref?.setOnPreferenceClickListener {
            showMessageTemplateSelector()
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

    override fun showMessageTemplateSelector() {
        startActivity(Intent(activity, MessageTemplateActivity::class.java))
    }

    private fun showSlackTokenEditor() {
        context?.let {
            val view = LayoutInflater.from(it).inflate(R.layout.dialog_set_slack_token, null)
            view.set_slack_token_link.movementMethod = LinkMovementMethod.getInstance()
            AlertDialog.Builder(it)
                .setView(view)
                .setTitle(R.string.title_slack_token)
                .setPositiveButton(R.string.settings_slack_token_confirm) { _, _ ->
                    val token = view.set_slack_token_edit_text.text.toString()
                    settingsPresenter.saveSlackToken(SlackToken(token))

                }.show()
        }
    }

    override fun showInvalidSlackToken() {
        context?.showToast(R.string.settings_error_invalid_slack_token)
    }

    override fun showInvalidSlackChannel() {
        context?.showToast(R.string.settings_error_invalid_slack_channel_id)
    }

    override fun updateSlackChannelSettingSummary(channel: SlackChannel?) {
        findPreference<ListPreference>(getString(R.string.key_slack_channel_id))?.apply {
            summary =
                if (channel == null || channel.name.isEmpty()) {
                    getString(R.string.settings_slack_channel_id_not_set)
                } else {
                    channel.name
                }

            value = channel?.id?.value
        }

    }

    override fun updateSlackTokenSettingSummary(isSet: Boolean) {
        findPreference<Preference>(getString(R.string.key_slack_token))?.summary =
            if (isSet) {
                getString(R.string.settings_slack_token_set)
            } else {
                getString(R.string.settings_slack_token_not_set)
            }
    }

    override fun updateSlackChannelList(channelNames: List<SlackChannel>?) {
        if (channelNames == null || channelNames.isEmpty()) {
            findPreference<ListPreference>(getString(R.string.key_slack_channel_id))?.isEnabled = false
        } else {
            findPreference<ListPreference>(getString(R.string.key_slack_channel_id))?.apply {
                entries = channelNames.map { it.name }.toTypedArray()
                entryValues = channelNames.map { it.id.value }.toTypedArray()
                isEnabled = true
            }
        }
    }
}