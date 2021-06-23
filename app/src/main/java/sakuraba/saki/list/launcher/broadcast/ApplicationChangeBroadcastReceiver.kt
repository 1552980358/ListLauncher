package sakuraba.saki.list.launcher.broadcast

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import java.io.Serializable

class ApplicationChangeBroadcastReceiver(activity: Activity): BroadcastReceiver(), Serializable {
    
    companion object {
        const val APPLICATION_CHANGE_BROADCAST_RECEIVER = "ApplicationChangeBroadcastReceiver"
    }
    
    init {
        activity.registerReceiver(this, IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
        })
    }
    
    private var applicationChangeListener: ApplicationChangeListener? = null
    
    fun setApplicationChangeListener(applicationChangeListener: ApplicationChangeListener?) {
        this.applicationChangeListener = applicationChangeListener
    }
    
    override fun onReceive(context: Context?, intent: Intent?) {
        applicationChangeListener?.onApplicationUpdate()
    }
    
    fun getUnregistered(activity: Activity) = activity.unregisterReceiver(this)
    
}