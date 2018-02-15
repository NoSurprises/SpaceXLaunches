package nick.spacexlaunches

interface MainPresenterMvp {
    fun attachView(view: MainViewMvp)
    fun detachView()
    fun onStart()
    fun onRefresh()
}