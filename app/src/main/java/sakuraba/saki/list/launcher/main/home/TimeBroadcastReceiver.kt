package sakuraba.saki.list.launcher.main.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_TIMEZONE_CHANGED
import android.content.Intent.ACTION_TIME_CHANGED
import android.content.Intent.ACTION_TIME_TICK
import android.content.IntentFilter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Locale

class TimeBroadcastReceiver: BroadcastReceiver() {
    
    private var textViewTime: TextView? = null
    private var textViewDate: TextView? = null
    
    private val time = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val date = SimpleDateFormat("yyyy-MM-dd E, Z", Locale.getDefault())
    
    private val intentFilter = IntentFilter().apply {
        addAction(ACTION_TIME_TICK)
        addAction(ACTION_TIME_CHANGED)
        addAction(ACTION_TIMEZONE_CHANGED)
    }
    
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_TIMEZONE_CHANGED,
            ACTION_TIME_CHANGED,
            ACTION_TIME_TICK -> {
                System.currentTimeMillis().apply {
                    textViewTime?.text = time.format(this)
                    textViewDate?.text = date.format(this)
                }
            }
        }
    }
    
    fun getRegister(context: Context) = context.registerReceiver(this, intentFilter)
    
    fun getUnregister(context: Context) = context.unregisterReceiver(this)
    
    val hasInitialized get() = textViewTime != null && textViewDate != null
    
    fun setTextView(textViewTime: TextView? = null, textViewDate: TextView? = null) {
        this.textViewTime = textViewTime
        this.textViewDate = textViewDate
    }
    
    fun getTimeInit() {
        System.currentTimeMillis().apply {
            textViewTime?.text = time.format(this)
            textViewDate?.text = date.format(this)
        }
    }
    
}