package id.emergence.wher.screen.auth.register

import android.graphics.Paint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import id.emergence.wher.R
import id.emergence.wher.R.color
import id.emergence.wher.databinding.FragmentRegisterBinding
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.utils.viewbinding.viewBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val binding by viewBinding<FragmentRegisterBinding>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val logoSpannable = SpannableString("Wher?")
            logoSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), color.brand_green2)),
                4,
                5,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            lblLogo.text = logoSpannable
            val firstTimeSpannable = SpannableString("Already Have Wher? Account?")
            firstTimeSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.brand_green2)),
                17,
                18,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            firstTimeSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.brand_blue)),
                13,
                17,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            lblFirstTime.text = firstTimeSpannable
            lblSignIn.paintFlags = lblSignIn.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            btnRegister.setOnClickListener {
                navigateTo(RegisterFragmentDirections.actionRegisterToLogin())
            }
            lblSignIn.setOnClickListener {
                navigateTo(RegisterFragmentDirections.actionRegisterToLogin())
            }
        }
    }
}
