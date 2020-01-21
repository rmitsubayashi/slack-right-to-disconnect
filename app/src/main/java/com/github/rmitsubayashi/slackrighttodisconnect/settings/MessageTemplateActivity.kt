package com.github.rmitsubayashi.slackrighttodisconnect.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.MessageTemplate
import com.github.rmitsubayashi.presentation.settings.MessageTemplateContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.serviceLocator.presenterModule
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.activity_message_template.*
import kotlinx.android.synthetic.main.dialog_add_edit_message_template.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MessageTemplateActivity : AppCompatActivity(), MessageTemplateContract.View {
    private val messageTemplatePresenter: MessageTemplateContract.Presenter by inject {
        parametersOf(
            this@MessageTemplateActivity
        )
    }

    private lateinit var listAdapter: MessageTemplateAdapter
    private lateinit var listLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_template)
        listAdapter = MessageTemplateAdapter(
            object : MessageTemplateRowClickListener {
                override fun onItemClicked(messageTemplate: MessageTemplate) {
                    messageTemplatePresenter.setCurrentMessageTemplate(messageTemplate)
                    showAddEditDialog(messageTemplate)
                }
            }
        )
        listLayoutManager = LinearLayoutManager(this)
        message_template_list.apply {
            adapter = listAdapter
            layoutManager = listLayoutManager
        }

        message_template_add.setOnClickListener {
            messageTemplatePresenter.setCurrentMessageTemplate(null)
            showAddEditDialog(null)
        }
    }

    override fun onStart() {
        super.onStart()
        messageTemplatePresenter.start()
    }

    override fun showNoLateTimeWarning() {
        AlertDialog.Builder(this)
            .setMessage(R.string.settings_warning_no_late_time)
            .setPositiveButton(R.string.settings_warning_no_late_time_confirm) { _, _ -> messageTemplatePresenter.save() }
            .setNegativeButton(R.string.settings_warning_no_late_time_dismiss) { _, _ -> messageTemplatePresenter.dismissNoLateTimeWarning() }
            .show()
    }

    override fun showCouldNotSaveMessageTemplate() {
        showToast(R.string.add_edit_message_template_save_error)
    }

    override fun setMessageTemplates(messageTemplates: List<MessageTemplate>) {
        listAdapter.setItems(messageTemplates)
    }

    override fun showCouldNotLoadMessageTemplatesLoaded() {
        showToast(R.string.settings_error_load_message_templates)
    }

    override fun showAddEditDialog(old: MessageTemplate?) {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.dialog_add_edit_message_template, null)
        view.add_edit_message_template_late_time.setOnClickListener {
            view.add_edit_message_template_input.addLateTime()
        }
        view.add_edit_message_template_input.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    messageTemplatePresenter.updateCurrentMessageTemplate(s.toString())
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
        val isAdding = old == null
        val isEditing = old != null
        if (isEditing) {
            view.add_edit_message_template_input.setText(old?.value)
        }

        val title = if (isAdding) { getString(R.string.add_edit_message_template_add_title) } else { getString(R.string.add_edit_message_template_edit_title) }
        val builder = AlertDialog.Builder(this)
            .setView(view)
            .setTitle(title)
            .setPositiveButton(R.string.add_edit_message_template_save) { _, _ ->
                messageTemplatePresenter.save()
            }
            .setNegativeButton(R.string.add_edit_message_template_cancel) { _, _ ->
                messageTemplatePresenter.deselectCurrentMessageTemplate()
            }
        if (isEditing) {
            builder.setNeutralButton(R.string.add_edit_message_template_remove) {_,_ ->
                messageTemplatePresenter.removeCurrentMessageTemplate()
            }
        }
        builder.show()
    }

}