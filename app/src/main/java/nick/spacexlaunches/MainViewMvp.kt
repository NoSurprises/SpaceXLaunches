package nick.spacexlaunches

import data.Flight

interface MainViewMvp {
    fun showLoadingIndicator()
    fun hideLoadingIndicator()
    fun addFlight(flight: Flight)
}