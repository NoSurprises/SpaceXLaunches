package nick.spacexlaunches

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.change_year -> {
                presenter.clickedChangeYear()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

    override fun showYearSelectionDialog() {
        val picker = AlertDialog.Builder(this)
        picker.setTitle("Change year")
        val yearSelection = layoutInflater.inflate(R.layout.year_selection, null)
        val years = yearSelection.findViewById<NumberPicker>(R.id.year_selector)
        years.minValue = 2000
        years.maxValue = 2018

        picker.setView(yearSelection)
        picker.setPositiveButton(
                "OK",
                DialogInterface.OnClickListener { dialog, which -> presenter.changeYear(years.value) }
        ).setNegativeButton(
                "CANCEL",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }
        )
        picker.create()
        picker.show()
    }
}
