import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.mikedg.thepinballapp.data.remote.OpenAiService
import com.mikedg.thepinballapp.features.photoscore.PhotoScoreViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//package com.mikedg.thepinballapp.features.photoscore.view
//
//import androidx.camera.compose.CameraXViewfinder
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.widthIn
//import androidx.compose.foundation.layout.wrapContentSize
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.compose.LocalLifecycleOwner
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.isGranted
//import com.google.accompanist.permissions.rememberPermissionState
//import com.google.accompanist.permissions.shouldShowRationale
//import com.mikedg.thepinballapp.features.photoscore.PhotoScoreViewModel
//
//import android.content.Context
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.camera.view.PreviewView
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.remember
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.core.content.ContextCompat
//import kotlin.coroutines.resume
//import kotlin.coroutines.suspendCoroutine
//
@Composable
fun PhotoScore() {
    val viewModel = hiltViewModel<PhotoScoreViewModel>()
    val context = LocalContext.current
    var photoUri = remember { mutableStateOf<Uri?>(null) }

    val createImageUri = {
        // For MediaStore approach (recommended for shared storage)
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
//        contract = ActivityResultContracts.StartActivityForResult()
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
//    ) { result ->
            photoUri.value?.let { uri ->
                // Resize the image
                coroutineScope.launch {
                    val resizedBitmap = resizeImageFromUri(context, uri, 800, 600)

                    // Now you can pass the resized bitmap to your function
                    resizedBitmap?.let { bitmap ->
                        viewModel.showScore("", bitmap)
                        // TODO: remove from here
                        val openAi = OpenAiService()

                        val response = openAi.getScore(resizedBitmap)

                        val moshi = Moshi.Builder()
                            .addLast(KotlinJsonAdapterFactory())
                            .build()
                        val jsonAdapter = moshi.adapter(Any::class.java).indent("  ") // Pretty print with indentation
                        val prettyJson = jsonAdapter.toJson(response)
                        Log.d("PhotoScore", "Pretty JSON Response: $prettyJson")
                        val scoreInfo = response.output.joinToString("\n") {
                            it.content.joinToString("\n") { it.text }
                        }
                        Log.d(
                            "PhotoScore", "Response: ${
                                scoreInfo
                            }"
                        )
                        viewModel.showScore(scoreInfo, resizedBitmap)
//                        yourFunctionThatNeedsSmallImage(bitmap)
                        // Works!
//                        viewModel.fetchScoresForImage(bitmap)
//                        val openai = OpenAi(
//                            token = "your-api-key",
//                            timeout = Timeout(socket = 60.seconds),
//                            // additional configurations...
//                        )
                    }
                }
            }

//        if (result.resultCode == Activity.RESULT_OK && result.data?.extras?.get("data") != null) {
            // Handle the image captured
//            val imageBitmap = result.data?.extras?.get("data") as? Bitmap // Thumbnail?
//            imageBitmap?.let {
//                https://developer.android.com/media/camera/camera-deprecated/photobasics
            // Do something with the bitmap
//                viewModel.onImageCaptured(it)
            // Save the image to the gallery

//            }
        } else {
            // Handle cancellation or failure
//            viewModel.onCameraCancelled()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PhotoScoreViewModel.UiEvent.TakePhoto -> {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (cameraIntent.resolveActivity(context.packageManager) != null) {
                        createImageUri()?.let { uri ->
                            photoUri.value = uri
                            cameraLauncher.launch(uri)
//                            cameraLauncher.launch(cameraIntent)
                        }
                    }
                }
                // Handle other events...
            }
        }
    }

    CameraPreviewScreen(viewModel)
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
    val inputStream = context.contentResolver.openInputStream(uri)
    val exifInterface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        ExifInterface(inputStream!!)
    } else {
        // For older Android versions, you need the file path
        val path = getRealPathFromURI(context, uri)
        ExifInterface(path!!)
    }
    inputStream?.close()

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
//
//// Difference format than node WTF?
//            return@withContext rotatedBitmap
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

//        // Further resize if needed for exact dimensions
//        if (sampledBitmap != null && (sampledBitmap.width > targetWidth || sampledBitmap.height > targetHeight)) {
//            val scaleFactor = min(
//                targetWidth.toFloat() / sampledBitmap.width,
//                targetHeight.toFloat() / sampledBitmap.height
//            )
//
//            val resizedBitmap = Bitmap.createScaledBitmap(
//                sampledBitmap,
//                (sampledBitmap.width * scaleFactor).toInt(),
//                (sampledBitmap.height * scaleFactor).toInt(),
//                true
//            )
//
//            // If we created a new bitmap, recycle the sampled one
//            if (resizedBitmap != sampledBitmap) {
//                sampledBitmap.recycle()
//            }
//
//            val rotatedBitmap = getRotatedBitmap(context, uri, resizedBitmap)
//
//// Difference format than node WTF?
//            return@withContext rotatedBitmap
//            return@withContext resizedBitmap
//        }
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
fun CameraPreviewScreen(viewModel: PhotoScoreViewModel, modifier: Modifier = Modifier) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val viewModel = hiltViewModel<PhotoScoreViewModel>()
    if (cameraPermissionState.status.isGranted) {
        //CameraPreviewContent(viewModel = PhotoScoreViewModel(), modifier = modifier)
//        Text("Got permissions")
        val score = viewModel.score.collectAsState()
        val bitmap = viewModel.image.collectAsState()
        Column {
            bitmap.value?.let { image ->
                Image(
                    bitmap = image.asImageBitmap(),
                    contentDescription = "Bitmap image",
                    modifier = modifier
                )

            }
            Text(score.value.toString())

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
//
//@Composable
//fun CameraPreviewContent(
//    viewModel: PhotoScoreViewModel,
//    modifier: Modifier = Modifier,
//    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
//) {
//    Text("Camera")
////    val surfaceRequest = viewModel.surfaceRequest.collectAsStateWithLifecycle()
////    val context = LocalContext.current
////    LaunchedEffect(lifecycleOwner) {
////        viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
////    }
////
////    surfaceRequest.value?.let { request ->
//        CameraXViewfinder(
//            surfaceRequest = request,
//            modifier = modifier
//        )
////    }
//}
//
//@Composable
//fun CameraPreviewScreen() {
//    val lensFacing = CameraSelector.LENS_FACING_BACK
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val context = LocalContext.current
//    val preview = Preview.Builder().build()
//    val previewView = remember {
//        PreviewView(context)
//    }
//    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
//    LaunchedEffect(lensFacing) {
//        val cameraProvider = context.getCameraProvider()
//        cameraProvider.unbindAll()
//        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview)
//        preview.setSurfaceProvider(previewView.surfaceProvider)
//    }
//    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
//}
//
//private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
//    suspendCoroutine { continuation ->
//        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
//            cameraProvider.addListener({
//                continuation.resume(cameraProvider.get())
//            }, ContextCompat.getMainExecutor(this))
//        }
//    }

