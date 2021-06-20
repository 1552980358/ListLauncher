package sakuraba.saki.list.launcher.util

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

@Suppress("UNCHECKED_CAST")
fun <T: FragmentActivity> Fragment.requireActivityAs() = requireActivity() as T

fun <T: View> Fragment.findActivityViewById(resId: Int) = requireActivity().findViewById<T>(resId)