package utils

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL


class FetchNetwork : AsyncTask<URL, Void, String>() {
    private var listener: FetchNetworkListener? = null

    override fun doInBackground(vararg params: URL?): String {
        if (params.size != 1)
            cancel(true)
        val url = params[0]

        val response = StringBuilder()

        with(url?.openConnection() as HttpURLConnection) {
            BufferedReader(InputStreamReader(inputStream)).use {
                var line = it.readLine()
                while (line != null) {
                    response.append(line)
                    line = it.readLine()
                }
            }
        }
        return response.toString()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        listener?.onFinish(result ?: "")
    }

    fun setFetchNetworkListener(listener: FetchNetworkListener): FetchNetwork {
        this.listener = listener
        return this
    }

}