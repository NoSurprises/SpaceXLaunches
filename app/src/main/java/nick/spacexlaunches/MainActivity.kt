package nick.spacexlaunches

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import data.Flight


class MainActivity : AppCompatActivity(), MainViewMvp {

    val swipeRefresh: SwipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.swipe_refresh) }
    val flights: LinearLayout by lazy { findViewById<LinearLayout>(R.id.flights) }
    val presenter = MainPresenter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.attachView(this)
        swipeRefresh.setOnRefreshListener { presenter.onRefresh() }
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun removeFlights() {
        flights.removeAllViews()
    }

    override fun addFlight(flight: Flight) {
        Log.d(TAG, "add flight: ");
        val newFlight = layoutInflater.inflate(R.layout.flight, flights, false)

        newFlight.findViewById<TextView>(R.id.rocket_name).text = flight.rocketName
        newFlight.findViewById<TextView>(R.id.details).text = flight.details
        newFlight.findViewById<TextView>(R.id.launch).text = flight.launch
        flights.addView(newFlight)
    }

    override fun setChildImage(i: Int, image: Bitmap) {
        flights.getChildAt(i).findViewById<ImageView>(R.id.icon).setImageBitmap(image)
    }

    override fun showLoadingIndicator() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        swipeRefresh.isRefreshing = false
    }
}
