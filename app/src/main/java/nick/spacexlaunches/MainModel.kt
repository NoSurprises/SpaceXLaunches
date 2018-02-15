package nick.spacexlaunches

import android.util.Log
import data.Flight
import org.json.JSONArray
import utils.FetchNetwork
import utils.FetchNetworkListener
import utils.LoadImage
import java.net.URL

class MainModel(val presenter: MainPresenterMvp) : MainModelMvp, FetchNetworkListener {

    override fun fetchSpaceXFlights() {
        val url = URL("https://api.spacexdata.com/v2/launches?launch_year=2017")
        FetchNetwork().setFetchNetworkListener(this).execute(url)
    }

    private var flights = ArrayList<Flight>()

    override fun onFinishResponseFromApi(result: String) {
        val jsonData = JSONArray(result)
        flights.clear()
        Log.i(TAG, "length: ${flights.size}")
        for (i in 0 until jsonData.length()) {
            val flightRaw = jsonData.getJSONObject(i)

            val rocketName = flightRaw.getJSONObject("rocket").getString("rocket_name")
            val launch = flightRaw.getLong("launch_date_unix")
            val iconUrl = flightRaw.getJSONObject("links").getString("mission_patch")
            val details = flightRaw.getString("details")

            flights.add(Flight(rocketName, launch.toString(), iconUrl = URL(iconUrl), details = details))
        }
        LoadImage().setListener(this).execute(flights)
        presenter.receiveSpacexFlights(flights)

    }

    override fun onImageLoaded(i: Int) {
        Log.d(TAG, "imageLoaded: $i")
        presenter.imageLoaded(i)
    }

    override fun getFlight(i: Int): Flight {
        return flights[i]
    }
}