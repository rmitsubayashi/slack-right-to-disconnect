package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.interactor.BookmarkInteractor
import com.github.rmitsubayashi.domain.model.BookmarkedRecipient
import com.github.rmitsubayashi.presentation.post.model.RecipientType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SelectRecipientTypePresenter (
    private val view: SelectPostRecipientTypeContract.View,
    private val bookmarkInteractor: BookmarkInteractor
): SelectPostRecipientTypeContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {
        loadBookmarks()
    }

    private fun loadBookmarks() {
        launch {
            val bookmarksResource = bookmarkInteractor.getBookmarks()
            withContext(Dispatchers.Main) {
                bookmarksResource.data?.let {
                    view.setBookmarks(it)
                }
            }
        }
    }

    override fun stop() {

    }

    override fun selectPostRecipientType(type: RecipientType) {
        view.navigateToSelectPostRecipient(type)
    }

    override fun selectBookmark(bookmark: BookmarkedRecipient) {
        view.navigateToPost(bookmark)
    }
}