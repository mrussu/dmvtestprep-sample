package app.dmvtestprep.datasource.api

object ApiProvider {

    private val apiClient by lazy { ApiClient() }
    val apiService by lazy { ApiService(apiClient) }

}