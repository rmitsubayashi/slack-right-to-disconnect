package com.github.rmitsubayashi.slackrighttodisconnect.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.rmitsubayashi.presentation.post.SelectPostRecipientTypeContract
import com.github.rmitsubayashi.presentation.post.model.RecipientType
import com.github.rmitsubayashi.slackrighttodisconnect.R
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
            }
    }

    override fun navigateToSelectPostRecipient(type: RecipientType) {
        val action =
            SelectPostRecipientTypeFragmentDirections.actionSelectPostRecipientTypeFragmentToSelectPostRecipientFragment(
                type
            )
        findNavController().navigate(action)
    }

}