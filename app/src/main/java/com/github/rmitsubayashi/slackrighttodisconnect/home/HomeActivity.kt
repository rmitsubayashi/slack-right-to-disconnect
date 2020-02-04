package com.github.rmitsubayashi.slackrighttodisconnect.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.presentation.home.HomeContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.settings.SettingsActivity
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class HomeActivity : AppCompatActivity(), HomeContract.View {
    private val homePresenter: HomeContract.Presenter by inject { parametersOf(this@HomeActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        home_send.setOnClickListener { homePresenter.postToSlack() }
        home_message.addTextChangedListener(
            object: TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    homePresenter.updateMessage(s.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }
            }
        )
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

    override fun showPostError(errorMessage: String) {
        home_progress_bar.visibility = View.GONE
        showToast(getString(R.string.home_failure_message_posted, errorMessage))
    }
}