package id.emergence.wher.screen.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import id.emergence.wher.utils.viewbinding.viewBinding
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding<FragmentHomeBinding>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }
}
