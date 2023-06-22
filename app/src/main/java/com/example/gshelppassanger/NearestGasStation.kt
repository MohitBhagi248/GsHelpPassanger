package com.example.gshelppassanger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.gshelppassanger.databinding.ActivityNearestGasStationBinding

class NearestGasStation : AppCompatActivity() {
    private lateinit var binding: ActivityNearestGasStationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearestGasStationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.setTitle("Find the best gas prices")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        binding.btnSearch.setOnClickListener {
            postalCode()
        }
    }

    fun postalCodeValid(postalCode: String): Boolean {
        val pattern = Regex("[A-Z][0-9][A-Z][0-9][A-Z][0-9]")
        return pattern.matches(postalCode);
    }

    private fun postalCode() {
        val etSearch = binding.etSearch.text.toString();
        if (etSearch.isNotEmpty()) {
            if(etSearch.length==6){
                val isValid = postalCodeValid(etSearch)
                if (isValid) {
                    val intent = Intent(this@NearestGasStation, Payment::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Invalid postal code!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Postal code length must be of 6 characters.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter a postal code!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(this@NearestGasStation, Profile::class.java)
                startActivity(intent)
                true
            }
            R.id.payment -> {
                val intent = Intent(this@NearestGasStation, Payment::class.java)
                startActivity(intent)
                true
            }
            R.id.priceAlerts -> {
                val intent = Intent(this@NearestGasStation, PriceAlerts::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}