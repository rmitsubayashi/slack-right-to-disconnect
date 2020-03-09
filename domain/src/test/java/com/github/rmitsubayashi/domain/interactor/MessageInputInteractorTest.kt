package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.domain.repository.SlackTeamRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class MessageInputInteractorTest {
    private lateinit var messageInputInteractor: MessageInputInteractor
    @Mock
    lateinit var slackTeamRepository: SlackTeamRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        messageInputInteractor = MessageInputInteractor(slackTeamRepository)
    }

    @Test
    fun correctFormattedMessage() {
        val slackID = "testSlackID"
        val slackName = "testName"
        mentionUser(slackID, slackName)
        runBlocking {
            assertThat(messageInputInteractor.formatMessageForSlack()).isEqualTo("<@$slackID> is here")
        }
    }

    @Test
    fun formattingMessageDoesNotAlterRawMessage() {
        val slackID = "testSlackID"
        val slackName = "testName"
        mentionUser(slackID, slackName)
        runBlocking {
            messageInputInteractor.formatMessageForSlack()
        }
        assertThat(messageInputInteractor.getRawInput()).isEqualTo("$slackName is here")
    }

    @Test
    fun correctRawMessageAfterMention() {
        val slackID = "slackID"
        val slackName = "name"
        mentionUser(slackID, slackName)
        assertThat(messageInputInteractor.getRawInput()).isEqualTo("$slackName is here")
    }

    private fun mentionUser(slackID: String, slackName: String) {
        runBlocking {
            `when`(slackTeamRepository.getUsers()).thenReturn(
                Resource.success(
                    listOf(Recipient(slackID, slackName, slackName, RecipientType.USER))
                )
            )
        }
        //the state of the input right before adding a mention (typing an '@' mark)
        messageInputInteractor.updateInput("@ is here")
        messageInputInteractor.addMention(slackName, 0)
    }

    @Test
    fun search() {
        val slackID = "slackID"
        val slackName = "name"
        val slackName2 = "nname"
        runBlocking {
            `when`(slackTeamRepository.getUsers()).thenReturn(
                Resource.success(
                    listOf(
                        Recipient(slackID, slackName, slackName, RecipientType.USER),
                        Recipient(slackID, slackName2, slackName2, RecipientType.USER)
                    )
                )
            )
        }
        runBlocking {
            val allMatches = messageInputInteractor.searchMention("n")
            assertThat(allMatches.size).isEqualTo(2)
            val oneMatch = messageInputInteractor.searchMention("na")
            assertThat(oneMatch.size).isEqualTo(1)
            val noMatches = messageInputInteractor.searchMention("nan")
            assertThat(noMatches.size).isEqualTo(0)
        }



    }
}