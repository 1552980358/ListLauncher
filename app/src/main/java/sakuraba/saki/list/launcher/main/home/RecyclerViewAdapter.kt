package sakuraba.saki.list.launcher.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.base.Constants.Companion.LAUNCH_APPLICATION_NAME
import sakuraba.saki.list.launcher.base.Constants.Companion.LAUNCH_PACKAGE_NAME
import sakuraba.saki.list.launcher.dialog.ApplicationInfoDialogFragment
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.SETTING_CONTAINER

class RecyclerViewAdapter(
    private var itemList: List<AppInfo>,
    private val activity: FragmentActivity
): RecyclerView.Adapter<ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_sub_view, parent, false))
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView_icon.setImageDrawable(itemList[position].icon)
        holder.textView_app_name.text = itemList[position].name
        holder.textView_package_name.text = itemList[position].packageName
        holder.relativeView_root.setOnClickListener {
            /**
            it?.context?.startActivity(
                Intent(it.context, LaunchAppActivity::class.java)
                    .putExtra(LAUNCH_APPLICATION_NAME, itemList[position].name)
                    .putExtra(LAUNCH_PACKAGE_NAME, itemList[position].packageName)
            )
            **/
            activity.findNavController(R.id.nav_host_fragment).navigate(R.id.nav_launch_app, Bundle().apply {
                putString(LAUNCH_APPLICATION_NAME, itemList[position].name)
                putString(LAUNCH_PACKAGE_NAME, itemList[position].packageName)
                putSerializable(SETTING_CONTAINER, activity.intent.getSerializableExtra(SETTING_CONTAINER))
            })
        }
        holder.relativeView_root.setOnLongClickListener {
            ApplicationInfoDialogFragment(itemList[position]).show(activity.supportFragmentManager)
            return@setOnLongClickListener true
        }
    }
    
    override fun getItemCount(): Int {
        return itemList.size
    }
    
}

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val imageView_icon = view.findViewById<ImageView>(R.id.imageView_icon)!!
    val textView_app_name = view.findViewById<TextView>(R.id.textView_app_name)!!
    val textView_package_name = view.findViewById<TextView>(R.id.textViewPackageName)!!
    val relativeView_root = view.findViewById<RelativeLayout>(R.id.relativeView_root)!!
}