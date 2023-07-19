package com.example.gshelppassanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.gshelppassanger.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            nearestGasStation()
        }

        binding.tvSignup.setOnClickListener {
            funSignUp()
        }
    }

    private fun nearestGasStation() {
        val et_email = binding.etEmail.text.toString()
        val et_password = binding.etPassword.text.toString()
        if (et_email.isNotEmpty() && et_password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(et_email, et_password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this@LoginActivity, NearestGasStation::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser != null){
            val intent = Intent(this@LoginActivity, NearestGasStation::class.java)
            startActivity(intent)
        }
    }

    private fun funSignUp() {
        val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
        startActivity(intent)
    }
}