package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.presentation.post.model.RecipientType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class SelectRecipientTypePresenter (
    private val view: SelectPostRecipientTypeContract.View
): SelectPostRecipientTypeContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {

    }

    override fun stop() {

    }

    override fun selectPostRecipientType(type: RecipientType) {
        view.navigateToSelectPostRecipient(type)
    }
}