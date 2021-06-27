package sakuraba.saki.list.launcher

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import sakuraba.saki.list.launcher.broadcast.ApplicationChangeBroadcastReceiver
import sakuraba.saki.list.launcher.broadcast.ApplicationChangeBroadcastReceiver.Companion.APPLICATION_CHANGE_BROADCAST_RECEIVER
import sakuraba.saki.list.launcher.databinding.ActivityMainBinding
import sakuraba.saki.list.launcher.main.MainViewModel
import sakuraba.saki.list.launcher.main.setting.SettingContainer
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_STATUS_BAR_BLACK_TEXT
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_CUSTOM_STATUS_BAR_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.KEY_STATUS_BAR_COLOR
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER

class MainActivity: AppCompatActivity() {
    
    private lateinit var viewModel: MainViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var _activityMainBinding: ActivityMainBinding? = null
    private val activityMainBinding get() = _activityMainBinding!!
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.setSettingContainer(SettingContainer(this))
        viewModel.setApplicationChangeBroadcastReceiver(ApplicationChangeBroadcastReceiver(this))
    
        intent!!.putExtra(SETTING_CONTAINER, viewModel.settingContainer.value)
        intent.putExtra(APPLICATION_CHANGE_BROADCAST_RECEIVER, viewModel.applicationChangeBroadcastReceiver.value)
    
        getCustomizeSystem()
        
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        // setContentView(R.layout.activity_main)
        setContentView(activityMainBinding.root)
        // val toolbar: Toolbar = findViewById(R.id.toolbar)
        // setSupportActionBar(toolbar)
        setSupportActionBar(activityMainBinding.root.findViewById(R.id.toolbar))
        
        // val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        // val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_setting), activityMainBinding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        activityMainBinding.navView.setupWithNavController(navController)
    }
    
    private fun getCustomizeSystem() {
        viewModel.settingContainer.value?.apply {
            if (getBooleanValue(KEY_CUSTOM_STATUS_BAR_BLACK_TEXT) == true) {
                when  {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                        setTheme(R.style.Theme_ListLauncher_LightStatusBar)
                    }
                    else -> {
                        @Suppress("DEPRECATION", "InlinedApi")
                        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
            }
            if (getBooleanValue(KEY_CUSTOM_STATUS_BAR_COLOR) == true
                && getStringValue(KEY_STATUS_BAR_COLOR) != null) {
                window.statusBarColor = Color.parseColor(getStringValue(KEY_STATUS_BAR_COLOR))
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    
    override fun onDestroy() {
        viewModel.applicationChangeBroadcastReceiver.value?.getUnregistered(this)
        _activityMainBinding = null
        super.onDestroy()
    }
    
}