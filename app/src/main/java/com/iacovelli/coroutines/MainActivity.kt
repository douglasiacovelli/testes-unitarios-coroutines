package com.iacovelli.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = getRetrofitInstance()
        val vmFactory = MainViewModel.Factory(retrofit)
        viewModel = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)

        setupObservers()
        viewModel.onViewReady()
    }

    private fun setupObservers() {
        viewModel.fact.observe(this, Observer {
            findViewById<TextView>(R.id.factTextView).text = it
        })
    }

    private fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://cat-fact.herokuapp.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}
