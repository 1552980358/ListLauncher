package sakuraba.saki.list.launcher.main.search

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager

interface KeyboardUtil {
    
    private fun Activity.peekDecorView() = window?.peekDecorView()
    
    private fun InputMethodManager.hideKeyboard(view: View?) = view?.let { hideSoftInputFromWindow(it.windowToken, 0) }
    
    private fun Context.getInputMethodManager() = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)

    fun hideKeyboard(activity: Activity) = activity.getInputMethodManager().hideKeyboard(activity.peekDecorView())

}