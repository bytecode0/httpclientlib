package com.mobileinsights.httpclient

// Custom exception class for HTTP errors
sealed class HttpException(
    open val responseCode: Int,
    override val message: String
) : Exception(message) {
    class HttpClientException(
        override val responseCode: Int,
        override val message: String
    ) : HttpException(responseCode, message) {
        override fun toString(): String {
            return "HttpClientException(responseCode=$responseCode, message='$message')"
        }
    }

    class HttpServerException(
        override val responseCode: Int,
        override val message: String
    ) : HttpException(responseCode, message) {
        override fun toString(): String {
            return "HttpServerErrorException(responseCode=$responseCode, message='$message')"
        }
    }

    class HttpInformationalException(
        override val responseCode: Int,
        override val message: String
    ) : HttpException(responseCode, message) {
        override fun toString(): String {
            return "HttpServerErrorException(responseCode=$responseCode, message='$message')"
        }
    }
}