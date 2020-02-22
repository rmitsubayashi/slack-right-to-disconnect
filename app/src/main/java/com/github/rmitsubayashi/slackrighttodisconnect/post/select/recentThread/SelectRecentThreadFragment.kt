package com.github.rmitsubayashi.slackrighttodisconnect.post.select.recentThread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.post.SelectRecentThreadContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.fragment__select_recent_thread.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SelectRecentThreadFragment : Fragment(), SelectRecentThreadContract.View {
    private val selectRecentThreadPresenter: SelectRecentThreadContract.Presenter by inject {
        parametersOf(
            this@SelectRecentThreadFragment
        )
    }
    //private val args
    private lateinit var listAdapter: SelectRecentThreadAdapter
    private lateinit var listLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment__select_recent_thread, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listAdapter = SelectRecentThreadAdapter(
            object : SelectRecentThreadRowClickListener {
                override fun onRecentThreadClicked(message: Message) {
                    selectRecentThreadPresenter.selectRecentThread(message)
                }
            }
        )
        listLayoutManager = LinearLayoutManager(context)
        view.list__select_recent_thread.apply {
            adapter = listAdapter
            layoutManager = listLayoutManager
        }
        selectRecentThreadPresenter.start()
    }

    override fun navigateToPost(recipient: Recipient, message: Message) {
        val action =
            SelectRecentThreadFragmentDirections.actionSelectRecentThreadFragmentToPostFragment(
                recipient,
                message
            )
        findNavController().navigate(action)
    }

    override fun setRecentThreads(recentThreads: List<Message>) {
        listAdapter.setRecentThreads(recentThreads)
    }

    override fun showGeneralError() {
        context?.showToast(R.string.error__core__general)
    }

    override fun showNoRecentThreads() {
        throw NotImplementedError()
    }
}