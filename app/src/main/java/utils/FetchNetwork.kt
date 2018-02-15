package utils

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class FetchNetwork : AsyncTask<URL, Void, String>() {
    private var listener: FetchNetworkListener? = null

    override fun doInBackground(vararg params: URL?): String {

        val url = params[0]
        val response = StringBuffer()

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
        listener?.onFinishResponseFromApi(result ?: "")
        listener = null

    }

    fun setFetchNetworkListener(listener: FetchNetworkListener): FetchNetwork {
        this.listener = listener
        return this
    }

}