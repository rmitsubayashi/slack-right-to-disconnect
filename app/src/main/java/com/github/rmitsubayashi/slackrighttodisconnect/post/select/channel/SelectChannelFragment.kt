package com.github.rmitsubayashi.slackrighttodisconnect.post.select.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.post.SelectChannelContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.fragment__select_channel.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SelectChannelFragment : Fragment(), SelectChannelContract.View {
    private val selectChannelPresenter: SelectChannelContract.Presenter by inject {
        parametersOf(
            this@SelectChannelFragment
        )
    }
    //private val args
    private lateinit var listAdapter: SelectChannelAdapter
    private lateinit var listLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment__select_channel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listAdapter = SelectChannelAdapter(
            object : SelectChannelRowClickListener {
                override fun onChannelClicked(channel: Recipient) {
                    selectChannelPresenter.selectChannel(channel)
                }
            }
        )
        listLayoutManager = LinearLayoutManager(context)
        view.list__select_channel.apply {
            adapter = listAdapter
            layoutManager = listLayoutManager
        }
        selectChannelPresenter.start()
    }

    override fun navigateToPost(recipient: Recipient) {
        val action = SelectChannelFragmentDirections.actionSelectChannelFragmentToPostFragment(
            recipient, null
        )
        findNavController().navigate(action)
    }

    override fun setChannels(channels: List<Recipient>) {
        listAdapter.setChannels(channels)
    }

    override fun showGeneralError() {
        context?.showToast(R.string.error__core__general)
    }

    override fun showNoNetwork() {
        context?.showToast(R.string.error__core__no_network)
    }

    override fun showTooManyChannels() {
        context?.showToast(R.string.error__select_channel__slack_too_many_channels)
    }
}