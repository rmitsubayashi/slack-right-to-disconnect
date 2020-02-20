package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.repository.BookmarkRepository

class BookmarkInteractor(private val bookmarkRepository: BookmarkRepository) {
    suspend fun getBookmarks(): Resource<List<Recipient>> {
        return bookmarkRepository.getBookmarks()
    }

    suspend fun saveBookmark(recipient: Recipient): Resource<Unit> {
        return bookmarkRepository.saveBookmark(recipient)
    }

    suspend fun deleteBookmark(recipient: Recipient): Resource<Unit> {
        return bookmarkRepository.removeBookmark(recipient)
    }

    suspend fun canBookmark(recipient: Recipient, threadID: String?): Resource<Boolean> {
        if (threadID != null) {
            return Resource.success(false)
        }
        val existsResource = bookmarkRepository.bookmarkExists(recipient)
        if (existsResource.error != null) {
            return Resource.error(existsResource.error)
        }
        existsResource.data?.let {
            exists -> return Resource.success(!exists)
        } ?: return Resource.error()
    }
}