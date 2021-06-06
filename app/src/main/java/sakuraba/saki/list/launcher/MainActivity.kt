package sakuraba.saki.list.launcher

import android.os.Bundle
import android.view.Menu
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import sakuraba.saki.list.launcher.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {
    
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var _activityMainBinding: ActivityMainBinding? = null
    private val activityMainBinding get() = _activityMainBinding!!
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home), activityMainBinding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        activityMainBinding.navView.setupWithNavController(navController)
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    
    override fun onDestroy() {
        _activityMainBinding = null
        super.onDestroy()
    }
    
}