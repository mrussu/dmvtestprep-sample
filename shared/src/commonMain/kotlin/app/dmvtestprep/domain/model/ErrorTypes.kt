package app.dmvtestprep.domain.model

sealed class ErrorTypes(open val type: String) : Throwable() {

    abstract val error: String
    abstract val details: String

    fun toErrorSummary(): ErrorSummary {
        return createErrorSummary(this)
    }

    sealed class Network(
        override val error: String,
        override val details: String
    ) : ErrorTypes("Network error") {

        data object ConnectTimeout : Network(
            error = "Connection timeout",
            details = "The connection to the server timed out, please check your network."
        )

        data object HttpRequestTimeout : Network(
            error = "Request timeout",
            details = "The server did not respond in time, please retry."
        )

        data object SocketTimeout : Network(
            error = "Socket timeout",
            details = "The connection to the server timed out."
        )

        data object TimeoutCancellation : Network(
            error = "Operation timeout",
            details = "The operation was cancelled due to timeout."
        )

        data object NoInternetConnection : Network(
            error = "No internet connection",
            details = "Please check your network and try again."
        )

        data class Unknown(
            override val error: String,
            override val details: String
        ) : Network(error, details) {
            companion object {
                fun from(message: String?): Unknown {
                    return Unknown(
                        error = "Unknown network error",
                        details = message ?: "An unknown network error occurred."
                    )
                }
            }
        }
    }

    sealed class HttpResponse(
        open val code: Int,
        override val error: String,
        override val details: String
    ) : ErrorTypes("Undefined response") {

        sealed class Client(
            code: Int,
            error: String,
            details: String
        ) : HttpResponse(code, error, details) {

            override val type = "Client error response"

            data object BadRequest : Client(
                code = 400,
                error = "Bad request",
                details = "The server couldn't understand your request."
            )

            data object Unauthorized : Client(
                code = 401,
                error = "Unauthorized",
                details = "Please check your authentication credentials."
            )

            data object Forbidden : Client(
                code = 403,
                error = "Forbidden",
                details = "You don't have permission to access this resource."
            )

            data object NotFound : Client(
                code = 404,
                error = "Not found",
                details = "The requested resource could not be found."
            )

            data class Undefined(
                override val code: Int,
                override val error: String,
                override val details: String = "An error occurred on the client side."
            ) : Client(code, error, details)
        }

        sealed class Server(
            code: Int,
            error: String,
            details: String
        ) : HttpResponse(code, error, details) {

            override val type = "Server error response"

            data object InternalServerError : Server(
                code = 500,
                error = "Internal server error",
                details = "The server encountered an error."
            )

            data object BadGateway : Server(
                code = 502,
                error = "Bad gateway",
                details = "The server received an invalid response."
            )

            data object ServiceUnavailable : Server(
                code = 503,
                error = "Service unavailable",
                details = "The server is temporarily unavailable."
            )

            data class Undefined(
                override val code: Int,
                override val error: String,
                override val details: String = "An error occurred on the server side."
            ) : Server(code, error, details)
        }

        data class Undefined(
            override val code: Int,
            override val error: String,
            override val details: String = "An undefined response status was received."
        ) : HttpResponse(code, error, details)
    }

    companion object {
        fun fromResponseCode(code: Int, error: String): ErrorTypes {
            return when (code) {
                400 -> HttpResponse.Client.BadRequest
                401 -> HttpResponse.Client.Unauthorized
                403 -> HttpResponse.Client.Forbidden
                404 -> HttpResponse.Client.NotFound
                in 400..499 -> HttpResponse.Client.Undefined(code, error)
                500 -> HttpResponse.Server.InternalServerError
                502 -> HttpResponse.Server.BadGateway
                503 -> HttpResponse.Server.ServiceUnavailable
                in 500..599 -> HttpResponse.Server.Undefined(code, error)
                else -> HttpResponse.Undefined(code, error)
            }
        }

        private fun createErrorSummary(e: ErrorTypes): ErrorSummary {
            return ErrorSummary(
                type = e.type,
                error = when (e) {
                    is Network -> e.error
                    is HttpResponse -> "${e.code} ${e.error}"
                },
                details = e.details
            )
        }
    }
}