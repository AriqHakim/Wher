package id.emergence.wher.screen.friends.list

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import id.emergence.wher.R
import id.emergence.wher.databinding.FragmentFriendListBinding
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.utils.viewbinding.viewBinding

class FriendListFragment : Fragment(R.layout.fragment_friend_list) {
    private val binding by viewBinding<FragmentFriendListBinding>()

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
        }
    }
}
