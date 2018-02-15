package nick.spacexlaunches

import android.graphics.Bitmap
import data.Flight

interface MainViewMvp {
    fun showLoadingIndicator()
    fun hideLoadingIndicator()
    fun addFlight(flight: Flight)
    fun setChildImage(i: Int, image: Bitmap)
    fun removeFlights()
    fun showToast(msg: String)
    fun openBrowserFor(link: String)
    fun showYearSelectionDialog()
    fun showEmptyMessage()
    fun hideEmptyMessage()
}