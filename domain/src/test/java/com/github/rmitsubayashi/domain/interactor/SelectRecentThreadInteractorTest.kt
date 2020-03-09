package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.domain.repository.SlackMessageRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*

class SelectRecentThreadInteractorTest {
    private lateinit var selectRecentThreadInteractor: SelectRecentThreadInteractor
    @Mock
    private lateinit var slackMessageRepository: SlackMessageRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        selectRecentThreadInteractor = SelectRecentThreadInteractor(slackMessageRepository)
    }

    @Test
    fun removesOldThreads() {
        val today = Date()
        val calendar = Calendar.getInstance()
        calendar.time = today
        calendar.add(Calendar.DATE, -6)
        val oneWeekOld = calendar.time
        calendar.add(Calendar.DATE, -2)
        val overOneWeekOld = calendar.time

        val threads = listOf(
            Message("test", Recipient("slackID", "name", "name", RecipientType.USER), "threadID", today),
            Message("test", Recipient("slackID", "name", "name", RecipientType.USER), "threadID", oneWeekOld),
            Message("test", Recipient("slackID", "name", "name", RecipientType.USER), "threadID", overOneWeekOld)
        )

        runBlocking {
            `when`(slackMessageRepository.getRecentThreads()).thenReturn(Resource.success(threads))
            val resource = selectRecentThreadInteractor.getRecentThreads()
            assertThat(resource.data?.size).isEqualTo(2)
        }
    }

}