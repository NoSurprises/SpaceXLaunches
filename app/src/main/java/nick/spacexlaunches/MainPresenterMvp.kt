package nick.spacexlaunches

import data.Flight

interface MainPresenterMvp {
    fun attachView(view: MainViewMvp)
    fun detachView()
    fun onStart()
    fun onRefresh()
    fun receiveSpacexFlights(flighs: List<Flight>)
    fun imageLoaded(i: Int)
    fun flightClicked(flight: Flight)
}