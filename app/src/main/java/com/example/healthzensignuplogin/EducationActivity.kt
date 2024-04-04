package com.example.healthzensignuplogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class EducationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education)

        // Set up the Retrofit service
        val retrofit = Retrofit.Builder()
            .baseUrl("https://clinicaltables.nlm.nih.gov/api/conditions/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DiseaseApiService::class.java)

        // Set up the AutoCompleteTextView
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedDisease = autoCompleteTextView.adapter.getItem(position) as String
            // Handle the selected disease
        }

        autoCompleteTextView.addTextChangedListener { editable ->
            val condition = editable.toString().trim()
            if (condition.isNotEmpty()) {
                // Make API request to search for diseases based on the condition
                service.searchDiseases(condition).enqueue(object : Callback<List<String>> {
                    override fun onResponse(
                        call: Call<List<String>>,
                        response: Response<List<String>>
                    ) {
                        if (response.isSuccessful) {
                            val diseases = response.body()
                            if (diseases != null) {
                                val adapter = ArrayAdapter<String>(
                                    this@EducationActivity,
                                    android.R.layout.simple_dropdown_item_1line,
                                    diseases
                                )
                                autoCompleteTextView.setAdapter(adapter)
                                autoCompleteTextView.showDropDown()
                            }
                        } else {
                            // Handle unsuccessful response
                        }
                    }

                    override fun onFailure(call: Call<List<String>>, t: Throwable) {
                        // Handle failure
                    }
                })
            }
        }
    }

    // Define a Retrofit interface for making HTTP requests
    interface DiseaseApiService {
        @GET("api/conditions/v3/search")
        fun searchDiseases(
            @Query("terms") condition: String
        ): Call<List<String>> // Change List<String> to match the actual response structure
    }
}
