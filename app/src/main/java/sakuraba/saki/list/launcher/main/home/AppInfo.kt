package sakuraba.saki.list.launcher.main.home

import android.graphics.drawable.Drawable
import java.io.Serializable

class AppInfo(val name: String, val packageName: String, val icon: Drawable, val isSystem: Boolean, val versionName: String, val versionCode: Long): Serializable {
    var pinYin = ""
}