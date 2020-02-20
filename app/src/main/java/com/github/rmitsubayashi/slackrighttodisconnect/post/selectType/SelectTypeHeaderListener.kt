package com.github.rmitsubayashi.slackrighttodisconnect.post.selectType

interface SelectTypeHeaderListener {
    fun onSelectUserClicked()
    fun onSelectChannelClicked()
    fun onSelectRecentThreadClicked()
}