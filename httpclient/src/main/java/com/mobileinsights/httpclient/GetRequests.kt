package com.mobileinsights.httpclient

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun get(
    endpoint: String,
    onSuccess: (HttpClientResponse) -> Unit,
    onError: (Throwable) -> Unit = { }
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val url = URL(endpoint)
            val connection = url.openConnection() as HttpURLConnection
            connection.apply {
                requestMethod = "GET"
                connectTimeout = 1000 // 10 seconds timeout
                readTimeout = 1000 // 10 seconds timeout
            }
            val responseCode = connection.responseCode
            // Call the success callback on the main thread
            withContext(Dispatchers.Main) {
                when (responseCode) {
                    in 200..299 -> {
                        val reader = BufferedReader(InputStreamReader(connection.inputStream))
                        val apiResponse = HttpClientResponse(responseCode, reader.readText())
                        onSuccess(apiResponse)
                    }
                    in 400..499 -> {
                        onError(
                            HttpException.HttpClientException(
                            responseCode,
                            "HTTP Request failed with client error response code $responseCode\""
                            )
                        )
                    }
                    in 500 .. 599 -> {
                        onError(
                            HttpException.HttpServerException(
                                responseCode,
                                "HTTP Request failed with server error response code $responseCode\""
                            )
                        )
                    } else -> {
                        onError(
                            HttpException.HttpServerException(
                                responseCode,
                                "HTTP Request failed with informational error response code $responseCode\""
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            // Handle error cases and call the error callback on the main thread
            withContext(Dispatchers.Main) {
                onError(e)
            }
        }
    }
}

data class HttpClientResponse(
    val responseCode: Int,
    val response: String
)