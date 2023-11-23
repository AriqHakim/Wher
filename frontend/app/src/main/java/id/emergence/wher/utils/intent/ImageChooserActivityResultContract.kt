package id.emergence.wher.utils.intent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.isPhotoPickerAvailable

class ImageChooserActivityResultContract : ActivityResultContract<Triple<Uri?, PickVisualMediaRequest, String>, Pair<Boolean, Uri?>>() {
    override fun createIntent(
        context: Context,
        input: Triple<Uri?, PickVisualMediaRequest, String>,
    ): Intent {
        if (input.first == null) return getImageSelectIntent(context, input.second)
        val cameraIntent =
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, input.first)
        val imageSelectIntent = getImageSelectIntent(context, input.second)
        val chooserIntent = Intent.createChooser(cameraIntent, input.third)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(imageSelectIntent))
        return chooserIntent
    }

    private fun getImageSelectIntent(
        context: Context,
        pickVisualMediaRequest: PickVisualMediaRequest,
    ): Intent {
        return if (isPhotoPickerAvailable(context)) {
            Intent(MediaStore.ACTION_PICK_IMAGES).apply {
                type = getVisualMimeType(pickVisualMediaRequest.mediaType)
            }
        } else {
            // For older devices running KitKat and higher and devices running Android 12
            // and 13 without the SDK extension that includes the Photo Picker, rely on the
            // ACTION_OPEN_DOCUMENT intent
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = getVisualMimeType(pickVisualMediaRequest.mediaType)

                if (type == null) {
                    // ACTION_OPEN_DOCUMENT requires to set this parameter when launching the
                    // intent with multiple mime types
                    type = "*/*"
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
                }
            }
        }
    }

    private fun getVisualMimeType(input: ActivityResultContracts.PickVisualMedia.VisualMediaType): String? {
        return when (input) {
            is ActivityResultContracts.PickVisualMedia.ImageOnly -> "image/*"
            is ActivityResultContracts.PickVisualMedia.VideoOnly -> "video/*"
            is ActivityResultContracts.PickVisualMedia.SingleMimeType -> input.mimeType
            is ActivityResultContracts.PickVisualMedia.ImageAndVideo -> null
        }
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?,
    ): Pair<Boolean, Uri?> {
        val isSuccessful = resultCode == Activity.RESULT_OK
        val uri = intent.takeIf { isSuccessful }?.data
        return Pair(isSuccessful, uri)
    }
}
