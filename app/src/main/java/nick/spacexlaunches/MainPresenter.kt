package nick.spacexlaunches

import android.util.Log
import data.Flight

val TAG = "daywint"

class MainPresenter : MainPresenterMvp {
    private var view: MainViewMvp? = null
    private var model: MainModelMvp = MainModel(this)

    override fun attachView(view: MainViewMvp) {
        this.view = view
    }

    override fun detachView() {
        view = null
        model.cancelAsyncTasks()
    }

    override fun onStart() {
        onRefresh()
    }

    override fun onRefresh() {
        view?.hideEmptyMessage()
        view?.removeFlights()
        view?.showLoadingIndicator()
        model.fetchSpaceXFlights()
    }

    override fun receiveSpacexFlights(flighs: List<Flight>) {
        view?.hideLoadingIndicator()
        Log.i(TAG, "size in presenter: ${flighs.size}")
        flighs.forEach({ x ->
            view?.addFlight(x)
            Log.v(TAG, "flight: $x")
        })

        if (flighs.size == 0) {
            view?.showEmptyMessage()
        }
    }

    override fun imageLoaded(i: Int) {
        Log.v(TAG, "setting image: $i")
        view?.setChildImage(i, model.getFlight(i).icon!!)
    }

    override fun flightClicked(flight: Flight) {
        view?.openBrowserFor(flight.article)
    }

    override fun clickedChangeYear() {
        view?.showYearSelectionDialog()
    }

    override fun changeYear(year: Int) {
        model.changeYear(year)
        onRefresh()
    }

    override fun getYear(): Int {
        return model.getYear()
    }
}