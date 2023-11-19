package id.emergence.wher.screen.friends.detail

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import id.emergence.wher.R
import id.emergence.wher.databinding.FragmentFriendDetailBinding
import id.emergence.wher.domain.model.FriendRequestStatus
import id.emergence.wher.domain.model.FriendState
import id.emergence.wher.domain.model.User
import id.emergence.wher.ext.hashEmail
import id.emergence.wher.ext.snackbar
import id.emergence.wher.utils.base.OneTimeEvent
import id.emergence.wher.utils.viewbinding.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FriendDetailFragment : Fragment(R.layout.fragment_friend_detail) {
    private val binding by viewBinding<FragmentFriendDetailBinding>()
    private val viewModel by viewModel<FriendDetailViewModel>()
    private val imageLoader by inject<ImageLoader>()

    override fun onStart() {
        super.onStart()
        viewModel
            .events
            .onEach { event ->
                when (event) {
                    is OneTimeEvent.Alert -> {
                        toggleLoading(false)
                        snackbar(event.message.toString())
                        viewModel.fetchDetail()
                    }

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
            btnBack.setOnClickListener { findNavController().popBackStack() }
        }
        observeUser()
    }

    private fun observeUser() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.collect { it?.let(::loadUser) }
        }

    private fun loadUser(data: User) {
        toggleLoading(false)
        with(binding.layoutDetail) {
            tvUserId.text = "#${data.userId}"
            tvUsername.text = data.username
            tvDisplayName.text = data.name

            ivAvatar.apply {
                val imgData =
                    ImageRequest
                        .Builder(requireContext())
                        .data("https://gravatar.com/avatar/${hashEmail(data.email)}")
                        .target(this)
                        .allowHardware(true)
                        .transformations(
                            listOf(
                                CircleCropTransformation(),
                            ),
                        ).build()
                imageLoader.enqueue(imgData)
            }
            toggleCta(data.friendState)
            when (data.friendState) {
                FriendState.NOT_FRIEND -> handleNotFriendState(data.requestStatus == FriendRequestStatus.PENDING)
                FriendState.FRIEND -> handleFriendState()
                else -> handleIncomingState(data.username)
            }
        }
    }

    private fun toggleLoading(flag: Boolean) {
        binding.loadingIndicator.isVisible = flag
    }

    private fun toggleCta(state: FriendState) {
        with(binding.layoutDetail) {
            layoutCtaRequest.root.isVisible = state == FriendState.NOT_FRIEND
            layoutCtaFriend.root.isVisible = state == FriendState.FRIEND
            layoutCtaIncomingRequest.root.isVisible = state == FriendState.INCOMING
        }
    }

    // if not friend
    private fun handleNotFriendState(hasRequested: Boolean = false) {
        with(binding.layoutDetail.layoutCtaRequest) {
            btnRequestAction.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (hasRequested) R.color.brand_red else R.color.brand_green2,
                ),
            )
            btnRequestAction.text = if (hasRequested) "Cancel Request!" else "Add as Friend!"
            btnRequestAction.setOnClickListener {
                viewModel.onRequestFriend(hasRequested)
            }
        }
    }

    private fun handleFriendState() {
        with(binding.layoutDetail.layoutCtaFriend) {
            btnEndFriendship.setOnClickListener {
                viewModel.onRemoveFriend()
            }
        }
    }

    private fun handleIncomingState(username: String) {
        with(binding.layoutDetail.layoutCtaIncomingRequest) {
            lblSubtitle.text = "$username has sent you a friend request"
            btnAcceptRequest.setOnClickListener { viewModel.acceptFriendRequest() }
            btnRejectRequest.setOnClickListener { viewModel.rejectFriendRequest() }
        }
    }
}