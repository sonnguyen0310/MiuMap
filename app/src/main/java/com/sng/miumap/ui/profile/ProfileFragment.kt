package com.sng.miumap.ui.profile

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.sng.miumap.R
import com.sng.miumap.model.Profile
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var avatarImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var editProfileImageButton: ImageButton
    private lateinit var emailTextView: TextView
    private lateinit var phoneNumberTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        initSubviews(root)

        val profile = Profile(
            "001", "Monkey", "D. Luffy", "hello@miu.edu", null,
            "https://avatarfiles.alphacoders.com/125/thumb-125919.jpg"
        )

        Picasso.get().load(profile.imageUrl).into(avatarImageView)
        nameTextView.text = profile.fullName()
        emailTextView.text = profile.email
        phoneNumberTextView.text = profile.phoneNumber ?: getString(R.string.not_applicable)
        editProfileImageButton.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            intent.putExtra("profile", profile)
            startActivityForResult(intent, EDIT_PROFILE_RESULT_CODE)
        }

        return root
    }

    private fun initSubviews(view: View) {
        avatarImageView = view.findViewById(R.id.avatar_image_view)
        nameTextView = view.findViewById(R.id.name_text_view)
        editProfileImageButton = view.findViewById(R.id.edit_profile_image_button)
        emailTextView = view.findViewById(R.id.email_text_view)
        phoneNumberTextView = view.findViewById(R.id.phone_number_text_view)
    }

    companion object {
        const val EDIT_PROFILE_RESULT_CODE = 1
    }
}
