package nick.spacexlaunches


import android.util.Log
import data.Flight
import org.json.JSONArray
import org.json.JSONObject
import utils.FetchNetwork
import utils.FetchNetworkListener
import utils.LoadImage
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainModel(private val presenter: MainPresenterMvp) : MainModelMvp, FetchNetworkListener {

    private var flights = ArrayList<Flight>()
    private var imagesAsync: LoadImage? = null
    private var apiAsync: FetchNetwork? = null
    private val dateFormat = SimpleDateFormat("EEE, d MMM yyyy", Locale.US)
    private var year = 2017
    private val url = "https://api.spacexdata.com/v2/launches?launch_year="

    override fun fetchSpaceXFlights() {
        cancelAsyncTasks()

        apiAsync = FetchNetwork()
        apiAsync?.setFetchNetworkListener(this)?.execute(URL(url + year.toString()))
    }

    override fun changeYear(year: Int) {
        this.year = year
    }

    override fun getYear(): Int {
        return year
    }

    override fun onFinishResponseFromApi(result: String) {
        val jsonData = JSONArray(result)
        flights.clear()
        Log.i(TAG, "length: ${flights.size}")
        for (i in 0 until jsonData.length()) {
            val flightRaw = jsonData.getJSONObject(i)
            addNewFlight(flightRaw)
        }

        imagesAsync = LoadImage()
        imagesAsync?.setListener(this)?.execute(flights)

        presenter.receiveSpacexFlights(flights)
    }

    private fun addNewFlight(flightRaw: JSONObject) {
        val rocketName = flightRaw.getJSONObject("rocket").getString("rocket_name")
        val launch = flightRaw.getLong("launch_date_unix")
        val iconUrl = flightRaw.getJSONObject("links").getString("mission_patch")
        val details = flightRaw.getString("details")
        val article = flightRaw.getJSONObject("links").getString("article_link")
        val datelaunch = dateFormat.format(Date(launch * 1000))

        flights.add(Flight(rocketName, datelaunch, iconUrl = URL(iconUrl), details = details, article = article))
    }

    override fun onImageLoaded(i: Int) {
        Log.v(TAG, "imageLoaded: $i")
        presenter.imageLoaded(i)
    }

    override fun getFlight(i: Int): Flight {
        return flights[i]
    }

    override fun cancelAsyncTasks() {
        imagesAsync?.onDestroy()
        apiAsync?.onDestroy()

        imagesAsync = null
        apiAsync = null
    }
}