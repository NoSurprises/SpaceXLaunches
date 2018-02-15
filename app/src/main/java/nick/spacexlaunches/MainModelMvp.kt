package nick.spacexlaunches

import data.Flight

interface MainModelMvp {
    fun fetchSpaceXFlights()
    fun getFlight(i: Int): Flight
    fun cancelAsyncTasks()
    fun changeYear(year: Int)
    fun getYear(): Int
}