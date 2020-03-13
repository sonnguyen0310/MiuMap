package com.sng.miumap.ui.profile

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.sng.miumap.R
import com.sng.miumap.model.Profile
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_profile_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu_item -> {
                finish()
                return true
            }
            android.R.id.home -> showDiscardEditAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        if (requestCode == OPEN_CAMERA_REQUEST_CODE) {
            val image = data?.extras?.get("data") as Bitmap
            profile_image_view.setImageBitmap(image)
        } else if (requestCode == OPEN_GALLERY_REQUEST_CODE) {
            profile_image_view.setImageURI(data?.data)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.first() == PERMISSION_GRANTED) {
                    openCamera()
                } else if (grantResults.first() == PERMISSION_DENIED &&
                    !shouldShowRequestPermissionRationale(CAMERA)
                ) {
                    showCameraPermissionRequiredAlertDialog()
                }
            }
            REQUEST_GALLERY_PERMISSION_REQUEST_CODE -> {
                if (grantResults.first() == PERMISSION_GRANTED) {
                    openGallery()
                } else if (grantResults.first() == PERMISSION_DENIED &&
                    !shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)
                ) {
                    showGalleryPermissionRequiredAlertDialog()
                }
            }
        }
    }

    private fun showGalleryPermissionRequiredAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_required))
            .setMessage(getString(R.string.gallery_permission_required_description))
            .setPositiveButton(getString(R.string.settings)) { _, _ -> openSettings() }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }

    private fun showDiscardEditAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirmation))
            .setMessage(getString(R.string.cancel_confirmation_message))
            .setPositiveButton(getString(R.string.ok)) { _, _ -> finish() }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .show()
    }

    private fun showCameraPermissionRequiredAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_required))
            .setMessage(getString(R.string.camera_permission_required_description))
            .setPositiveButton(getString(R.string.settings)) { _, _ -> openSettings() }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }

    private fun setupUI() {
        val profile = intent.getParcelableExtra<Profile>("profile") ?: return
        with(profile) {
            Picasso.get().load(imageUrl).placeholder(R.drawable.ic_launcher_background)
                .into(profile_image_view)
            first_name_edit_text.setText(firstName)
            last_name_edit_text.setText(lastName)
            email_edit_text.setText(email)
            phone_number_edit_text.setText(phoneNumber)
        }
    }

    fun changeProfileImageButtonClicked(view: View) {
        val menu = PopupMenu(view.context, view)
        menu.menuInflater.inflate(R.menu.choose_photo_picker_menu, menu.menu)
        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.choose_photo_camera -> openCameraIfApplicable()
                R.id.choose_photo_gallery -> openGalleryIfApplicable()
            }
            true
        }
        menu.show()
    }

    private fun openGalleryIfApplicable() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery()
            return
        }
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            openGallery()
        } else {
            requestPermissions(
                arrayOf(READ_EXTERNAL_STORAGE),
                REQUEST_GALLERY_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE)
    }

    private fun openCameraIfApplicable() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openCamera()
            return
        }
        if (checkSelfPermission(CAMERA) == PERMISSION_GRANTED) {
            openCamera()
        } else {
            requestPermissions(arrayOf(CAMERA), REQUEST_CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, OPEN_CAMERA_REQUEST_CODE)
        }
    }

    private fun openSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
    }

    companion object {
        const val REQUEST_CAMERA_PERMISSION_REQUEST_CODE = 0
        const val REQUEST_GALLERY_PERMISSION_REQUEST_CODE = 1
        const val OPEN_CAMERA_REQUEST_CODE = 2
        const val OPEN_GALLERY_REQUEST_CODE = 3
    }
}
