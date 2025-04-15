package com.mikedg.thepinballapp.features.photoscore

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikedg.thepinballapp.data.local.ScoreResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhotoScoreViewModel @Inject constructor() : ViewModel() {
    private val _image = MutableStateFlow<Bitmap?>(null)
    val image: StateFlow<Bitmap?> = _image

    private val _score = MutableStateFlow<String>("")
    val score: StateFlow<String?> = _score

    private val _machineName = MutableStateFlow<String>("")
    val machineName: StateFlow<String?> = _machineName

    private val _opdbId = MutableStateFlow<String>("")
    val opdbId: StateFlow<String?> = _opdbId

    fun showScore(scoreResult: ScoreResult?, bitmap: Bitmap) {
        scoreResult?.let {
            _score.value = scoreResult.player_scores?.get(0).toString()
            _machineName.value = scoreResult.machine_name ?: ""
            _opdbId.value = scoreResult.opdb_id ?: ""
        } ?: run {
            _score.value = ""
            _machineName.value = ""
            _opdbId.value = ""
        }

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