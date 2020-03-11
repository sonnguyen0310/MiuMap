package com.sng.miumap.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.support.v4.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.widget.ImageButton
import android.widget.ImageView
import com.sng.miumap.R
import com.sng.miumap.model.Profile
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var avatarImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var updatePhotoImageButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        initSubviews(root)

        val profile = Profile(
            "001", "Monkey", "D. Luffy", "hello@miu.edu",
            "https://avatarfiles.alphacoders.com/125/thumb-125919.jpg"
        )

        Picasso.get().load(profile.imageUrl).into(avatarImageView)
        nameTextView.text = profile.fullName()

        return root
    }

    private fun initSubviews(view: View) {
        avatarImageView = view.findViewById(R.id.avatar_image_view)
        nameTextView = view.findViewById(R.id.name_text_view)
        updatePhotoImageButton = view.findViewById(R.id.update_photo_image_button)
    }

}
