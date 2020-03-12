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
import android.widget.Toast
import com.sng.miumap.R
import com.sng.miumap.model.Profile
import com.sng.miumap.network.APIClient
import com.sng.miumap.network.ResponseWrapper
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        APIClient.apiInterface.profile().enqueue(object : Callback<ResponseWrapper<Profile>> {
            override fun onResponse(
                call: Call<ResponseWrapper<Profile>>,
                response: Response<ResponseWrapper<Profile>>
            ) {
                if (response.body()?.data != null) {
                    updateUI(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<ResponseWrapper<Profile>>, t: Throwable) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
            }
        })

        return root
    }

    private fun updateUI(profile: Profile) {
        Picasso.get().load(profile.imageUrl).placeholder(R.drawable.ic_launcher_background)
            .into(avatarImageView)
        nameTextView.text = profile.fullName()
        emailTextView.text = profile.email
        phoneNumberTextView.text = profile.phoneNumber ?: getString(R.string.not_applicable)
        editProfileImageButton.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            intent.putExtra("profile", profile)
            startActivityForResult(intent, EDIT_PROFILE_RESULT_CODE)
        }
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
