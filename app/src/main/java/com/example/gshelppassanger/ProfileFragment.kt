package com.example.gshelppassanger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.gshelppassanger.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val firstNameFragment = sharedPreferences?.getString("firstName", "")
        val lastNameFragment = sharedPreferences?.getString("lastName", "")
        val profileImgBase = sharedPreferences?.getString("profileImg", "")

        _binding?.tvFirstName?.text = firstNameFragment
        _binding?.tvLastName?.text = lastNameFragment

        _binding?.imageProfile?.isEnabled = false
        _binding?.imageProfile?.isClickable = false
        _binding?.imageProfile?.isFocusable = false

        if (profileImgBase != null) {
            val imageByteArray = Base64.decode(profileImgBase, Base64.DEFAULT)
            val imageBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            _binding?.imageProfile?.setImageBitmap(imageBitmap)

        }

        _binding!!.btnUpdate.setOnClickListener {
            update()
        }

        _binding!!.btnDelete.setOnClickListener {
            deleteData()
        }
        return binding.root

//        val firstNameFragment: TextView = view.findViewById(R.id.tvFirstName)
//        val lastNameFragment: TextView = view.findViewById(R.id.tvLastName)
//        val firstNameValue = arguments?.getString("firstName")
//        val lastNameValue = arguments?.getString("lastName")
//        firstNameFragment.setText(firstNameValue)
//        lastNameFragment.setText(lastNameValue)

    }

    private fun deleteData() {
        Toast.makeText(requireContext(), "Profile deleted successfully", Toast.LENGTH_SHORT).show()
        Log.e("", "Delete Data Function call");
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        if (editor != null) {
            editor?.remove("firstName")
            editor?.remove("lastName")
            editor?.remove("profileImg")
            editor?.apply()
        }

        _binding?.tvFirstName?.text = ""
        _binding?.tvLastName?.text = ""

        // Clear the image view
        _binding?.imageProfile?.setImageResource(R.drawable.round_account_circle_24)
        database = FirebaseDatabase.getInstance().getReference("users")
        database.removeValue().addOnSuccessListener {
        }
    }

    private fun update() {
        val intent = Intent(requireContext(), Profile::class.java)
        startActivity(intent)
    }
}