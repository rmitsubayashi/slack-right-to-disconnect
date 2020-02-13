package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.BookmarkedRecipient
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.repository.BookmarkRepository

class BookmarkInteractor(private val bookmarkRepository: BookmarkRepository) {
    suspend fun getBookmarks(): Resource<List<BookmarkedRecipient>> {
        return bookmarkRepository.getBookmarks()
    }

    suspend fun saveBookmark(recipient: Recipient): Resource<Unit> {
        return bookmarkRepository.saveBookmark(recipient)
    }

    suspend fun deleteBookmark(recipient: Recipient): Resource<Unit> {
        return bookmarkRepository.removeBookmark(recipient)
    }

    suspend fun bookmarkExists(recipient: Recipient): Resource<Boolean> {
        return bookmarkRepository.bookmarkExists(recipient)
    }
}