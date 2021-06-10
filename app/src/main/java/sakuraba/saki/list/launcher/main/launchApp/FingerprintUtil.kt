@file:Suppress("DEPRECATION")
package sakuraba.saki.list.launcher.main.launchApp

import android.app.Service
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import sakuraba.saki.list.launcher.R

interface FingerprintUtil {
    
    fun checkSupportFingerprint(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        /**
         * if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
         *     @Suppress("DEPRECATION")
         *     val fingerprintManager = context.getSystemService(FingerprintManager::class.java)
         *     @Suppress("DEPRECATION")
         *     if (fingerprintManager == null || !fingerprintManager.isHardwareDetected) {
         *         return false
         *     }
         *     return true
         * }
         * // Method deprecated in API 30
         * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
         *     (context.getSystemService(Service.BIOMETRIC_SERVICE) as BiometricManager).canAuthenticate()
         * }
         **/
        // Solution source:
        // https://stackoverflow.com/questions/50968732/determine-if-biometric-hardware-is-present-and-the-user-has-enrolled-biometrics
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }
    
    fun getFingerprintAuth(context: Context, callback: FingerprintAuthCallback, cancellationSignal: CancellationSignal) {
        // if (Build.VERSION.SDK_INT in (Build.VERSION_CODES.M .. Build.VERSION_CODES.O)) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getFingerprintAuthP(context, callback, cancellationSignal)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getFingerprintAuthM(context, callback, cancellationSignal)
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.M)
    fun getFingerprintAuthM(context: Context, callback: FingerprintAuthCallback, cancellationSignal: CancellationSignal) {
        @Suppress("DEPRECATION")
        (context.getSystemService(Service.FINGERPRINT_SERVICE) as FingerprintManager)
            .authenticate(null, cancellationSignal, 0, object : FingerprintManager.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    callback.error(errorCode, errString?.toString())
                }
                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                    callback.help(helpCode, helpString?.toString())
                }
                override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
                    callback.success()
                }
                override fun onAuthenticationFailed() {
                    callback.failed()
                }
            }, null)
    }
    
    @RequiresApi(Build.VERSION_CODES.P)
    fun getFingerprintAuthP(context: Context, callback: FingerprintAuthCallback, cancellationSignal: CancellationSignal) {
        BiometricPrompt.Builder(context)
            .setTitle(context.getString(R.string.fingerprint_auth_title))
            .setDescription(context.getString(R.string.fingerprint_auth_description))
            .setNegativeButton(context.getString(R.string.fingerprint_auth_cancel), context.mainExecutor) { _, _ ->
                callback.exit()
            }.build()
            .authenticate(cancellationSignal, context.mainExecutor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    callback.error(errorCode, errString?.toString())
                }
                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                    callback.help(helpCode, helpString?.toString())
                }
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    callback.success()
                }
                override fun onAuthenticationFailed() {
                    callback.failed()
                }
            })
        
    }
    
}