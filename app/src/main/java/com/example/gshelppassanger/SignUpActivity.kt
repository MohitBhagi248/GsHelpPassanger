package com.example.gshelppassanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.gshelppassanger.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignup.setOnClickListener {
            nearestGasStation()
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun nearestGasStation() {
        val et_email = binding.etEmail.text.toString()
        val et_password = binding.etPassword.text.toString()
        val et_confirm_password = binding.etConfirmPassword.text.toString()

        if(et_email.isNotEmpty() && et_password.isNotEmpty() && et_confirm_password.isNotEmpty()) {
            if(et_password == et_confirm_password) {
                firebaseAuth.createUserWithEmailAndPassword(et_email, et_password).addOnCompleteListener {
                    if(it.isSuccessful) {
                        val intent = Intent(this@SignUpActivity, NearestGasStation::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this,it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this,"Password is not matching", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this,"Empty fields are not allowed", Toast.LENGTH_LONG).show()
        }
    }
}