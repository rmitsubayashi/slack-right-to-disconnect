package com.github.rmitsubayashi.slackrighttodisconnect.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.post.SelectPostRecipientContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import kotlinx.android.synthetic.main.fragment_select_post_recipient.view.*
import kotlinx.android.synthetic.main.fragment_select_post_recipient_type.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SelectPostRecipientFragment : Fragment(), SelectPostRecipientContract.View {
    private val selectPostRecipientPresenter: SelectPostRecipientContract.Presenter by inject{ parametersOf(this@SelectPostRecipientFragment) }
    private val args: SelectPostRecipientFragmentArgs by navArgs()
    private lateinit var listAdapter: PostRecipientsAdapter
    private lateinit var listLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_post_recipient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listAdapter = PostRecipientsAdapter(
            object: PostRecipientRowClickListener {
                override fun onItemClicked(recipient: Recipient) {
                    selectPostRecipientPresenter.selectPostRecipient(recipient)
                }
            }
        )
        listLayoutManager = LinearLayoutManager(context)
        view.select_post_recipient_list.apply {
            adapter = listAdapter
            layoutManager = listLayoutManager
        }
        selectPostRecipientPresenter.loadPostRecipients(args.PostRecipientType)

    }

    override fun navigateToPost(recipient: Recipient) {
        val action =
            SelectPostRecipientFragmentDirections.actionSelectPostRecipientFragmentToPostFragment(
                RecipientID = recipient.id,
                RecipientName = recipient.name
            )
        findNavController().navigate(action)
    }

    override fun setPostRecipients(recipients: List<Recipient>) {
        listAdapter.setRecipients(recipients)
    }
}