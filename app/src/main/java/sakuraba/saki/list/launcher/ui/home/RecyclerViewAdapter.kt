package sakuraba.saki.list.launcher.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import sakuraba.saki.list.launcher.R

class RecyclerViewAdapter(
    private var itemList: List<AppInfo>,
    private val fragmentManager: FragmentManager
): RecyclerView.Adapter<ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_sub_view, parent, false))
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView_icon.setImageDrawable(itemList[position].icon)
        holder.textView_app_name.text = itemList[position].name
        holder.textView_package_name.text = itemList[position].packageName
        holder.relativeView_root.setOnClickListener {
        
        }
        holder.relativeView_root.setOnLongClickListener {
            ApplicationInfoBottomSheetDialogFragment(itemList[position]).show(fragmentManager)
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