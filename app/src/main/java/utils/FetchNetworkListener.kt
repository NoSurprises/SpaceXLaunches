package utils

interface FetchNetworkListener {
    fun onFinishResponseFromApi(result: String)
    fun onImageLoaded(i: Int)
}