package com.iacovelli.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class MainViewModel(
    private val repository: Repository
) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val fact = MutableLiveData<String>()

    fun onViewReady() {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            loading.value = true
            val result = repository.fetchCatFact()
            fact.value = result.text
            loading.value = false
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val retrofit: Retrofit) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val service = retrofit.create(APIService::class.java)
            return MainViewModel(RepositoryImpl(service)) as T
        }

    }
}