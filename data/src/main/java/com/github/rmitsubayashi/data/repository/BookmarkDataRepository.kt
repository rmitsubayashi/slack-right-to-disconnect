package com.github.rmitsubayashi.data.repository

import com.github.rmitsubayashi.data.local.sqlite.dao.BookmarkDao
import com.github.rmitsubayashi.data.local.sqlite.model.Bookmark
import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.repository.BookmarkRepository

class BookmarkDataRepository(private val bookmarkDao: BookmarkDao): BookmarkRepository {
    override suspend fun getBookmarks(): Resource<List<Recipient>> {
        val bookmarks = bookmarkDao.getAll()
        val domainBookmarks = bookmarks.map {
            Recipient(it.slackID, it.title, it.type)
        }
        return Resource.success(domainBookmarks)
    }

    override suspend fun saveBookmark(recipient: Recipient): Resource<Unit> {
        val dataBookmark =
            Bookmark(
                recipient.slackID,
                recipient.displayName,
                recipient.recipientType
            )
        bookmarkDao.insert(dataBookmark)
        return Resource.success()
    }

    override suspend fun removeBookmark(recipient: Recipient): Resource<Unit> {
        val dataBookmark =
            Bookmark(
                recipient.slackID,
                recipient.displayName,
                recipient.recipientType
            )
        bookmarkDao.delete(dataBookmark)
        return Resource.success()
    }

    override suspend fun bookmarkExists(recipient: Recipient): Resource<Boolean> {
        val exists = bookmarkDao.bookmarkExists(recipient.slackID) == 1
        return Resource.success(exists)
    }

}