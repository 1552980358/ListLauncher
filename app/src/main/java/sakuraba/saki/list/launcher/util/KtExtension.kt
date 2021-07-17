package sakuraba.saki.list.launcher.util

import android.graphics.Color
import androidx.preference.Preference

fun Preference.updateIconColor(colorStr: String?) = icon?.setTint(Color.parseColor(colorStr))

fun Preference.updateIconColor(color: Int) = icon?.setTint(color)