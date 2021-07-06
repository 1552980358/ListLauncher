# ListLauncher

## About ListLauncher

- A simple launcher for Android with customizations of all

## Maintenance Language
- Kotlin

## Supported API
- Android L - 5.0 (API 21)
- Android L_MR1 - 5.1 (API 22)
- Android M - 6.0 (API 23)
- Android N - 7.0 (API 24)
- Android N_MR1 - 7.1 (API 25)
- Android O - 8.0 (API 26)
- Android O_MR1 - 8.1 (API 27)
- Android P - 9.0 (API 28)
- Android Q - 10.0 (API 29)
- Android R - 11.0 (API 30)
- ~Android S - 12.0 (API 31)~ [Will Be Supported later]

## IDE
- Intellij Idea

## Comment

- Quoted with `/** **/` with code
    - Provides important messages and warnings about the quoted code, telling about whether the quoted code is useless or have harmful shortcomings, solution might be provided at the comment nearby
    - [For example](https://github.com/1552980358/ListLauncher/blob/master/app/src/main/java/sakuraba/saki/list/launcher/main/setting/SettingFragment.kt#L254)
  ```kotlin
  /**
   * Due to the limitation of Android Q [Build.VERSION_CODES.Q], application directories,
   * including the external directory fetched with application-owned [Context], e.g. [Context.getExternalFilesDir],
   * the cropping of image is handled by external application, that there is limitation on the access of the file.
   * For fetching the cropped file, we can only use the public directory to store and access the cropped image.
   *
   * 用中文简单的说一下，反正就是[Context]限制的，一切用[Context]获取的路径，包括[Context.getExternalFilesDir]，都是应用专属，
   * 使用com.android.camera.action.CROP会调用非本应用处理，由于[Context]的限制，导致无法保存。所以只能用公共空间来保存访问了。
   *
   * This solution is suggested by
   * 解决方案是来自 [https://blog.csdn.net/qq_35584878/article/details/115284323]
   *
   * No need, cropping will be handled by [CropImageView] in the [CropImageFragment]
   *
   * // @Suppress("DEPRECATION")
   * // cropImageUri = Uri.fromFile(File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES), BACKGROUND_FILE))
   **/
  val getImageContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
      when (activityResult.resultCode) {
          RESULT_OK -> {
              findNavController().navigate(R.id.nav_crop_image, Bundle().apply { putString(CROP_URI, activityResult.data?.data?.toString()) })
          }
          else -> {
              Snackbar.make(findActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout), R.string.setting_background_snackbar_fetching_image_fail, LENGTH_SHORT).show()
          }
       }
  }
  ```

- Quoted with `/** **/` without code inside
    - Provides important messages and warnings that the code nearby is important, and the reason will be provided in the comment quote or other comment quote.
    - [For example](https://github.com/1552980358/ListLauncher/blob/master/app/src/main/java/sakuraba/saki/list/launcher/util/NavigationBarUtil.kt#L14)
  ```kotlin
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
  ```

- Comments with `//` with text
    - Talks about the code below
    - Can be ignored, just a small remind about the code
    - [For example](https://github.com/1552980358/ListLauncher/blob/master/app/src/main/java/sakuraba/saki/list/launcher/util/NavigationBarUtil.kt#L24)
  ```kotlin
  when {
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ->
          // Require API 29+
          display?.getRealSize(this)
      // Deprecated on API 29
      else -> windowManager.defaultDisplay.getRealSize(this)
  }
  ```
  
- Comments with `//` with code
  - Just remove the commented code, might due to
    1. Deprecated, with new method
    2. A better solution is found
  - [For example](https://github.com/1552980358/ListLauncher/blob/master/app/src/main/java/sakuraba/saki/list/launcher/main/home/HomeFragment.kt#L58)
  ```kotlin
  // val appInfos = requireContext().packageManager.queryIntent.getInstalledApplications(0)
  ```
  
## Credit

- [TinyPinYin](https://github.com/promeG/TinyPinyin)
  ```
  'com.github.promeg:tinypinyin:2.0.3'
  'com.github.promeg:tinypinyin-lexicons-android-cncity:2.0.3'
  ```
  
- [Google Inc.](https://developer.android.com/)
 ```
 'androidx.core:core-ktx:1.6.0'
 'androidx.appcompat:appcompat:1.3.0'
 'com.google.android.material:material:1.4.0'
 'androidx.constraintlayout:constraintlayout:2.0.4'
 'androidx.navigation:navigation-fragment-ktx:2.3.5'
 'androidx.navigation:navigation-ui-ktx:2.3.5'
 'androidx.lifecycle:lifecycle-livedata-ktx:2.3.1'
 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1'
 'androidx.navigation:navigation-fragment-ktx:2.3.5'
 'androidx.navigation:navigation-ui-ktx:2.3.5'
 'androidx.preference:preference-ktx:1.1.1'
 'androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01'
 'com.github.promeg:tinypinyin:2.0.3'
 'com.github.promeg:tinypinyin-lexicons-android-cncity:2.0.3'
 ```

- [JetBrains](https://www.jetbrains.com/)
  ```
  "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
  'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0'
  'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0'
  ```
  
## Open-sourced License - [GNU GENERAL PUBLIC LICENSE Version 3](LICENSE)
```
                    GNU GENERAL PUBLIC LICENSE
                      Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
```