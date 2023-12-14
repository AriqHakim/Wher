package id.emergence.wher.screen.friends.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import coil.ImageLoader
import id.emergence.wher.R
import id.emergence.wher.databinding.FragmentFriendSearchBinding
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.ext.snackbar
import id.emergence.wher.utils.adapter.UserPagingAdapter
import id.emergence.wher.utils.viewbinding.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FriendSearchFragment : Fragment(R.layout.fragment_friend_search) {
    private val binding by viewBinding<FragmentFriendSearchBinding>()
    private val viewModel by viewModel<FriendSearchViewModel>()
    private val imageLoader by inject<ImageLoader>()

    private lateinit var userAdapter: UserPagingAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnBack.setOnClickListener { findNavController().popBackStack() }
            userAdapter =
                UserPagingAdapter(imageLoader) { user ->
                    navigateTo(
                        FriendSearchFragmentDirections.actionFriendSearchToDetail(user.id, null),
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
            viewModel.user.collectLatest(userAdapter::submitData)
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
