package sakuraba.saki.list.launcher.broadcast

import java.io.Serializable

fun interface ApplicationChangeListener: Serializable {
    fun onApplicationUpdate()
}