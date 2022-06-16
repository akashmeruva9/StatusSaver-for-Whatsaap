package com.example.socialmediadownloadeapp

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediadownloadeapp.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replacefragment(BlankFragment())

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.allstatuses -> {
                    replacefragment(BlankFragment())
                }

                R.id.mydownloads -> {
                    replacefragment(BlankFragment2())
                }
            }

            return@setOnItemSelectedListener true
        }


    }

    private fun replacefragment(fragment : Fragment)
    {
        val fragmentmanager = supportFragmentManager
        val fragmentTransaction = fragmentmanager.beginTransaction()
        fragmentTransaction.replace( R.id.fragmentContainerView , fragment)
        fragmentTransaction.commit()
    }

}


