package com.mobileinsights.httpclient

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.logging.Logger

fun httpGet(
    endpoint: String,
    onSuccess: (HttpClientResponse) -> Unit,
    onError: (Exception) -> Unit = { error ->
        Logger.getLogger(null, error.message.toString())
    }
) {
    CoroutineScope(Dispatchers.IO).launch {
        val url = URL(endpoint)
        val openedConnection = url.openConnection() as HttpURLConnection
        openedConnection.requestMethod = "GET"
        val responseCode = openedConnection.responseCode
        try {
            val reader = BufferedReader(InputStreamReader(openedConnection.inputStream))
            val apiResponse = HttpClientResponse(responseCode, reader.readText())
            reader.close()
            // Call the success callback on the main thread
            launch(Dispatchers.Main) {
                onSuccess(apiResponse)
            }
        } catch (e: Exception) {
            // Handle error cases and call the error callback on the main thread
            launch(Dispatchers.Main) {
                onError(Exception("HTTP Request failed with response code $responseCode"))
            }
        }
    }
}

data class HttpClientResponse(
    val responseCode: Int,
    val response: String
)