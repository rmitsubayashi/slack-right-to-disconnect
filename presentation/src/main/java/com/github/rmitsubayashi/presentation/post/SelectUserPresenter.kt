package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.error.SlackError
import com.github.rmitsubayashi.domain.interactor.SelectUserInteractor
import com.github.rmitsubayashi.domain.model.Recipient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SelectUserPresenter(
    private val view: SelectUserContract.View,
    private val selectUserInteractor: SelectUserInteractor
): SelectUserContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {
        loadUsers()
    }

    private fun loadUsers() {
        launch {
            val resource = selectUserInteractor.getUsers()
            withContext(Dispatchers.Main){
                when (resource.error) {
                    null -> resource.data?.let { view.setUsers(it) } ?: view.showGeneralError()
                    NetworkError.NOT_CONNECTED -> view.showNoNetwork()
                    SlackError.TOO_MANY_USERS -> view.showTooManyUsers()
                    else -> view.showGeneralError()
                }
            }
        }
    }

    override fun stop() {
    }

    override fun toggleUser(user: Recipient, selected: Boolean) {
        if (selected) {
            selectUserInteractor.selectUser(user)
        } else {
            selectUserInteractor.deselectUser(user)
        }
    }

    override fun selectUsers() {
        launch {
            val resource = selectUserInteractor.getUserGroup()
            withContext(Dispatchers.Main) {
                when (resource.error) {
                    null -> resource.data?.let { view.navigateToPost(it) } ?: view.showGeneralError()
                    NetworkError.NOT_CONNECTED -> view.showNoNetwork()
                    SlackError.TOO_MANY_USERS -> view.showTooManySelectedUsers()
                    else -> view.showGeneralError()
                }
            }
        }
    }
}