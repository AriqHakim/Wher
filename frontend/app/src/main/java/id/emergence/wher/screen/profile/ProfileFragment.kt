package id.emergence.wher.screen.profile

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import id.emergence.wher.R
import id.emergence.wher.databinding.FragmentProfileBinding
import id.emergence.wher.domain.model.User
import id.emergence.wher.ext.hashEmail
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.ext.snackbar
import id.emergence.wher.ext.toast
import id.emergence.wher.screen.profile.ProfileViewModel.AccountDeletionSuccess
import id.emergence.wher.screen.profile.ProfileViewModel.LogoutSuccess
import id.emergence.wher.utils.base.OneTimeEvent
import id.emergence.wher.utils.viewbinding.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding<FragmentProfileBinding>()
    private val viewModel by viewModel<ProfileViewModel>()
    private val imageLoader by inject<ImageLoader>()

    override fun onStart() {
        super.onStart()
        viewModel
            .events
            .onEach { event ->
                when (event) {
                    LogoutSuccess -> {
                        toast("Logged out!")
                        navigateTo(ProfileFragmentDirections.actionProfileToSplash())
                    }
                    AccountDeletionSuccess -> {
                        toast("Delete account success!")
                        navigateTo(ProfileFragmentDirections.actionProfileToSplash())
                    }
                    OneTimeEvent.Loading -> {
                        toggleLoading(true)
                    }
                    OneTimeEvent.NotLoading -> {
                        toggleLoading(false)
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

            tabLayout.addTab(
                tabLayout.newTab().setCustomView(tabProfile).also {
                    it.select()
                },
            )
            tabLayout.addTab(tabLayout.newTab().setCustomView(tabFriends))
            tabLayout.addOnTabSelectedListener(
                object : OnTabSelectedListener {
                    override fun onTabSelected(tab: Tab?) {
                        when (tab?.position) {
                            1 -> navigateTo(ProfileFragmentDirections.actionProfileToFriendList())
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

            btnEditProfile.setOnClickListener {
                navigateTo(ProfileFragmentDirections.actionProfileToEdit())
            }

            btnLogout.setOnClickListener {
                viewModel.onLogout()
            }
            btnDeleteAccount.setOnClickListener {
                showDeleteAccountDialog()
            }
        }
        profileObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchProfile()
    }

    private fun profileObserver() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.collectLatest { data ->
                data?.let { loadProfile(it) }
            }
        }

    private fun toggleLoading(flag: Boolean) {
        binding.loadingIndicator.isVisible = flag
    }

    private fun loadProfile(data: User) {
        with(binding) {
            tvUserId.text = "#${data.userId}"
            tvEmail.text = data.email
            tvUsername.text = data.username
            tvDisplayName.text = data.name
            val imgUrl =
                if (data.photoUrl.isNotEmpty()) {
                    data.photoUrl
                } else if (data.email.isNotEmpty()) {
                    "https://gravatar.com/avatar/${hashEmail(data.email)}"
                } else {
                    "https://placekitten.com/144/144"
                }

            ivAvatar.apply {
                val imgData =
                    ImageRequest
                        .Builder(requireContext())
                        .data(imgUrl)
                        .target(this)
                        .allowHardware(true)
                        .transformations(
                            listOf(
                                CircleCropTransformation(),
                            ),
                        ).build()
                imageLoader.enqueue(imgData)
            }
        }
    }

    private fun showDeleteAccountDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .apply {
                setTitle("Delete Account")
                setMessage("Account deletion is a one way ticket. Are you sure you want to delete this account?")
                setPositiveButton("Yes") { _, _ ->
                    viewModel.deleteAccount()
                }
                setNegativeButton("Cancel") { _, _ ->
                    // do nothing
                }
            }.show()
    }
}
