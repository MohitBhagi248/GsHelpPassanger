package com.example.gshelppassanger

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gshelppassanger.databinding.FragmentRetrievePriceBinding

class RetrievePriceFragment : Fragment() {
    private var _binding: FragmentRetrievePriceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRetrievePriceBinding.inflate(inflater, container, false)
        val sharedPreferences = activity?.getSharedPreferences("Price", Context.MODE_PRIVATE)

        val yesterdayFragment = sharedPreferences?.getString("yesterday", "")
        val todayFragment = sharedPreferences?.getString("today", "")
        val tomorrowFragment = sharedPreferences?.getString("tomorrow", "")

        _binding!!.tvYesterdayPrice.text = yesterdayFragment
        _binding!!.tvTodayPrice.text = todayFragment
        _binding!!.tvTomorrowPrice.text = tomorrowFragment

        return binding.root
    }
}