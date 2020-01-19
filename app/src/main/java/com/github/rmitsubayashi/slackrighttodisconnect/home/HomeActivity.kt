package com.github.rmitsubayashi.slackrighttodisconnect.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.rmitsubayashi.domain.model.LateTime
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.MessageTemplate
import com.github.rmitsubayashi.presentation.home.HomeContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.settings.SettingsActivity
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.dialog_set_late_time.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class HomeActivity : AppCompatActivity(), HomeContract.View {
    private val homePresenter: HomeContract.Presenter by inject { parametersOf(this@HomeActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        home_send.setOnClickListener { homePresenter.postToSlack() }
        home_late_time.setOnClickListener { openLateTimePicker() }
    }

    override fun onStart() {
        super.onStart()
        homePresenter.start()
    }

    override fun onStop() {
        super.onStop()
        homePresenter.stop()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app_bar_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home_menu_settings -> {
                navigateToSettings()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setMessageTemplates(templates: List<MessageTemplate>) {
        home_spinner.adapter = ArrayAdapter<String>(
            this, R.layout.row_spinner_message_template, templates.map { it.value }
        )
        home_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                val selectedText = home_spinner.selectedItem as String
                homePresenter.updateMessageTemplate(MessageTemplate(selectedText))
            }
        }
    }

    override fun showConfirmMessage(message: Message) {
        AlertDialog.Builder(this)
            .setTitle(R.string.home_confirm_message_title)
            .setMessage(message.value)
            .setPositiveButton(
                R.string.home_confirm_message_confirm
            ) { _, _ ->
                homePresenter.postToSlack()
            }
            .setNegativeButton(R.string.home_confirm_message_cancel) { _, _ -> }
            .show()
    }

    override fun showInputError() {
        home_progress_bar.visibility = View.GONE
        showToast(R.string.home_error_message_invalid_input)
    }

    override fun showPostSuccess() {
        home_progress_bar.visibility = View.GONE
        showToast(R.string.home_success_message_posted)
    }

    override fun showPostSending() {
        home_progress_bar.visibility = View.VISIBLE
    }

    override fun navigateToSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun showEmptyMessageTemplatesError() {
        showToast(R.string.home_failure_no_message_template_selected)
    }

    override fun showPostError(errorMessage: String) {
        home_progress_bar.visibility = View.GONE
        showToast(getString(R.string.home_failure_message_posted, errorMessage))
    }

    override fun openLateTimePicker() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_set_late_time, null)
        val hourArr =
            resources.getIntArray(R.array.set_late_time_hour)
        val minArr =
            resources.getIntArray(R.array.set_late_time_minute)

        view.set_late_time_hour.apply {
            minValue = 1
            maxValue = hourArr.size
            displayedValues = hourArr.map { it.toString() }.toTypedArray()
        }
        view.set_late_time_minutes.apply {
            minValue = 1
            maxValue = minArr.size
            displayedValues = minArr.map { it.toString() }.toTypedArray()
        }
        AlertDialog.Builder(this)
            .setPositiveButton(R.string.home_late_time_set) { _, _ ->
                val hourIndex = view.set_late_time_hour.value - 1
                val hour = hourArr[hourIndex]
                val minuteIndex = view.set_late_time_minutes.value - 1
                val minute = minArr[minuteIndex]
                homePresenter.updateLateTime(LateTime(hour, minute))
            }
            .setNegativeButton(R.string.home_late_time_cancel) { _, _ -> }
            .setTitle(R.string.home_late_time_title)
            .setView(view)
            .show()
    }

    override fun setLateTimeButtonText(text: String?) {
        if (text == null || text.isEmpty()) {
            home_late_time.setText(R.string.home_late_time_default)
        } else {
            home_late_time.text = text
        }
    }

    override fun setPreview(text: String) {
        home_preview.text = text
    }
}