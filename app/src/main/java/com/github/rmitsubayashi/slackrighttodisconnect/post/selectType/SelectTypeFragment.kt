package com.github.rmitsubayashi.slackrighttodisconnect.post.selectType

import android.content.DialogInterface
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.post.SelectTypeContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.fragment__select_type.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SelectTypeFragment : Fragment(), SelectTypeContract.View {
    private val selectTypePresenter: SelectTypeContract.Presenter by inject{ parametersOf(this@SelectTypeFragment) }
    private lateinit var listAdapter: SelectTypeAdapter
    private lateinit var listLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment__select_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listAdapter = SelectTypeAdapter(
            object : SelectTypeHeaderListener {
                override fun onSelectChannelClicked() {
                    selectTypePresenter.selectSelectChannel()
                }

                override fun onSelectRecentThreadClicked() {
                    selectTypePresenter.selectSelectRecentThread()
                }

                override fun onSelectUserClicked() {
                    selectTypePresenter.selectSelectUser()
                }
            },
            object : BookmarkListener {
                override fun onBookmarkClicked(bookmarkedRecipient: Recipient) {
                    selectTypePresenter.selectBookmark(bookmarkedRecipient)
                }

                override fun onBookmarkLongClicked(bookmarkedRecipient: Recipient) {
                    context?.let {
                        val removeString = getString(R.string.label__select_type__remove_bookmark)
                        AlertDialog.Builder(it)
                            .setItems(arrayOf(removeString)) {
                                _,_ ->
                                selectTypePresenter.removeBookmark(bookmarkedRecipient)
                            }
                            .show()
                    }
                }
            }
        )
        listLayoutManager = LinearLayoutManager(context)
        list__select_type.apply {
            adapter = listAdapter
            layoutManager = listLayoutManager
        }
    }

    override fun onStart() {
        selectTypePresenter.start()
        super.onStart()
    }

    override fun navigateToPost(recipient: Recipient) {
        val action =
            SelectTypeFragmentDirections.actionSelectPostRecipientTypeFragmentToPostFragment(recipient, null)
        findNavController().navigate(action)
    }

    override fun navigateToSelectChannel() {
        val action = SelectTypeFragmentDirections.actionSelectPostRecipientTypeFragmentToSelectChannelFragment()
        findNavController().navigate(action)
    }

    override fun navigateToSelectUser() {
        val action = SelectTypeFragmentDirections.actionSelectPostRecipientTypeFragmentToSelectUserFragment()
        findNavController().navigate(action)
    }

    override fun navigateToSelectRecentThread() {
        val action = SelectTypeFragmentDirections.actionSelectPostRecipientTypeFragmentToSelectRecentThreadFragment()
        findNavController().navigate(action)
    }

    override fun setBookmarks(bookmarks: List<Recipient>) {
        listAdapter.setBookmarks(bookmarks)
    }

    override fun removeBookmark(bookmark: Recipient) {
        listAdapter.removeBookmark(bookmark)
    }

    override fun showGeneralError() {
        context?.showToast(R.string.error__core__general)
    }

}