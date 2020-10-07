package tech.skot.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import tech.skot.core.MutablePoker

class NetworkApplicativeModelImpl(private val applicationContext: Context) : NetworkApplicativeModel {

    companion object {
        private const val connectivityChange = "android.net.conn.CONNECTIVITY_CHANGE"

        fun getNetworkStatus(context: Context): NetworkApplicativeModel.Status {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
                when {
                    capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> NetworkApplicativeModel.Status.Wifi
                    capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> NetworkApplicativeModel.Status.Mobile
                    else -> NetworkApplicativeModel.Status.NotConnected
                }
            } else {
                cm.activeNetworkInfo?.let {
                    when (it.type) {
                        ConnectivityManager.TYPE_WIFI -> NetworkApplicativeModel.Status.Wifi
                        ConnectivityManager.TYPE_MOBILE -> NetworkApplicativeModel.Status.Mobile
                        else -> NetworkApplicativeModel.Status.NotConnected
                    }
                } ?: NetworkApplicativeModel.Status.NotConnected
            }
        }
    }

    private val receiver: BroadcastReceiver


    init {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                context?.let { cont ->
                    when (intent?.action) {
                        connectivityChange -> {
                            statusChangedPoker.poke()
                        }
                    }
                }

            }
        }
        applicationContext.registerReceiver(receiver, IntentFilter(connectivityChange))
    }


    override val status
        get() = getNetworkStatus(applicationContext)
    override val isConnected
        get() = status != NetworkApplicativeModel.Status.NotConnected
    override val statusChangedPoker = MutablePoker()

}