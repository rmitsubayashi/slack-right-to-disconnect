package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.interactor.BookmarkInteractor
import com.github.rmitsubayashi.domain.interactor.OnboardingInteractor
import com.github.rmitsubayashi.domain.model.Recipient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SelectTypePresenter (
    private val view: SelectTypeContract.View,
    private val bookmarkInteractor: BookmarkInteractor,
    private val onboardingInteractor: OnboardingInteractor
): SelectTypeContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {
        launch {
            if (onboardingInteractor.shouldOnboard().data == true) {
                withContext(Dispatchers.Main) {
                    view.navigateToOnboarding()
                }
                return@launch
            }
            loadBookmarks()
        }
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

    override fun selectSelectChannel() {
        view.navigateToSelectChannel()
    }

    override fun selectSelectRecentThread() {
        view.navigateToSelectRecentThread()
    }

    override fun selectSelectUser() {
        view.navigateToSelectUser()
    }

    override fun selectBookmark(bookmark: Recipient) {
        view.navigateToPost(bookmark)
    }

    override fun removeBookmark(bookmark: Recipient) {
        launch {
            bookmarkInteractor.deleteBookmark(bookmark)
            withContext(Dispatchers.Main) {
                view.removeBookmark(bookmark)
            }
        }
    }
}