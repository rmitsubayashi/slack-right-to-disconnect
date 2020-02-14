package com.github.rmitsubayashi.data.repository

import com.github.rmitsubayashi.data.dao.BookmarkDao
import com.github.rmitsubayashi.data.model.Bookmark
import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.BookmarkedRecipient
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.repository.BookmarkRepository

class BookmarkDataRepository(private val bookmarkDao: BookmarkDao): BookmarkRepository {
    override suspend fun getBookmarks(): Resource<List<BookmarkedRecipient>> {
        val bookmarks = bookmarkDao.getAll()
        val domainBookmarks = bookmarks.map {
            BookmarkedRecipient(it.slackID, it.title, it.type)
        }
        return Resource.success(domainBookmarks)
    }

    override suspend fun saveBookmark(recipient: BookmarkedRecipient): Resource<Unit> {
        val dataBookmark = Bookmark(recipient.id, recipient.name, recipient.recipientType)
        bookmarkDao.insert(dataBookmark)
        return Resource.success()
    }

    override suspend fun removeBookmark(recipient: BookmarkedRecipient): Resource<Unit> {
        val dataBookmark = Bookmark(recipient.id, recipient.name, recipient.recipientType)
        bookmarkDao.delete(dataBookmark)
        return Resource.success()
    }

    override suspend fun bookmarkExists(recipient: Recipient): Resource<Boolean> {
        val exists = bookmarkDao.bookmarkExists(recipient.id) == 1
        return Resource.success(exists)
    }

}