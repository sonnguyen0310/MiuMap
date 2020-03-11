package com.sng.miumap.ui.profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
        Picasso.get().load(profile.imageUrl).into(profile_image_view as ImageView)
        first_name_edit_text.setText(profile.firstName)
        last_name_edit_text.setText(profile.lastName)
        email_edit_text.setText(profile.email)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
