package com.sng.miumap.ui.profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
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
}
