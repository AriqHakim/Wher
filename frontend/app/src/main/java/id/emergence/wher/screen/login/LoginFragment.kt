package id.emergence.wher.screen.login

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
import id.emergence.wher.databinding.FragmentLoginBinding
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.utils.viewbinding.viewBinding

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by viewBinding<FragmentLoginBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val logoSpannable = SpannableString("Wher?")
            logoSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), color.brand_green2)),
                4,
                5,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            lblLogo.text = logoSpannable
            val firstTimeSpannable = SpannableString("First Time Using Wher?")
            firstTimeSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.brand_green2)),
                21,
                22,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            firstTimeSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.brand_blue)),
                17,
                21,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            lblFirstTime.text = firstTimeSpannable
            lblSignUp.paintFlags = lblSignUp.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            btnLogin.setOnClickListener {
                navigateTo(LoginFragmentDirections.actionLoginToHome())
            }
            lblSignUp.setOnClickListener {
                navigateTo(LoginFragmentDirections.actionLoginToRegister())
            }
        }
    }
}