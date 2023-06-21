package com.example.gshelppassanger

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gshelppassanger.databinding.ActivityPriceAlertsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PriceAlerts : AppCompatActivity() {
    private lateinit var binding: ActivityPriceAlertsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPriceAlertsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.setTitle("Price Alerts")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Gas Price")

        binding.btnSubmit.setOnClickListener {
            retrievePriceAlerts()
            val fragment = RetrievePriceFragment()
            val yesterday = binding.etYesterdayPrice.text.toString()
            val today = binding.etTodayPrice.text.toString()
            val tomorrow = binding.etTomorrowPrice.text.toString()

            val sharedPreferences = getSharedPreferences("Price", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply(){
                putString("yesterday", yesterday)
                putString("today", today)
                putString("tomorrow", tomorrow)
            }.apply()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentPriceAlerts,fragment)
            fragmentTransaction.commit()
            Toast.makeText(this, "Price added successfully", Toast.LENGTH_SHORT).show()
            binding.btnSubmit.isEnabled = false
        }
    }

    private fun retrievePriceAlerts() {
        val uid = auth.uid.toString()
        val yesterday = binding.etYesterdayPrice.text.toString()
        val today = binding.etTodayPrice.text.toString()
        val tomorrow = binding.etTomorrowPrice.text.toString()

        val price = PriceModel(uid, yesterday, today, tomorrow)
        if(uid.isNotEmpty() && yesterday.isNotEmpty() && today.isNotEmpty() && tomorrow.isNotEmpty()){
            databaseReference.child(uid).setValue(price).addOnSuccessListener {
            }
        } else {
            Toast.makeText(this, "Please enter all fiels", Toast.LENGTH_SHORT).show()
        }
    }
}