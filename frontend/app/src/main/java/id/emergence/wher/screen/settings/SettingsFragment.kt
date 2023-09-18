package id.emergence.wher.screen.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.utils.viewbinding.viewBinding
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val binding by viewBinding<FragmentSettingsBinding>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            childFragmentManager.commit {
                replace(
                    settingsContainer.id,
                    SettingsContainer {
                        navigateTo(SettingsFragmentDirections.actionSettingsToLogin())
                    },
                )
            }
        }
    }

    class SettingsContainer(
        private val logoutCallback: () -> Unit,
    ) : PreferenceFragmentCompat() {
        override fun onCreatePreferences(
            savedInstanceState: Bundle?,
            rootKey: String?,
        ) {
            val screen = preferenceManager.createPreferenceScreen(requireContext())
            preferenceScreen = screen
            setupPreferenceScreen(screen)
        }

        private fun setupPreferenceScreen(screen: PreferenceScreen) {
        }
    }
}
