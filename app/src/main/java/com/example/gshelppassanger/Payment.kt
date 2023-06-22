package com.example.gshelppassanger

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.gshelppassanger.databinding.ActivityPaymentBinding
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class Payment : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.setTitle("Payment")
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        Checkout.preload(applicationContext)
        binding.btnPayment.setOnClickListener {
            startPayment()
        }
    }

    private fun startPayment() {
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name","GsHelpPassanger")
            options.put("description","Pay Now")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("amount","50000")//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email","")
            prefill.put("contact","")

            options.put("prefill",prefill)
            co.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this ,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        Toast.makeText(this ,"Payment Successful $razorpayPaymentId",Toast.LENGTH_LONG).show()
        binding.btnPayment.visibility = View.GONE
        binding.cardView.visibility = View.GONE
        binding.success.visibility = View.VISIBLE
    }

    override fun onPaymentError(code: Int, description: String?) {
        Toast.makeText(this ,"Error $code",Toast.LENGTH_LONG).show()
        binding.failed.visibility = View.VISIBLE
    }
}