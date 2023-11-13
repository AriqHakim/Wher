package id.emergence.wher.screen.auth.login

import android.graphics.Paint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.internal.ViewUtils.hideKeyboard
import id.emergence.wher.R
import id.emergence.wher.R.color
import id.emergence.wher.databinding.FragmentLoginBinding
import id.emergence.wher.domain.model.LoginData
import id.emergence.wher.ext.hideKeyboard
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.ext.snackbar
import id.emergence.wher.screen.auth.login.LoginViewModel.LoginSuccess
import id.emergence.wher.utils.base.OneTimeEvent
import id.emergence.wher.utils.viewbinding.viewBinding
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by viewBinding<FragmentLoginBinding>()
    private val viewModel by viewModel<LoginViewModel>()

    private var formData: LoginData
        get() = viewModel.formData.value
        set(value) {
            viewModel.updateFormData(value)
        }

    override fun onStart() {
        super.onStart()
        viewModel.events.onEach { event ->
            when (event) {
                LoginSuccess -> {
                    navigateTo(LoginFragmentDirections.actionLoginToHome())
                }
                OneTimeEvent.Loading -> {
                    toggleLoading(true)
                }
                is OneTimeEvent.Error -> {
                    toggleLoading(false)
                    snackbar("Error : ${event.throwable?.message}")
                }
            }
        }
    }

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
            val firstTimeSpannable = SpannableString("First Time Using Wher?")
            firstTimeSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.brand_green2)),
                21,
                22,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            firstTimeSpannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.brand_blue)),
                17,
                21,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            lblFirstTime.text = firstTimeSpannable
            lblSignUp.paintFlags = lblSignUp.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            edUsername.setText(formData.username)
            edPassword.setText(formData.password)
            edUsername.doOnTextChanged { text, _, _, _ ->
                formData = formData.copy(username = text.toString())
            }
            edPassword.doOnTextChanged { text, _, _, _ ->
                formData = formData.copy(password = text.toString())
            }

            btnLogin.setOnClickListener {
                hideKeyboard()
                viewModel.onLogin()
            }

            lblSignUp.setOnClickListener {
                navigateTo(LoginFragmentDirections.actionLoginToRegister())
            }
        }
    }

    private fun toggleLoading(flag: Boolean) {
        binding.loadingIndicator.isVisible = flag
    }
}
