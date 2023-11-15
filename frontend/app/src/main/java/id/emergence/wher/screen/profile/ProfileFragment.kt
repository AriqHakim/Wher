package id.emergence.wher.screen.profile

import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import id.emergence.wher.R
import id.emergence.wher.databinding.FragmentProfileBinding
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.ext.snackbar
import id.emergence.wher.screen.auth.login.LoginFragmentDirections
import id.emergence.wher.screen.profile.ProfileViewModel.LogoutSuccess
import id.emergence.wher.utils.base.OneTimeEvent
import id.emergence.wher.utils.viewbinding.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding<FragmentProfileBinding>()
    private val viewModel by viewModel<ProfileViewModel>()

    override fun onStart() {
        super.onStart()
        viewModel.events.onEach { event ->
            when (event) {
                LogoutSuccess -> {
                    navigateTo(ProfileFragmentDirections.actionProfileToSplash())
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

            btnLogout.setOnClickListener {
                viewModel.onLogout()
            }
        }
    }

    private fun toggleLoading(flag: Boolean) {
        binding.loadingIndicator.isVisible = flag
    }
}
