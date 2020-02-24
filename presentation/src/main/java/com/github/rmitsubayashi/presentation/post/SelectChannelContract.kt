package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface SelectChannelContract {
    interface View: BaseView {
        fun navigateToPost(recipient: Recipient)
        fun setChannels(channels: List<Recipient>)
        fun showNoNetwork()
        fun showTooManyChannels()
        fun showLoading(loading: Boolean)
    }

    interface Presenter: BasePresenter {
        fun selectChannel(channel: Recipient)
    }
}