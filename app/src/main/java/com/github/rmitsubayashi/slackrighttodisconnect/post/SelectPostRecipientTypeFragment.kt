package com.github.rmitsubayashi.slackrighttodisconnect.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.rmitsubayashi.domain.model.BookmarkedRecipient
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.post.SelectPostRecipientTypeContract
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.fragment_select_post_recipient_type.*
import kotlinx.android.synthetic.main.fragment_select_post_recipient_type.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SelectPostRecipientTypeFragment : Fragment(), SelectPostRecipientTypeContract.View {
    private val selectPostRecipientTypePresenter: SelectPostRecipientTypeContract.Presenter by inject{ parametersOf(this@SelectPostRecipientTypeFragment) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_post_recipient_type, container, false)
            .apply {
                select_post_recipient_type_channel_button.setOnClickListener {
                    selectPostRecipientTypePresenter.selectPostRecipientType(
                        RecipientType.CHANNEL
                    )
                }
                select_post_recipient_type_user_button.setOnClickListener {
                    selectPostRecipientTypePresenter.selectPostRecipientType(
                        RecipientType.USER
                    )
                }
                select_post_recipient_type_thread_button.setOnClickListener {
                    selectPostRecipientTypePresenter.selectPostRecipientType(
                        RecipientType.THREAD
                    )
                }
            }
    }

    override fun onStart() {
        selectPostRecipientTypePresenter.start()
        super.onStart()
    }

    override fun navigateToSelectPostRecipient(type: RecipientType) {
        val action =
            SelectPostRecipientTypeFragmentDirections.actionSelectPostRecipientTypeFragmentToSelectPostRecipientFragment(
                type
            )
        findNavController().navigate(action)
    }

    override fun navigateToPost(recipient: BookmarkedRecipient) {
        val action =
            SelectPostRecipientTypeFragmentDirections.actionSelectPostRecipientTypeFragmentToPostFragment(recipient.id, recipient.name, null, recipient.recipientType)
        findNavController().navigate(action)
    }

    override fun setBookmarks(bookmarks: List<BookmarkedRecipient>) {
        if (bookmarks.isNotEmpty()) {
            select_post_recipient_type_bookmark.text = bookmarks[0].name + bookmarks[0].recipientType.name
            select_post_recipient_type_bookmark.setOnClickListener { selectPostRecipientTypePresenter.selectBookmark(bookmarks[0]) }
        }
    }

    override fun showGeneralError() {
        context?.showToast(R.string.general_error)
    }

}