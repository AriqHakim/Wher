package id.emergence.wher.screen.auth.register

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import id.emergence.wher.R
import id.emergence.wher.R.color
import id.emergence.wher.databinding.FragmentRegisterBinding
import id.emergence.wher.domain.model.LoginData
import id.emergence.wher.domain.model.RegisterData
import id.emergence.wher.ext.hideKeyboard
import id.emergence.wher.ext.navigateTo
import id.emergence.wher.ext.snackbar
import id.emergence.wher.screen.auth.login.LoginFragmentDirections
import id.emergence.wher.screen.auth.login.LoginViewModel.LoginSuccess
import id.emergence.wher.screen.auth.register.RegisterViewModel.RegisterSuccess
import id.emergence.wher.utils.base.OneTimeEvent
import id.emergence.wher.utils.viewbinding.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val binding by viewBinding<FragmentRegisterBinding>()
    private val viewModel by viewModel<RegisterViewModel>()

    private var formData: RegisterData
        get() = viewModel.formData.value
        set(value) {
            viewModel.updateFormData(value)
        }

    override fun onStart() {
        super.onStart()
        viewModel.events.onEach { event ->
            when (event) {
                RegisterSuccess -> {
                    navigateTo(RegisterFragmentDirections.actionRegisterToLogin())
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

            edUsername.setText(formData.username)
            edEmail.setText(formData.email)
            edDisplayName.setText(formData.name)
            edPassword.setText(formData.password)
            edConfirmPassword.setText(formData.confirmPassword)
            edUsername.doOnTextChanged { text, _, _, _ ->
                formData = formData.copy(username = text.toString())
            }
            edEmail.doOnTextChanged { text, _, _, _ ->
                formData = formData.copy(email = text.toString())
            }
            edDisplayName.doOnTextChanged { text, _, _, _ ->
                formData = formData.copy(name = text.toString())
            }
            edPassword.doOnTextChanged { text, _, _, _ ->
                formData = formData.copy(password = text.toString())
            }
            edConfirmPassword.doOnTextChanged { text, _, _, _ ->
                formData = formData.copy(confirmPassword = text.toString())
            }

            btnRegister.setOnClickListener {
                hideKeyboard()
                viewModel.onRegister()
            }
            lblSignIn.setOnClickListener {
                navigateTo(RegisterFragmentDirections.actionRegisterToLogin())
            }
        }
    }

    private fun toggleLoading(flag: Boolean) {
        binding.loadingIndicator.isVisible = flag
    }
}
