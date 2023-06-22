package com.example.gshelppassanger

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log

import android.widget.TextView
import android.widget.Toast
import com.example.gshelppassanger.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.view.View
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImg: Uri
    private val PICK_IMAGE_REQUEST = 123

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.setTitle("Profile")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.imageProfile.setOnClickListener {
            selectImage()
        }
        binding.btnSave.setOnClickListener {
            checkData()
            insertDataToFragment()
        }
//      binding.etFirstName.isEnabled = false
    }

    private fun insertDataToFragment() {
        if (binding.etFirstName.text.toString().isEmpty() && binding.etLastName.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
        } else {
            val fragment = ProfileFragment()
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()

            val imageDrawable = binding.imageProfile.drawable

            val imageBitmap: Bitmap = (imageDrawable as BitmapDrawable).bitmap
            val outputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            val profileImg = Base64.encodeToString(byteArray, Base64.DEFAULT)

            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.apply() {
                putString("firstName", firstName)
                putString("lastName", lastName)
                putString("profileImg", profileImg)
            }.apply()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, fragment)
            fragmentTransaction.commit()
            Toast.makeText(this, "Data inserted and updated successfully", Toast.LENGTH_SHORT).show()
            binding.btnSave.isEnabled = false
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun checkData() {
        if (selectedImg == null) {
            Toast.makeText(this, "Please select your image", Toast.LENGTH_SHORT).show()
        } else uploadData()
    }

    private fun uploadData() {
        val reference = storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(selectedImg).addOnCompleteListener {
            if (it.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        val user = UserModel(
            auth.uid.toString(),
            binding.etFirstName.text.toString(),
            binding.etLastName.text.toString(),
            imgUrl
        )

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
//                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
//                finish()
            }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImg = data.data!!
            binding.imageProfile.setImageURI(selectedImg)
        }
    }
}