package com.github.rmitsubayashi.slackrighttodisconnect.post

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.post.PostContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import com.linkedin.android.spyglass.mentions.Mentionable
import com.linkedin.android.spyglass.suggestions.SuggestionsResult
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver
import com.linkedin.android.spyglass.ui.MentionsEditText
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class PostFragment: Fragment(), PostContract.View, QueryTokenReceiver {
    private val postPresenter: PostContract.Presenter by inject { parametersOf(this@PostFragment) }
    private val args: PostFragmentArgs by navArgs()
    private val bucket = "slack-people"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)
        view.post_send_button.setOnClickListener { postPresenter.postToSlack() }
        view.post_message_edittext.addMentionWatcher(
            object: MentionsEditText.MentionWatcher {
                override fun onMentionAdded(
                    mention: Mentionable,
                    text: String,
                    start: Int,
                    end: Int
                ) {
                    postPresenter.addMention(mention.getTextForDisplayMode(Mentionable.MentionDisplayMode.FULL), start)
                }

                override fun onMentionDeleted(
                    mention: Mentionable,
                    text: String,
                    start: Int,
                    end: Int
                ) {
                    postPresenter.removeMention(mention.getTextForDisplayMode(Mentionable.MentionDisplayMode.FULL), start)
                }

                override fun onMentionPartiallyDeleted(
                    mention: Mentionable,
                    text: String,
                    start: Int,
                    end: Int
                ) {
                    postPresenter.removeMention(mention.getTextForDisplayMode(Mentionable.MentionDisplayMode.FULL), start)
                }
            }
        )
        view.post_message_edittext.addTextChangedListener(
            object: TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    postPresenter.updateMessage(s.toString())
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }
            }
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postPresenter.setRecipient(args.RecipientID, args.RecipientType, args.ThreadID)
        post_message_edittext.setQueryTokenReceiver(this)
        post_message_edittext.displayTextCounter(false)
        post_message_edittext.setEditTextShouldWrapContent(true)
    }

    override fun navigateToPostSuccess() {
        val action = PostFragmentDirections.actionPostFragmentToPostSuccessFragment(args.RecipientID, args.RecipientName, args.RecipientType)
        findNavController().navigate(action)
    }

    override fun showPostSending() {

    }

    override fun onQueryReceived(queryToken: QueryToken): MutableList<String> {
        val mutableList = mutableListOf(bucket)
        postPresenter.searchMentions(queryToken.tokenString, queryToken.keywords)
        return mutableList
    }

    override fun showMentionSuggestions(token: String, suggestions: List<String>) {
        val suggestibles = suggestions.map { MentionSuggestion(it) }
        val suggestionResult = SuggestionsResult(QueryToken(token), suggestibles)
        post_message_edittext.onReceiveSuggestionsResult(suggestionResult, bucket)
    }

    override fun showNoNetwork() {
        context?.showToast(R.string.no_network_error)
    }

    override fun showGeneralError() {
        context?.showToast(R.string.general_error)
    }

    override fun showRestrictedChannel() {
        context?.showToast(R.string.slack_restricted_channel)
    }

    override fun showInvalidContent() {
        context?.showToast(R.string.slack_invalid_content)
    }
}