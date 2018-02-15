package nick.spacexlaunches

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity(), MainViewMvp {

    val swipeRefresh: SwipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.swipe_refresh) }

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

    override fun showLoadingIndicator() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        swipeRefresh.isRefreshing = false
    }
}
