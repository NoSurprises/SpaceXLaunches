package data

import android.graphics.Bitmap
import java.net.URL


data class Flight(val rocketName: String,
                  val launch: String,
                  var icon: Bitmap? = null,
                  val iconUrl: URL,
                  val details: String,
                  val article: String)