import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.mikedg.thepinballapp.data.remote.OpenAiService
import com.mikedg.thepinballapp.features.photoscore.PhotoScoreViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PhotoScore() {
    val viewModel = hiltViewModel<PhotoScoreViewModel>()
    val context = LocalContext.current
    var photoUri = remember { mutableStateOf<Uri?>(null) }

    val createImageUri = {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/The Pinball App")
                // This saves the full size image
            }
        }
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }
    val coroutineScope = rememberCoroutineScope()

    // Register for activity result
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri.value?.let { uri ->
                // Resize the image
                coroutineScope.launch {
                    val resizedBitmap = resizeImageFromUri(context, uri, 800, 600)

                    // Now you can pass the resized bitmap to your function
                    resizedBitmap?.let { bitmap ->
                        viewModel.showScore(null, bitmap)
                        // TODO: remove from here
                        val openAi = OpenAiService()

                        val scoreResult = openAi.getScore(resizedBitmap)
                        viewModel.showScore(scoreResult, resizedBitmap)
                    }
                }
            }
        } else {
            // Handle cancellation or failure
//            viewModel.onCameraCancelled()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PhotoScoreViewModel.UiEvent.TakePhoto -> {
                    createImageUri()?.let { uri ->
                        photoUri.value = uri
                        cameraLauncher.launch(uri)
                    }
                }
            }
        }
    }

    PhotoCaptureScreen(viewModel)
}

// Helper function to get file path from URI for older Android versions
private fun getRealPathFromURI(context: Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
            if (columnIndex >= 0) {
                return it.getString(columnIndex)
            }
        }
    }
    return uri.path
}

private fun getRotatedBitmap(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
    context.contentResolver.openInputStream(uri).use { inputStream ->
        val exifInterface = ExifInterface(inputStream!!)
        inputStream.close()

        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val matrix = android.graphics.Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.preScale(1f, -1f)
        }

        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
        )
    }
}

// Function to resize image from Uri
suspend fun resizeImageFromUri(
    context: Context,
    uri: Uri,
    targetWidth: Int,
    targetHeight: Int
): Bitmap? = withContext(Dispatchers.IO) {
    try {
        // Load the full-size bitmap
        val inputStream = context.contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
//        BitmapFactory.decodeStream(inputStream, null, options) // was this doing anything?
        val fullBitmap = BitmapFactory.decodeStream(inputStream, null, options) //
        fullBitmap?.let {
            val rotatedBitmap = getRotatedBitmap(context, uri, fullBitmap)
            return@withContext rotatedBitmap
        }
        inputStream?.close()

        // Calculate sample size
        options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)

        // Decode with sample size
        options.inJustDecodeBounds = false
        val secondInputStream = context.contentResolver.openInputStream(uri)
        val sampledBitmap = BitmapFactory.decodeStream(secondInputStream, null, options)
        secondInputStream?.close()

        sampledBitmap?.let {
            val rotatedBitmap = getRotatedBitmap(context, uri, sampledBitmap)

            return@withContext rotatedBitmap
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Calculate sample size for efficient loading
fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}

@OptIn(ExperimentalPermissionsApi::class) // For accompanist permission
@Composable
fun PhotoCaptureScreen(viewModel: PhotoScoreViewModel, modifier: Modifier = Modifier) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val score = viewModel.score.collectAsState()
    val title = viewModel.machineName.collectAsState()

    if (cameraPermissionState.status.isGranted) {
        val bitmap = viewModel.image.collectAsState()
        Column {
            PolaroidBitmapImage(
                bitmap = bitmap.value?.asImageBitmap(),
                caption = "${title.value} — ${score.value}",
                contentDescription = "Photo of ${title.value} with score ${score.value}",
                modifier = Modifier.sizeIn(minHeight = 200.dp)
            )
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize().wrapContentSize().widthIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "Whoops! Looks like we need your camera to work our magic!" +
                        "Don't worry, we just wanna see your pretty face (and maybe some cats).  " +
                        "Grant us permission and let's get this party started!"
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Hi there! We need your camera to work our magic! ✨\n" +
                        "Grant us permission and let's get this party started! \uD83C\uDF89"
            }
            Text(textToShow, textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Unleash the Camera!")
            }
        }
    }
}


/**
 * A composable that displays a bitmap image in a Polaroid-style format with text at the bottom.
 *
 * @param bitmap The bitmap image to display
 * @param caption The text to display at the bottom of the Polaroid
 * @param modifier Modifier to be applied to the component
 * @param contentDescription Description of the image for accessibility
 */
@Composable
fun PolaroidBitmapImage(
    bitmap: ImageBitmap?, // Using coil3.Bitmap as seen in your imports
    caption: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(2.dp)
            ),
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image part of the Polaroid
            // Convert coil3.Bitmap to androidx.compose.ui.graphics.ImageBitmap
            val imageBitmap = bitmap
            imageBitmap?.let {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .defaultMinSize(200.dp, 200.dp)
                        .fillMaxWidth()
                        .background(Color.Red)
                        .aspectRatio(1f) // Square aspect ratio for typical Polaroid look
                )
            }

            // Caption part (white space at the bottom)
            Text(
                text = caption,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                color = Color.Green
            )
        }
    }
}
