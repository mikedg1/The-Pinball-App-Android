package com.mikedg.thepinballapp.util

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Represents the result of an API call.
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    sealed class Error : ApiResult<Nothing>() {
        data class NetworkError(val exception: IOException) : Error()
        data class HttpError(val code: Int, val message: String) : Error()
        data class UnknownError(val exception: Throwable) : Error()
        
        val displayMessage: String
            get() = when (this) {
                is NetworkError -> when (exception) {
                    is UnknownHostException -> "No internet connection"
                    is SocketTimeoutException -> "Connection timed out"
                    else -> "Network error: ${exception.message}"
                }
                is HttpError -> when (code) {
                    401 -> "Unauthorized: Invalid API key"
                    403 -> "Access forbidden"
                    404 -> "Resource not found"
                    429 -> "Too many requests. Please try again later."
                    500 -> "Server error. Please try again later."
                    else -> "HTTP Error $code: $message"
                }
                is UnknownError -> "Unknown error: ${exception.message}"
            }
    }
}

/**
 * Safely executes an API call and returns an [ApiResult].
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
    return try {
        ApiResult.Success(apiCall())
    } catch (e: HttpException) {
        ApiResult.Error.HttpError(e.code(), e.message())
    } catch (e: IOException) {
        ApiResult.Error.NetworkError(e)
    } catch (e: Exception) {
        ApiResult.Error.UnknownError(e)
    }
}