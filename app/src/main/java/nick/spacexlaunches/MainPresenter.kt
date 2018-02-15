package nick.spacexlaunches

class MainPresenter : MainPresenterMvp {
    var view: MainViewMvp? = null

    override fun attachView(view: MainViewMvp) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    override fun onStart() {

    }

    override fun onRefresh() {
        view?.hideLoadingIndicator()
    }
}