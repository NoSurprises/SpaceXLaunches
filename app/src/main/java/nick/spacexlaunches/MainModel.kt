package nick.spacexlaunches

import data.Flight
import org.json.JSONArray
import utils.FetchNetwork
import utils.FetchNetworkListener
import java.net.URL

class MainModel(val presenter: MainPresenterMvp) : MainModelMvp, FetchNetworkListener {

    override fun fetchSpaceXFlights() {
        val url = URL("https://api.spacexdata.com/v2/launches?launch_year=2017")
        FetchNetwork().setFetchNetworkListener(this).execute(url)
    }

    override fun onFinish(result: String) {
        val jsonData = JSONArray(result)
        val flights = ArrayList<Flight>()

        for (i in 0 until jsonData.length()) {
            val flightRaw = jsonData.getJSONObject(i)

            val rocketName = flightRaw.getJSONObject("rocket").getString("rocket_name")
            val launch = flightRaw.getLong("launch_date_unix")
            val icon = flightRaw.getJSONObject("links").getString("mission_patch")
            val details = flightRaw.getString("details")

            flights.add(Flight(rocketName, launch.toString(), icon, details))
        }

        presenter.receiveSpacexFlights(flights)
    }
}