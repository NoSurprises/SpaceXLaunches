package nick.spacexlaunches

import android.util.Log
import data.Flight
import org.json.JSONArray
import utils.FetchNetwork
import utils.FetchNetworkListener
import utils.LoadImage
import java.net.URL

class MainModel(val presenter: MainPresenterMvp) : MainModelMvp, FetchNetworkListener {

    private var flights = ArrayList<Flight>()
    private var imagesAsync: LoadImage? = null
    private var apiAsync: FetchNetwork? = null

    override fun fetchSpaceXFlights() {
        cancellAsyncTasks()
        val url = URL("https://api.spacexdata.com/v2/launches?launch_year=2017")
        apiAsync = FetchNetwork()
        apiAsync?.setFetchNetworkListener(this)?.execute(url)
    }

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
        imagesAsync = LoadImage()
        imagesAsync?.setListener(this)?.execute(flights)

        presenter.receiveSpacexFlights(flights)
    }

    override fun onImageLoaded(i: Int) {
        Log.v(TAG, "imageLoaded: $i")
        presenter.imageLoaded(i)
    }

    override fun getFlight(i: Int): Flight {
        return flights[i]
    }

    override fun cancellAsyncTasks() {
        imagesAsync?.onDestroy()
        apiAsync?.onDestroy()

        imagesAsync = null
        apiAsync = null
    }
}