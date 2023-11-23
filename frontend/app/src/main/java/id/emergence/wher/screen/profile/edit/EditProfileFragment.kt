package id.emergence.wher.screen.profile.edit

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.github.dhaval2404.imagepicker.ImagePicker
import id.emergence.wher.R
import id.emergence.wher.databinding.FragmentEditProfileBinding
import id.emergence.wher.domain.model.ProfileData
import id.emergence.wher.domain.model.User
import id.emergence.wher.ext.hashEmail
import id.emergence.wher.ext.snackbar
import id.emergence.wher.ext.toast
import id.emergence.wher.screen.profile.edit.EditProfileViewModel.UpdateProfileSucceed
import id.emergence.wher.utils.base.OneTimeEvent
import id.emergence.wher.utils.viewbinding.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {
    private val binding by viewBinding<FragmentEditProfileBinding>()
    private val viewModel by viewModel<EditProfileViewModel>()
    private val imageLoader by inject<ImageLoader>()
    private var mCameraUri: Uri? = null

    private var formData: ProfileData
        get() = viewModel.formData.value
        set(value) {
            viewModel.updateFormData(value)
        }

    override fun onStart() {
        super.onStart()
        viewModel
            .events
            .onEach { event ->
                when (event) {
                    UpdateProfileSucceed -> {
                        toggleLoading(false)
                        findNavController().popBackStack()
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
            edDisplayName.setText(formData.name)
            edPassword.setText(formData.password)
            edConfirmPassword.setText(formData.confirmPassword)
            edDisplayName.doOnTextChanged { text, _, _, _ ->
                formData = formData.copy(name = text.toString())
            }
            edPassword.doOnTextChanged { text, _, _, _ ->
                formData = formData.copy(password = text.toString())
            }
            edConfirmPassword.doOnTextChanged { text, _, _, _ ->
                formData = formData.copy(confirmPassword = text.toString())
            }
            btnBack.setOnClickListener { findNavController().popBackStack() }
            btnEditAvatar.setOnClickListener {
                ImagePicker
                    .with(this@EditProfileFragment)
                    .compress(1024)
                    .cropSquare()
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }
            btnSave.setOnClickListener {
                viewModel.updateProfile()
            }
        }
        profileObserver()
    }

    private fun profileObserver() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.collectLatest { data ->
                data?.let { loadProfile(it) }
            }
        }

    private fun setAvatarFromUri(uri: Uri) {
        binding.ivAvatar.apply {
            val imgData =
                ImageRequest
                    .Builder(requireContext())
                    .data(uri)
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

    private fun toggleLoading(flag: Boolean) {
        binding.loadingIndicator.isVisible = flag
    }

    private fun loadProfile(data: User) {
        with(binding) {
            edDisplayName.setText(data.name)
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

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                // Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                mCameraUri = fileUri
                formData = formData.copy(file = File(fileUri.path))
                setAvatarFromUri(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                toast(ImagePicker.getError(data))
            } else {
                toast("Task Cancelled")
            }
        }
}
