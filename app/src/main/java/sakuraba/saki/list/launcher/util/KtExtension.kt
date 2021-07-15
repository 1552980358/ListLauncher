package sakuraba.saki.list.launcher.util

import android.graphics.Bitmap
import android.graphics.Color
import androidx.preference.Preference

val Bitmap.heightFloat: Float get() = height.toFloat()

val Bitmap.widthFloat: Float get() = width.toFloat()

fun Preference.updateIconColor(colorStr: String?) = icon?.setTint(Color.parseColor(colorStr))

fun Preference.updateIconColor(color: Int) = icon?.setTint(color)