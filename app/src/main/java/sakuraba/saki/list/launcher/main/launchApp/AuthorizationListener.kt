package sakuraba.saki.list.launcher.main.launchApp

import java.io.Serializable

interface AuthorizationListener: Serializable {
    
    companion object {
        const val AUTHORIZATION_LISTENER = "AuthorizationListener"
    }
    
    fun onAuthFailed()
    
    fun onAuthComplete()
    
}