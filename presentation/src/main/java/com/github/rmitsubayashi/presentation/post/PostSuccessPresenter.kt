package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.interactor.BookmarkInteractor
import com.github.rmitsubayashi.domain.model.BookmarkedRecipient
import com.github.rmitsubayashi.domain.model.Recipient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class PostSuccessPresenter(
    private val view: PostSuccessContract.View,
    private val bookmarkInteractor: BookmarkInteractor
): PostSuccessContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {
    }

    override fun stop() {

    }

    override fun checkBookmark(recipient: Recipient){
        launch {
            val resource = bookmarkInteractor.bookmarkExists(recipient)
            withContext(Dispatchers.Main) {
                resource.data?.let {
                    exists -> view.enableBookmarkButton(!exists)
                } ?: view.enableBookmarkButton(false)
            }
        }
    }

    override fun bookmark(recipient: BookmarkedRecipient) {
        launch {
            bookmarkInteractor.saveBookmark(recipient)
            withContext(Dispatchers.Main) {
                view.enableBookmarkButton(false)
            }
        }
    }
}