package com.github.rmitsubayashi.presentation.settings

import com.github.rmitsubayashi.domain.error.ValidationError
import com.github.rmitsubayashi.domain.interactor.SettingsInteractor
import com.github.rmitsubayashi.domain.model.MessageTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessageTemplatePresenter(
    private val messageTemplateDialogView: MessageTemplateContract.View,
    private val settingsInteractor: SettingsInteractor
) : MessageTemplateContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext = job + Dispatchers.IO

    private var oldMessageTemplate: MessageTemplate? = null
    private var newMessageTemplate: MessageTemplate? = null

    override fun start() {
        deselectCurrentMessageTemplate()
        loadMessageTemplates()
    }

    override fun stop() {
        job.cancel()
    }

    override fun loadMessageTemplates() {
        launch {
            val messageTemplatesResource = settingsInteractor.loadMessageTemplates()
            withContext(Dispatchers.Main) {
                when (messageTemplatesResource.error) {
                    null -> messageTemplatesResource.data?.let {
                        messageTemplateDialogView.setMessageTemplates(
                            it
                        )
                    }
                        ?: messageTemplateDialogView.showCouldNotLoadMessageTemplatesLoaded()
                    else -> messageTemplateDialogView.showCouldNotLoadMessageTemplatesLoaded()
                }
            }
        }
    }

    override fun setCurrentMessageTemplate(messageTemplate: MessageTemplate?) {
        deselectCurrentMessageTemplate()
        this.oldMessageTemplate = messageTemplate
    }

    override fun updateCurrentMessageTemplate(text: String) {
        this.newMessageTemplate = MessageTemplate(text)
    }

    override fun addCurrentMessageTemplate() {
        newMessageTemplate?.let {
            launch {
                val resultResource = settingsInteractor.addMessageTemplate(it)
                withContext(Dispatchers.Main) {
                    when (resultResource.error) {
                        null -> {
                            deselectCurrentMessageTemplate()
                            loadMessageTemplates()
                        }
                        ValidationError.EMPTY_LATE_TIME -> messageTemplateDialogView.showNoLateTimeWarning()
                        else -> messageTemplateDialogView.showCouldNotSaveMessageTemplate()
                    }
                }
            }
        }
    }

    override fun removeCurrentMessageTemplate() {
        oldMessageTemplate?.let {
            launch {
                settingsInteractor.removeMessageTemplate(it)
                loadMessageTemplates()
            }
        }
    }

    override fun editCurrentMessageTemplate() {
        oldMessageTemplate?.let { old ->
            newMessageTemplate?.let { new ->
                launch {
                    val resultResource = settingsInteractor.editMessageTemplate(old, new)
                    withContext(Dispatchers.Main) {
                        when (resultResource.error) {
                            null -> {
                                deselectCurrentMessageTemplate()
                                loadMessageTemplates()
                            }
                            ValidationError.EMPTY_LATE_TIME -> messageTemplateDialogView.showNoLateTimeWarning()
                            else -> messageTemplateDialogView.showCouldNotSaveMessageTemplate()
                        }
                    }
                }
            }
        }
    }

    override fun dismissNoLateTimeWarning() {
        messageTemplateDialogView.showAddEditDialog(newMessageTemplate)
    }

    override fun save() {
        val old = oldMessageTemplate
        val new = newMessageTemplate
        if (old == null && new != null) {
            addCurrentMessageTemplate()
        } else if (old != null && new != null) {
            editCurrentMessageTemplate()
        } else if (old != null && new == null) {
            removeCurrentMessageTemplate()
        } else {
            messageTemplateDialogView.showCouldNotSaveMessageTemplate()
        }
    }

    override fun deselectCurrentMessageTemplate() {
        oldMessageTemplate = null
        newMessageTemplate = null
        settingsInteractor.dismissMessageTemplateWarning()
    }


}