package nick.spacexlaunches

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
    }

    override fun onStart() {
        onRefresh()
    }

    override fun onRefresh() {
        view?.showLoadingIndicator()
        model.fetchSpaceXFlights()
    }


    override fun receiveSpacexFlights(flighs: List<Flight>) {
        view?.hideLoadingIndicator()
        flighs.forEach({ x -> view?.addFlight(x) })
    }
}