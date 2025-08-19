package app.dmvtestprep.datasource.api

import app.dmvtestprep.domain.model.ErrorTypes
import app.dmvtestprep.model.BodyData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.io.IOException
import kotlinx.serialization.json.Json

class ApiClient {

    val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 15000
            requestTimeoutMillis = 30000
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
        HttpResponseValidator {
            validateResponse { response ->
                if (response.status.value in 400..599) {
                    throw ErrorTypes.fromResponseCode(
                        code = response.status.value,
                        error = response.status.description
                    )
                }
            }
            handleResponseException { cause, _ ->
                throw when (cause) {
                    is ErrorTypes.HttpResponse -> cause
                    is ConnectTimeoutException -> ErrorTypes.Network.ConnectTimeout
                    is HttpRequestTimeoutException -> ErrorTypes.Network.HttpRequestTimeout
                    is SocketTimeoutException -> ErrorTypes.Network.SocketTimeout
                    is TimeoutCancellationException -> ErrorTypes.Network.TimeoutCancellation
                    is IOException -> ErrorTypes.Network.NoInternetConnection
                    else -> ErrorTypes.Network.Unknown.from(cause.message)
                }
            }
        }
    }

    suspend inline fun <reified R> get(path: String): R {
        return httpClient.get(ApiConfig.getApiEndpoint(path)).body()
    }

    suspend inline fun <reified T, reified R> post(path: String, body: BodyData<T>): R {
        
        return httpClient.post(ApiConfig.getApiEndpoint(path)) {
            header("Content-Type", "application/json")
            setBody(body)
        }.body()
    }

}