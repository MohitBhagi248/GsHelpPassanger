package com.example.gshelppassanger

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class Payment : AppCompatActivity() {
    private lateinit var tv_card_number : TextView
    private lateinit var et_card_number : EditText
    private lateinit var tv_expiry : TextView
    private lateinit var et_expiry : EditText
    private lateinit var btn_payment : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        getSupportActionBar()?.setTitle("Payment")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        tv_card_number = findViewById(R.id.tv_card_number)
        et_card_number = findViewById(R.id.et_card_number)
        tv_expiry = findViewById(R.id.tv_expiry)
        et_expiry = findViewById(R.id.ed_expiry)
        btn_payment = findViewById(R.id.btn_payment)
    }
}