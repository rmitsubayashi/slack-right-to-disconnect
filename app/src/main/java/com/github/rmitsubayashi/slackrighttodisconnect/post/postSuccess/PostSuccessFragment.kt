package com.github.rmitsubayashi.slackrighttodisconnect.post.postSuccess

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.rmitsubayashi.presentation.post.PostSuccessContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.fragment__post_success.*
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
        return inflater.inflate(R.layout.fragment__post_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postSuccessPresenter.checkBookmark(args.recipient, args.threadID)

        button__post_success__bookmark.setOnClickListener {
            postSuccessPresenter.bookmark(args.recipient)
        }
        button__post_success__go_back.setOnClickListener {
            navigateToSelectPostRecipientType()
        }
    }

    override fun enableBookmarkButton(enable: Boolean) {
        button__post_success__bookmark.isEnabled = enable
        button__post_success__bookmark.text = getString(if (enable) { R.string.button__post_success__bookmark } else { R.string.button__post_success__bookmarked })
    }

    override fun navigateToSelectPostRecipientType() {
        findNavController().navigate(R.id.action_postSuccessFragment_to_selectPostRecipientTypeFragment)
    }

    override fun showGeneralError() {
        context?.showToast(R.string.error__core__general)
    }


}