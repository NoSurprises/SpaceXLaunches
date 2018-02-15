package utils

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


val TAG = "daywint"

class FetchNetwork : AsyncTask<URL, Void, String>() {
    private var listener: FetchNetworkListener? = null

    override fun onPreExecute() {
        super.onPreExecute()
        Log.d(TAG, "starting fetch network ")
    }

    override fun doInBackground(vararg params: URL?): String {

        val url = params[0]
        val response = StringBuffer()

        with(url?.openConnection() as HttpURLConnection) {
            BufferedReader(InputStreamReader(inputStream)).use {
                var line = it.readLine()
                while (line != null) {
                    if (isCancelled) {
                        Log.d(TAG, "stop doInBackground")
                        return ""
                    }
                    response.append(line)
                    line = it.readLine()
                }
            }
        }

        return response.toString()

    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!isCancelled) {
            listener?.onFinishResponseFromApi(result ?: "")
            listener = null
        }
        Log.d(TAG, "post execute fetch network: ");
    }

    fun setFetchNetworkListener(listener: FetchNetworkListener): FetchNetwork {
        this.listener = listener
        return this
    }

    fun onDestroy() {
        cancel(true)
        Log.d(TAG, "destroy fetch network isCancelled $isCancelled")
        listener = null
    }

}