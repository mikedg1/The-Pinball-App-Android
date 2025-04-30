package com.mikedg.thepinballapp.util

import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApiCallHandlerTest {

    @Test
    fun `safeApiCall returns Success with data when call succeeds`() = runTest {
        // Given
        val expectedData = "Success"
        
        // When
        val result = safeApiCall { expectedData }
        
        // Then
        assertTrue(result is ApiResult.Success)
        assertEquals(expectedData, (result as ApiResult.Success).data)
    }
    
    @Test
    fun `safeApiCall returns NetworkError when IOException occurs`() = runTest {
        // Given
        val ioException = IOException("Network error")
        
        // When
        val result = safeApiCall<String> { throw ioException }
        
        // Then
        assertTrue(result is ApiResult.Error.NetworkError)
        assertEquals(ioException, (result as ApiResult.Error.NetworkError).exception)
        assertEquals("Network error: Network error", result.displayMessage)
    }
    
    @Test
    fun `safeApiCall returns specific message for UnknownHostException`() = runTest {
        // Given
        val exception = UnknownHostException()
        
        // When
        val result = safeApiCall<String> { throw exception }
        
        // Then
        assertTrue(result is ApiResult.Error.NetworkError)
        assertEquals("No internet connection", (result as ApiResult.Error).displayMessage)
    }
    
    @Test
    fun `safeApiCall returns specific message for SocketTimeoutException`() = runTest {
        // Given
        val exception = SocketTimeoutException()
        
        // When
        val result = safeApiCall<String> { throw exception }
        
        // Then
        assertTrue(result is ApiResult.Error.NetworkError)
        assertEquals("Connection timed out", (result as ApiResult.Error).displayMessage)
    }
    
    @Test
    fun `safeApiCall returns HttpError when HttpException occurs`() = runTest {
        // Given
        val httpException = HttpException(Response.error<String>(404, okhttp3.ResponseBody.create(null, "")))
        
        // When
        val result = safeApiCall<String> { throw httpException }
        
        // Then
        assertTrue(result is ApiResult.Error.HttpError)
        val error = result as ApiResult.Error.HttpError
        assertEquals(404, error.code)
        assertEquals("Resource not found", error.displayMessage)
    }
    
    @Test
    fun `safeApiCall returns specific message for 401 error`() = runTest {
        // Given
        val httpException = HttpException(Response.error<String>(401, okhttp3.ResponseBody.create(null, "")))
        
        // When
        val result = safeApiCall<String> { throw httpException }
        
        // Then
        assertTrue(result is ApiResult.Error.HttpError)
        assertEquals("Unauthorized: Invalid API key", (result as ApiResult.Error).displayMessage)
    }
    
    @Test
    fun `safeApiCall returns specific message for 429 error`() = runTest {
        // Given
        val httpException = HttpException(Response.error<String>(429, okhttp3.ResponseBody.create(null, "")))
        
        // When
        val result = safeApiCall<String> { throw httpException }
        
        // Then
        assertTrue(result is ApiResult.Error.HttpError)
        assertEquals("Too many requests. Please try again later.", (result as ApiResult.Error).displayMessage)
    }
    
    @Test
    fun `safeApiCall returns UnknownError for other exceptions`() = runTest {
        // Given
        val exception = IllegalArgumentException("Invalid argument")
        
        // When
        val result = safeApiCall<String> { throw exception }
        
        // Then
        assertTrue(result is ApiResult.Error.UnknownError)
        assertEquals(exception, (result as ApiResult.Error.UnknownError).exception)
        assertEquals("Unknown error: Invalid argument", result.displayMessage)
    }
}