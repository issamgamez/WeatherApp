package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherActivity : AppCompatActivity() {

    private lateinit var editTextCityName: EditText
    private lateinit var searchBtn: TextView
    private lateinit var responseCityImage: ImageView
    private lateinit var responseCityName: TextView
    private lateinit var responseCityTemperature: TextView
    private lateinit var SpinnerLangue: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        // Initialize views
        editTextCityName = findViewById(R.id.cityName)
        searchBtn = findViewById(R.id.search)
        responseCityImage = findViewById(R.id.ResponseCityImage)
        responseCityName = findViewById(R.id.ResponseCityname)
        responseCityTemperature = findViewById(R.id.ResponseCityTemperature)
        SpinnerLangue = findViewById(R.id.SpinnerLangue)
        var countries = arrayOf("French","English","Arabic")
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,countries)
        SpinnerLangue.adapter = adapter
        SpinnerLangue.setOnItemSelectedListener( object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(applicationContext,countries[position],Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        })


        // Set up button click listener
        searchBtn.setOnClickListener {
            val city = editTextCityName.text.toString()
            if (city.isNotEmpty()) {
                fetchWeatherData(city)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }



    }

    private fun fetchWeatherData(city: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherService = retrofit.create(WeatherService::class.java)

        val apiKey = "e219c008845485f330e0f909b44212f3"

        val result = weatherService.getWeatherByCity(city, apiKey)
        result.enqueue(object : Callback<JsonObject> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val main = result?.get("main")?.asJsonObject
                    val temp = main?.get("temp")?.asDouble
                    responseCityTemperature.text = "$temp °C"

                    val cityName = result?.get("name")?.asString
                    responseCityName.text = "$cityName"

                    val weather = result?.get("weather")?.asJsonArray
                    val icon = weather?.get(0)?.asJsonObject?.get("icon")?.asString
                    Picasso.get().load("https://openweathermap.org/img/w/$icon.png").into(responseCityImage)
                    responseCityImage.visibility = View.VISIBLE
                } else {
                    Toast.makeText(applicationContext, "City not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
