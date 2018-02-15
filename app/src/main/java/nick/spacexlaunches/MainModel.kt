package nick.spacexlaunches

import data.Flight
import utils.FetchNetwork
import utils.FetchNetworkListener
import java.net.URL

class MainModel(val presenter: MainPresenterMvp) : MainModelMvp, FetchNetworkListener {

    override fun fetchSpaceXFlights() {
        val url = URL("https://api.spacexdata.com/v2/launches?launch_year=2017")
        FetchNetwork().setFetchNetworkListener(this).execute(url)
    }

    override fun onFinish(result: String) {
        val flights = ArrayList<Flight>()
        flights.add(Flight(result))

        presenter.receiveSpacexFlights(flights)
    }
}