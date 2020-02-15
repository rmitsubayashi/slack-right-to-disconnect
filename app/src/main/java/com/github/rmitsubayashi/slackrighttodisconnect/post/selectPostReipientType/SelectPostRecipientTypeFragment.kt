package com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostReipientType

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.BookmarkedRecipient
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
    private lateinit var listAdapter: PostRecipientTypeAdapter
    private lateinit var listLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_post_recipient_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listAdapter = PostRecipientTypeAdapter(
            object : HeaderListener {
                override fun onRecipientTypeClicked(type: RecipientType) {
                    selectPostRecipientTypePresenter.selectPostRecipientType(
                        type
                    )
                }
            },
            object : BookmarkListener {
                override fun onBookmarkClicked(bookmarkedRecipient: BookmarkedRecipient) {
                    selectPostRecipientTypePresenter.selectBookmark(bookmarkedRecipient)
                }
            }
        )
        listLayoutManager = LinearLayoutManager(context)
        select_post_recipient_type_layout.apply {
            adapter = listAdapter
            layoutManager = listLayoutManager
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
            SelectPostRecipientTypeFragmentDirections.actionSelectPostRecipientTypeFragmentToPostFragment(
                recipient.id,
                recipient.name,
                null,
                recipient.recipientType
            )
        findNavController().navigate(action)
    }

    override fun setBookmarks(bookmarks: List<BookmarkedRecipient>) {
        listAdapter.setBookmarks(bookmarks)
    }

    override fun showGeneralError() {
        context?.showToast(R.string.general_error)
    }

}