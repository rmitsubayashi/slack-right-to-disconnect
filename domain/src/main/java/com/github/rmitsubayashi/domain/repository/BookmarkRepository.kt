package com.github.rmitsubayashi.domain.repository

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.Recipient

interface BookmarkRepository {
    suspend fun getBookmarks(): Resource<List<Recipient>>
    suspend fun saveBookmark(recipient: Recipient): Resource<Unit>
    suspend fun removeBookmark(recipient: Recipient): Resource<Unit>
    suspend fun bookmarkExists(recipient: Recipient): Resource<Boolean>
}