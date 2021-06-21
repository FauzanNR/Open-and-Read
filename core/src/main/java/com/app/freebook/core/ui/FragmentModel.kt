package com.app.freebook.core.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.freebook.core.R


abstract class FragmentModel : Fragment() {

    companion object {
        var isConnected: Boolean = false
    }

    private val connectionBroadcastReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val isNotConnected =
                    intent?.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
                Log.d("isNotConnected", isNotConnected.toString())
                if (isNotConnected == false) {
                    isConnected = true
                    onConnected()

                } else {
                    isConnected = false
                    onDisconnected()

                    Toast.makeText(
                        this@FragmentModel.context,
                        R.string.no_connection,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    abstract fun onDisconnected() //block process
    abstract fun onConnected() //process

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().registerReceiver(
            connectionBroadcastReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(connectionBroadcastReceiver)
    }
}