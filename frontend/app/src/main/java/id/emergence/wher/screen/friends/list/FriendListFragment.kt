package id.emergence.wher.screen.friends.list

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import id.emergence.wher.R
import id.emergence.wher.databinding.FragmentFriendListBinding
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.ext.snackbar
import id.emergence.wher.utils.adapter.UserPagingAdapter
import id.emergence.wher.utils.base.OneTimeEvent
import id.emergence.wher.utils.viewbinding.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FriendListFragment : Fragment(R.layout.fragment_friend_list) {
    private val binding by viewBinding<FragmentFriendListBinding>()
    private val viewModel by viewModel<FriendListViewModel>()
    private val imageLoader by inject<ImageLoader>()

    private lateinit var userAdapter: UserPagingAdapter

    override fun onStart() {
        super.onStart()
        viewModel
            .events
            .onEach { event ->
                when (event) {
                    OneTimeEvent.Loading -> {
                        toggleLoading(true)
                    }

                    is OneTimeEvent.Error -> {
                        toggleLoading(false)
                        snackbar("Error : ${event.throwable?.message}")
                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val tabProfile = layoutInflater.inflate(R.layout.view_custom_tab, null)
            tabProfile.findViewById<ImageView>(R.id.icon).setBackgroundResource(R.drawable.ic_profile)
            val tabFriends = layoutInflater.inflate(R.layout.view_custom_tab, null)
            tabFriends.findViewById<ImageView>(R.id.icon).setBackgroundResource(R.drawable.ic_friends)

            tabLayout.addTab(tabLayout.newTab().setCustomView(tabProfile))
            tabLayout.addTab(
                tabLayout.newTab().setCustomView(tabFriends).also {
                    it.select()
                },
            )
            tabLayout.addOnTabSelectedListener(
                object : OnTabSelectedListener {
                    override fun onTabSelected(tab: Tab?) {
                        when (tab?.position) {
                            0 -> navigateTo(FriendListFragmentDirections.actionFriendListToProfile())
                            else -> {
                                // do nothing
                            }
                        }
                    }

                    override fun onTabUnselected(tab: Tab?) {
                    }

                    override fun onTabReselected(tab: Tab?) {
                    }
                },
            )

            edAddFriend.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    navigateTo(FriendListFragmentDirections.actionFriendListToSearch(edAddFriend.text.toString()))
                }
                true
            }
            btnFriendRequest.setOnClickListener {
                navigateTo(FriendListFragmentDirections.actionFriendListToRequest())
            }

            userAdapter =
                UserPagingAdapter(imageLoader) { user ->
                    navigateTo(
                        FriendListFragmentDirections.actionFriendListToDetail(user.id),
                    )
                }
            recyclerView.apply {
                adapter = userAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        observeResult()
        observeAdapter()
    }

    private fun observeResult() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.friends.collectLatest(userAdapter::submitData)
        }

    private fun observeAdapter() =
        viewLifecycleOwner.lifecycleScope.launch {
            userAdapter
                .loadStateFlow
                .map { it.refresh }
                .distinctUntilChanged()
                .collect {
                    if (it is LoadState.NotLoading) {
                        toggleLoading(false)
                        if (userAdapter.itemCount < 1) {
                            toggleEmptyLayout(true)
                        } else {
                            toggleEmptyLayout(false)
                        }
                    } else if (it is LoadState.Loading) {
                        toggleLoading(true)
                    } else if (it is LoadState.Error) {
                        toggleLoading(false)
                        toggleEmptyLayout(true)
                        snackbar("Error : ${it.error.message}")
                    }
                }
        }

    private fun toggleLoading(flag: Boolean) {
        binding.loadingIndicator.isVisible = flag
    }

    private fun toggleEmptyLayout(isEmpty: Boolean) {
        with(binding) {
            recyclerView.isVisible = !isEmpty
            lblEmpty.isVisible = isEmpty
        }
    }
}
