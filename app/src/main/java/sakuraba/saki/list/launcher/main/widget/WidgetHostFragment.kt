package sakuraba.saki.list.launcher.main.widget

import android.app.Activity.RESULT_OK
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import sakuraba.saki.list.launcher.databinding.FragmentWidgetHostBinding

/**
 * This [WidgetHostFragment] is workable demo with widget on pure Android OS Emulator.
 * However, there is a crash on the MIUI devices with
 * [java.lang.NullPointerException]: Attempt to read from field 'com.android.server.appwidget.AppWidgetServiceImpl$ProviderId com.android.server.appwidget.AppWidgetServiceImpl$Provider.id' on a null object reference
 *
 * Maybe need some more time on solving this problem so that using app widgets could be workable on every device
 **/
class WidgetHostFragment: Fragment() {
    
    companion object {
        private const val APP_WIDGET_HOST_ID = 1024
    }
    
    private var _fragmentWidgetHostBinding: FragmentWidgetHostBinding? = null
    private val fragmentWidgetHost get() = _fragmentWidgetHostBinding!!
    
    private lateinit var appWidgetManager: AppWidgetManager
    private lateinit var appWidgetHost: AppWidgetHost
    
    private lateinit var registerSelectWidget: ActivityResultLauncher<Intent>
    private lateinit var registerCreateWidget: ActivityResultLauncher<Intent>
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _fragmentWidgetHostBinding = FragmentWidgetHostBinding.inflate(inflater)
        return fragmentWidgetHost.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        appWidgetManager = AppWidgetManager.getInstance(requireContext())
        appWidgetHost = AppWidgetHost(requireContext(), APP_WIDGET_HOST_ID)
        
        registerCreateWidget = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.e("registerCreateWidget", "result")
            if (result.resultCode == RESULT_OK) {
                val extras = result?.data?.extras ?: return@registerForActivityResult
                createWidget(extras)
            }
        }
        
        registerSelectWidget = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.e("registerSelectWidget", "result")
            if (result.resultCode == RESULT_OK) {
                val extras = result.data?.extras
                val widgetId = extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)!!
                val widgetInfo = appWidgetManager.getAppWidgetInfo(widgetId)
                if (widgetInfo.configure != null) {
                    registerCreateWidget.launch(
                        Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE).apply {
                            component = widgetInfo.configure
                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                        }
                    )
                } else {
                    createWidget(extras)
                }
            }
        }
    
        registerSelectWidget.launch(
            Intent(AppWidgetManager.ACTION_APPWIDGET_PICK).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetHost.allocateAppWidgetId())
                putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, arrayListOf())
                putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, arrayListOf())
            }
        )
        
    }
    
    private fun createWidget(extras: Bundle) {
        val widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val info = appWidgetManager.getAppWidgetInfo(widgetId)
        val widgetHostView = appWidgetHost.createView(requireContext(), widgetId, info)
        widgetHostView.setAppWidget(widgetId, info)
        fragmentWidgetHost.root.addView(widgetHostView)
    }
    
    override fun onStart() {
        super.onStart()
        appWidgetHost.startListening()
    }
    
    override fun onStop() {
        super.onStop()
        appWidgetHost.stopListening()
    }
    
}