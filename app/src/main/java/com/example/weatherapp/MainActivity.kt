package com.example.weatherapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentHostCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var SpinnerLangue: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val getstart = findViewById<TextView>(R.id.GetStartedTextView)
        getstart.setOnClickListener {
            Intent(this,WeatherActivity::class.java).also{
                startActivity(it)
            }
        }

        // spinner
        SpinnerLangue = findViewById(R.id.SpinnerLangue)
        val countries = arrayOf("English","French","Arabic")
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,countries)
        SpinnerLangue.adapter = adapter
        SpinnerLangue.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = when (countries[position]) {
                    "English" -> "en"
                    "French" -> "fr"
                    "Arabic" -> "ar"
                    else -> Locale.getDefault().language
                }
                val newLocale = Locale(selectedItem)
                val config = resources.configuration
                config.setLocale(newLocale)
                resources.updateConfiguration(config, resources.displayMetrics)
                if (Locale.getDefault().language != selectedItem) {
                    Locale.setDefault(newLocale)
                    recreate()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Do nothing
            }
        })

    }

}