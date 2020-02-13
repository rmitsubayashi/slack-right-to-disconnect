package com.github.rmitsubayashi.slackrighttodisconnect.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.rmitsubayashi.domain.model.BookmarkedRecipient
import com.github.rmitsubayashi.presentation.post.PostSuccessContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.fragment_post_success.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class PostSuccessFragment : Fragment(), PostSuccessContract.View {
    private val postSuccessPresenter: PostSuccessContract.Presenter by inject { parametersOf(this@PostSuccessFragment) }
    private val args: PostSuccessFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postSuccessPresenter.checkBookmark(
            BookmarkedRecipient(
                args.RecipientID,
                args.RecipientName
            )
        )

        post_success_bookmark.setOnClickListener {
            val recipient = BookmarkedRecipient(args.RecipientID, args.RecipientName)
            postSuccessPresenter.bookmark(recipient)
        }
        post_success_go_back.setOnClickListener {
            navigateToSelectPostRecipientType()
        }
    }

    override fun enableBookmarkButton(enable: Boolean) {
        post_success_bookmark.isEnabled = enable
    }

    override fun navigateToSelectPostRecipientType() {
        findNavController().navigate(R.id.action_postSuccessFragment_to_selectPostRecipientTypeFragment)
    }

    override fun showGeneralError() {
        context?.showToast(R.string.general_error)
    }


}