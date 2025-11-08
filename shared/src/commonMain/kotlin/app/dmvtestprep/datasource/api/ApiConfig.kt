package app.dmvtestprep.datasource.api

import app.dmvtestprep.core.config.AppInfo
import app.dmvtestprep.core.config.apiBaseUrl
import app.dmvtestprep.core.config.apiVersion
import app.dmvtestprep.core.config.mediaBaseUrl

object ApiConfig {
    fun getApiEndpoint(path: String): String {
        val baseUrl = normalizeUrl(AppInfo.Build.apiBaseUrl)

        return buildUrl(baseUrl, AppInfo.Build.apiVersion, path)
    }

    fun getFullImageUrl(path: String?): String? {
        val baseUrl = normalizeUrl(AppInfo.Build.mediaBaseUrl)

        return path?.takeIf { it.isNotBlank() }?.let {
            buildUrl(baseUrl, it)
        }
    }

    private fun normalizeUrl(address: String, scheme: String = "https"): String {
        return address.trim().let {
            if (it.startsWith("http", true)) it else "$scheme://$it"
        }
    }

    private fun buildUrl(baseUrl: String, vararg paths: String): String {
        return paths.fold(baseUrl.trimEnd('/')) { acc, path ->
            "$acc/${path.trimStart('/')}"
        }
    }
}