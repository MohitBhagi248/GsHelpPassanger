package com.example.gshelppassanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.gshelppassanger.databinding.ActivityPromotionsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PromotionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPromotionsBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
        binding = ActivityPromotionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.setTitle("Promotions")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        databaseReference = FirebaseDatabase.getInstance().getReference("Promotions")
        auth = FirebaseAuth.getInstance()

        retrievePriceAlerts()

//        binding.btnSave.setOnClickListener {
//            promotions()
//        }
    }

    private fun retrievePriceAlerts() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        if (userId != null) {
            databaseReference.child(userId).get().addOnSuccessListener {
                if (it.exists()) {
                    val esso = it.child("esso").value
                    val petroCanada = it.child("petroCanada").value
                    val shell = it.child("shell").value
                    val husky = it.child("husky").value
                    Toast.makeText(this, "Data read successfully", Toast.LENGTH_SHORT).show()
                    binding.etEsso.text = esso.toString()
                    binding.petroCanada.text = petroCanada.toString()
                    binding.shell.text = shell.toString()
                    binding.husky.text = husky.toString()
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User not exist", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun promotions(){
//        val uid = auth.uid.toString()
//        val esso = binding.etEsso.text.toString()
//        val petroCanada = binding.petroCanada.text.toString()
//        val shell = binding.shell.text.toString()
//        val husky = binding.husky.text.toString()
//
//        if(uid.isNotEmpty() && esso.isNotEmpty() && petroCanada.isNotEmpty() && shell.isNotEmpty() && husky.isNotEmpty()){
//            val promotions = PromotionsModel(uid, esso, petroCanada, shell, husky)
//            databaseReference.child(uid).setValue(promotions).addOnSuccessListener {
//                Toast.makeText(this,"Data added successfully", Toast.LENGTH_SHORT).show()
//                binding.etEsso.text?.clear()
//                binding.petroCanada.text?.clear()
//                binding.shell.text?.clear()
//                binding.husky.text?.clear()
//            }
//        } else {
//            Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
//        }
//    }
}
