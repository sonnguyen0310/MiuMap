package com.sng.miumap.ui.profile

import android.Manifest.permission.CAMERA
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

        val profile = intent.getParcelableExtra<Profile>("profile") ?: return
        setupUI(profile)
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
            android.R.id.home -> {
                showDiscardEditAlertDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == OPEN_CAMERA_REQUEST_CODE) {
            val image = data?.extras?.get("data") as Bitmap
            profile_image_view.setImageBitmap(image)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != REQUEST_CAMERA_PERMISSION_REQUEST_CODE) {
            return
        }
        if (grantResults.first() == PERMISSION_GRANTED) {
            openCamera()
        } else if (grantResults.first() == PERMISSION_DENIED &&
            !shouldShowRequestPermissionRationale(CAMERA)
        ) {
            showCameraPermissionRequiredAlertDialog()
        }
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
            .setPositiveButton(getString(R.string.settings)) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .show()
    }

    private fun setupUI(profile: Profile) {
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
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.menuInflater.inflate(R.menu.choose_photo_picker_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.choose_photo_camera -> openCameraIfApplicable()
                R.id.choose_photo_gallery -> openGallery()
            }
            true
        }
        popupMenu.show()
    }

    private fun openGallery() {
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

    companion object {
        const val REQUEST_CAMERA_PERMISSION_REQUEST_CODE = 0
        const val OPEN_CAMERA_REQUEST_CODE = 1
    }
}
