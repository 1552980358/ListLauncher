package sakuraba.saki.list.launcher.main.launchApp

interface FingerprintAuthCallback {
    
    fun success()
    
    fun failed()
    
    fun error(code: Int, message: String?)
    
    fun exit()
    
}