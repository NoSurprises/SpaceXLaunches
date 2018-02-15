package nick.spacexlaunches

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import data.Flight


class MainActivity : AppCompatActivity(), MainViewMvp {

    private val swipeRefresh: SwipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.swipe_refresh) }
    private val flights: LinearLayout by lazy { findViewById<LinearLayout>(R.id.flights) }
    private val presenter = MainPresenter()

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
        Log.v(TAG, "add flight: ")
        val flightView = layoutInflater.inflate(R.layout.flight, flights, false)
        bindFlightDataToView(flight, flightView)
        flights.addView(flightView)

        flightView.setOnClickListener({ presenter.flightClicked(flight) })
    }

    private fun bindFlightDataToView(flight: Flight, flightView: View) {
        flightView.findViewById<TextView>(R.id.rocket_name).text = flight.rocketName
        flightView.findViewById<TextView>(R.id.details).text = flight.details
        flightView.findViewById<TextView>(R.id.launch).text = flight.launch
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

    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun openBrowserFor(link: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(link)
        startActivity(i)
    }
}
