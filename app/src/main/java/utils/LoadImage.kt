package utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import data.Flight
import java.net.HttpURLConnection

class LoadImage : AsyncTask<List<Flight>, Int, Boolean>() {

    private var listener: FetchNetworkListener? = null
    override fun doInBackground(vararg params: List<Flight>?): Boolean {
        if (params.size != 1)
            cancel(true)
        val flights = params[0]
        for (i in 0 until flights?.size!!) {
            val x = flights[i]

            with(x.iconUrl.openConnection() as HttpURLConnection) {
                inputStream.use {
                    val bigIcon = BitmapFactory.decodeStream(it)
                    x.icon = Bitmap.createScaledBitmap(bigIcon, 150, 150, false)
                    publishProgress(i)
                }
            }
        }
        return true
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        val iChildImageLoaded = values[0]!!
        listener?.onImageLoaded(iChildImageLoaded)

    }

    fun setListener(listener: FetchNetworkListener): LoadImage {
        this.listener = listener
        return this
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        listener = null
        cancel(true)
    }
}