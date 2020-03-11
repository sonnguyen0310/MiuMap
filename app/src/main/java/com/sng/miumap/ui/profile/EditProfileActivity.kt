package com.sng.miumap.ui.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import com.sng.miumap.R
import com.sng.miumap.model.Profile
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val profile = intent.getParcelableExtra<Profile>("profile")
        setupUI(profile)
    }

    private fun setupUI(profile: Profile?) {
        val profile = profile ?: return
        Picasso.get().load(profile.imageUrl).into(profile_image_view)
        first_name_edit_text.setText(profile.firstName)
        last_name_edit_text.setText(profile.lastName)
        email_edit_text.setText(profile.email)
        phone_number_edit_text.setText(profile.phoneNumber)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_profile_options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu_item -> finish()
            android.R.id.home -> {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.confirmation))
                    .setMessage("Do you really want to exit?")
                    .setPositiveButton(getString(R.string.ok)) { _, _ -> finish() }
                    .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
                    .show()
            }
        }
        return true
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val extras = data?.extras
        val imageBitmap = extras?.get("data") as Bitmap
        profile_image_view.setImageBitmap(imageBitmap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == OPEN_CAMERA_REQUEST_CODE && permissions.first() == Manifest.permission.CAMERA) {
        }
    }

    private fun openCameraIfApplicable() {
        if (!needToAskPermissionForCamera()) {
            return
        }
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(
                this,
                "Camera permission is needed to show the camera preview",
                Toast.LENGTH_SHORT
            ).show()
        }
        requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, OPEN_CAMERA_REQUEST_CODE)
        }
    }

    private fun needToAskPermissionForCamera() =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M

    companion object {
        const val OPEN_CAMERA_REQUEST_CODE = 0
    }
}
