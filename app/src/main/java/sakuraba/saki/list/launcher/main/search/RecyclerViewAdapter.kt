package sakuraba.saki.list.launcher.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.base.Constants
import sakuraba.saki.list.launcher.main.home.AppInfo
import sakuraba.saki.list.launcher.main.setting.SettingContainer

class RecyclerViewAdapter(private val appInfos: ArrayList<AppInfo>, private val textView: TextView, private val activity: FragmentActivity): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_sub_view, parent, false))
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageDrawable(appInfos[position].icon)
        holder.textViewAppName.text = appInfos[position].name
        holder.textViewPackageName.text = appInfos[position].packageName
        holder.relativeViewRoot.setOnClickListener {
            activity.findNavController(R.id.nav_host_fragment).navigate(R.id.nav_launch_app, Bundle().apply {
                putString(Constants.LAUNCH_APPLICATION_NAME, appInfos[position].name)
                putString(Constants.LAUNCH_PACKAGE_NAME, appInfos[position].packageName)
                putSerializable(SettingContainer.SETTING_CONTAINER, activity.intent.getSerializableExtra(SettingContainer.SETTING_CONTAINER))
            })
        }
    }
    
    override fun getItemCount(): Int {
        if (appInfos.isEmpty()) {
            textView.visibility = VISIBLE
            return 0
        }
        textView.visibility = GONE
        return appInfos.size
    }
    
}

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val imageView: ImageView = view.findViewById(R.id.imageView_icon)!!
    val textViewAppName: TextView = view.findViewById(R.id.textView_app_name)!!
    val textViewPackageName: TextView = view.findViewById(R.id.textViewPackageName)!!
    val relativeViewRoot: RelativeLayout = view.findViewById(R.id.relativeView_root)!!
}