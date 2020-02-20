package com.github.rmitsubayashi.slackrighttodisconnect.post.select.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.post.SelectUserContract
import com.github.rmitsubayashi.slackrighttodisconnect.R
import com.github.rmitsubayashi.slackrighttodisconnect.util.showToast
import kotlinx.android.synthetic.main.fragment_select_user.*
import kotlinx.android.synthetic.main.fragment_select_user.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SelectUserFragment : Fragment(), SelectUserContract.View {
    private val selectUserPresenter: SelectUserContract.Presenter by inject { parametersOf(this@SelectUserFragment) }
    //private val args
    private lateinit var listAdapter: SelectUserAdapter
    private lateinit var listLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listAdapter = SelectUserAdapter(
            object : SelectUserRowClickListener {
                override fun onUserClicked(user: Recipient, selected: Boolean) {
                    selectUserPresenter.toggleUser(user, selected)
                }
            }
        )
        listLayoutManager = LinearLayoutManager(context)
        view.select_user_list.apply {
            adapter = listAdapter
            layoutManager = listLayoutManager
        }
        selectUserPresenter.start()
        select_user_select_button.setOnClickListener {
            selectUserPresenter.selectUsers()
        }
    }

    override fun navigateToPost(recipient: Recipient) {
        val action =
            SelectUserFragmentDirections.actionSelectUserFragmentToPostFragment(recipient, null)
        findNavController().navigate(action)
    }

    override fun setSelectButtonEnabled(enabled: Boolean) {
        select_user_select_button.isEnabled = enabled
    }

    override fun setUsers(users: List<Recipient>) {
        listAdapter.setUsers(users)
    }

    override fun showGeneralError() {
        context?.showToast(R.string.general_error)
    }

    override fun showNoNetwork() {
        context?.showToast(R.string.no_network_error)
    }

    override fun showTooManyUsers() {
        context?.showToast(R.string.slack_too_many_users)
    }

    override fun showTooManySelectedUsers() {
        context?.showToast(R.string.slack_too_many_selected_users)
    }


}