package utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import data.Flight
import java.net.HttpURLConnection

class LoadImage : AsyncTask<List<Flight>, Int, Boolean>() {

    private var listener: FetchNetworkListener? = null

    override fun onPreExecute() {
        super.onPreExecute()
        Log.d(TAG, "image loading start: ");
    }

    override fun doInBackground(vararg params: List<Flight>?): Boolean {
        if (params.size != 1)
            cancel(true)
        val flights = params[0]
        for (i in 0 until flights?.size!!) {
            if (isCancelled) {
                Log.d(TAG, "stop doInBackground images: ");
                return false
            }
            val x = flights[i]

            with(x.iconUrl.openConnection() as HttpURLConnection) {
                inputStream.use {
                    val bigIcon = BitmapFactory.decodeStream(it)
                    x.icon = Bitmap.createScaledBitmap(bigIcon, 200, 200, false)
                    publishProgress(i)
                }
            }
        }
        return true
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        if (!isCancelled) {
            val iChildImageLoaded = values[0]!!
            listener?.onImageLoaded(iChildImageLoaded)
        }

    }

    fun setListener(listener: FetchNetworkListener): LoadImage {
        this.listener = listener
        return this
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        listener = null
        Log.d(TAG, "postexecute images: ");
    }

    fun onDestroy() {
        cancel(true)
        listener = null
        Log.d(TAG, "images destroy isCancelled $isCancelled: ");
    }
}