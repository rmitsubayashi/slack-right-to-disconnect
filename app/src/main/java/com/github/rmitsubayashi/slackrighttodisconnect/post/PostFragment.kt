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
import kotlinx.android.synthetic.main.fragment_post.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class PostFragment: Fragment(), PostContract.View {
    private val postPresenter: PostContract.Presenter by inject { parametersOf(this@PostFragment) }
    private val args: PostFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)
        view.post_send_button.setOnClickListener { postPresenter.postToSlack() }
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
        postPresenter.setRecipient(args.RecipientID, args.ThreadID)
    }

    override fun navigateToPostSuccess() {
        findNavController().navigate(R.id.action_postFragment_to_postSuccessFragment)
    }

    override fun showPostSending() {

    }

    override fun showPostError(errorMessage: String) {
        context?.showToast(errorMessage)
    }
}