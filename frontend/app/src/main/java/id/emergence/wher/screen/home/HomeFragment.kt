package id.emergence.wher.screen.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import id.emergence.wher.R
import id.emergence.wher.databinding.FragmentHomeBinding
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.utils.viewbinding.viewBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding<FragmentHomeBinding>()

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
            tabLayout.addTab(tabLayout.newTab().setCustomView(tabFriends))
            tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: Tab?) {
                    when (tab?.position) {
                        0 -> navigateTo(HomeFragmentDirections.actionHomeToProfile())
                        else -> navigateTo(HomeFragmentDirections.actionHomeToFriendList())
                    }
                }

                override fun onTabUnselected(tab: Tab?) {
                }

                override fun onTabReselected(tab: Tab?) {
                    when (tab?.position) {
                        0 -> navigateTo(HomeFragmentDirections.actionHomeToProfile())
                        else -> navigateTo(HomeFragmentDirections.actionHomeToFriendList())
                    }
                }
            })
        }
    }
}
