package com.marta.ud6_01_networkud6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.marta.ud6_01_networkud6.databinding.ActivityMainBinding
import com.marta.ud6_01_networkud6.provider.db.DataBaseRepository
import com.marta.ud6_01_networkud6.provider.db.entitties.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        lifecycleScope.launch(Dispatchers.IO){
            DataBaseRepository.getInstance(applicationContext).databaseDao().addUser(UserEntity(1,"Marta","Magui","pruebamail@gmail.eth","12354",true))
        }
        setContentView(binding.root)
    }
}