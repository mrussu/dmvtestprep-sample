package app.dmvtestprep.datasource.api

object ApiConfig {

    private const val API_VERSION: String = "v1"
    private const val BASE_URL_API: String = "https://api.example.com"
    private const val BASE_URL_MEDIA: String = "https://cdn.example.com"

    private fun buildUrl(baseUrl: String, vararg paths: String): String {
        return paths.fold(baseUrl.trimEnd('/')) { acc, path ->
            "$acc/${path.trimStart('/')}"
        }
    }

    fun getApiEndpoint(path: String): String {
        return buildUrl(BASE_URL_API, API_VERSION, path)
    }

    fun getFullImageUrl(path: String?): String? {
        return path?.takeIf { it.isNotBlank() }?.let {
            buildUrl(BASE_URL_MEDIA, it)
        }
    }

}