package com.mikedg.thepinballapp.features.photoscore

import android.R
import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhotoScoreViewModel @Inject constructor(): ViewModel(){
    private val _image = MutableStateFlow<Bitmap?>(null)
    val image: StateFlow<Bitmap?> = _image
    private val _score = MutableStateFlow<String?>(null)
    val score: StateFlow<String?> = _score

    fun showScore(string: String, bitmap: Bitmap) {
        _score.value = string
        _image.value = bitmap
    }

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    sealed class UiEvent {
        data object TakePhoto : UiEvent()
    }
    sealed class UiState {
//        data object Loading : UiState()
        data class Content(val message: String) : UiState()
        data class Error(val message: String) : UiState()
        data object WaitingForPhoto : UiState()
        data object PhotoReceived : UiState()
    }

    init {
        viewModelScope.launch {
            delay(1000) // TODO: too fast is missed?
            _events.emit(UiEvent.TakePhoto)
        }
    }

    // TODO: this breaks stuff? wtf in building...
//
//    // Used to set up a link between the Camera and your UI.
//    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
//    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest
//
//    private val cameraPreviewUseCase = Preview.Builder().build().apply {
//        setSurfaceProvider { newSurfaceRequest ->
//            _surfaceRequest.update { newSurfaceRequest }
//        }
//    }
//
//    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
//        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
//        processCameraProvider.bindToLifecycle(
//            lifecycleOwner, DEFAULT_FRONT_CAMERA, cameraPreviewUseCase
//        )
//
//        // Cancellation signals we're done with the camera
//        try { awaitCancellation() } finally { processCameraProvider.unbindAll() }
//    }

}