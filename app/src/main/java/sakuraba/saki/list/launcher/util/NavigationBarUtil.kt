package sakuraba.saki.list.launcher.util

import android.app.Activity
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.view.Display
import android.view.WindowManager
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import sakuraba.saki.list.launcher.R

private val Activity.pixelHeight get(): Int = Point().apply {
    /**
     * Running [resources.displayMetrics.heightPixels] cannot get correct heightPixels.
     * e.g. on my Xiaomi Mi 10 Ultra, height pixel is 2340, but calling [resources.displayMetrics.heightPixels]
     * will return 2206, where is much greater than the [Rect.bottom] of [DrawerLayout] of [MainActivity].
     * So, we should use the [WindowManager.getDefaultDisplay] (API < 29) or [Display.getRealSize] (API >= 29)
     * to get real heightPixel.
     **/
    @Suppress("DEPRECATION")
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ->
            // Require API 29+
            display?.getRealSize(this)
        // Deprecated on API 29
        else -> windowManager.defaultDisplay.getRealSize(this)
    }
}.y

/**
 * Get bottom pixel of the root view
 * Root view is [DrawerLayout] with ID [R.id.drawer_layout]
 **/
private val Activity.getRootViewBottom get(): Int = Rect().apply { findViewById<DrawerLayout>(R.id.drawer_layout) }.bottom

/**
 * Check whether Navigation Bar exists
 **/
val Activity.hasNavigationBar get(): Boolean = getRootViewBottom < pixelHeight

val Fragment.hasNavigationBar get() = requireActivity().hasNavigationBar