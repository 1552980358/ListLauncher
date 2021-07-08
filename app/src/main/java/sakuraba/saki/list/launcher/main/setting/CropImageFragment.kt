package sakuraba.saki.list.launcher.main.setting

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.databinding.FragmentCropImageBinding
import sakuraba.saki.list.launcher.main.setting.SettingFragment.Companion.BACKGROUND_FILE
import sakuraba.saki.list.launcher.main.setting.UserInterfaceSettingFragment.Companion.CROP_URI
import sakuraba.saki.list.launcher.util.findActivityViewById

class CropImageFragment: Fragment() {
    
    private var _fragmentCropImageBinding: FragmentCropImageBinding? = null
    private val fragmentCropImageBinding get() = _fragmentCropImageBinding!!
    
    private lateinit var getImageContent:  ActivityResultLauncher<Intent>
    private lateinit var intent: Intent
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _fragmentCropImageBinding = FragmentCropImageBinding.inflate(inflater)
        fragmentCropImageBinding.cropImageView.setBitmap(arguments!!.getString(CROP_URI)!!)
        setHasOptionsMenu(true)
        getImageContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            when (activityResult.resultCode) {
                Activity.RESULT_OK -> {
                    val uri = activityResult.data?.data?.toString()
                    if (uri == null) {
                        Snackbar.make(findActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout), R.string.setting_background_snackbar_fetching_image_fail,
                            BaseTransientBottomBar.LENGTH_SHORT
                        ).show()
                        return@registerForActivityResult
                    }
                    fragmentCropImageBinding.cropImageView.setBitmap(uri)
                }
                else -> {
                    Snackbar.make(findActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout), R.string.setting_background_snackbar_fetching_image_fail,
                        BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        return fragmentCropImageBinding.root
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_crop_image, menu)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                fragmentCropImageBinding.cropImageView.getCutBitmap().apply {
                    if (this == null) {
                        Snackbar.make(
                            findActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout),
                            R.string.setting_background_snackbar_fetching_image_fail,
                            BaseTransientBottomBar.LENGTH_SHORT
                        ).show()
                        return false
                    }
                    requireContext().openFileOutput(BACKGROUND_FILE, MODE_PRIVATE).apply {
                        if (this == null) {
                            Snackbar.make(
                                findActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout),
                                R.string.setting_background_snackbar_storing_image_fail,
                                BaseTransientBottomBar.LENGTH_SHORT
                            ).show()
                            return false
                        }
                        try {
                            compress(Bitmap.CompressFormat.JPEG, 100, this)
                        } catch (e: Exception) {
                            Snackbar.make(
                                findActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout),
                                R.string.setting_background_snackbar_storing_image_fail,
                                BaseTransientBottomBar.LENGTH_SHORT
                            ).show()
                        }
                    }
                    findActivityViewById<DrawerLayout>(R.id.drawer_layout).background = this.toDrawable(resources)
                    findNavController().navigateUp()
                }
            }
            R.id.menu_change_image -> {
                getImageContent.launch(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    
}