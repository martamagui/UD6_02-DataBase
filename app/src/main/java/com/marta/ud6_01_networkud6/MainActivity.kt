package com.marta.ud6_01_networkud6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.marta.ud6_01_networkud6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}