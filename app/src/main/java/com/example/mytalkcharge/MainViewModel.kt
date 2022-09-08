package com.example.mytalkcharge

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytalkcharge.network.NetworkService
import com.example.mytalkcharge.network.RetrofitInstance
import com.example.mytalkcharge.repository.Sky
import com.example.mytalkcharge.repository.WeatherResponse
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {

    val retrofitInstance= RetrofitInstance.getRetrofitInstance().create(NetworkService::class.java)
    val apikey="c3528ccdd115c63801af22bc093f916c"

    val hourlyList= MutableLiveData<ArrayList<Sky>>()
    var responseData=MutableLiveData<WeatherResponse>()
    var currData=MutableLiveData<TownWeather>()


    fun getWeather(state: String) {
        Log.i("weatherapi", "getWeather: $state ")
        viewModelScope.launch {
            try {
                val res=retrofitInstance.getWeatherInfo(state,"c3528ccdd115c63801af22bc093f916c")
                if(res.isSuccessful){
                    currData.value=res.body()
                    res.body()?.id?.let {
                        getWeather(it) }
                    Log.i("weatherapi", "getWeather: success ${res.body()}")
                }
                else
                    Log.i("weatherapi", "getWeather: failed ${res.code()} --- ${res.message()}")
            }catch (ex:Exception){
                Log.i("weatherapi", "getWeather: exception: ${ex.message}")

            }
        }
    }

    fun getWeather(id:Int) {
        Log.i("weatherapi", "getWeather: $id ")
        viewModelScope.launch {
            try {
                val res=retrofitInstance.getWeatherInfo(id,"c3528ccdd115c63801af22bc093f916c")
                if(res.isSuccessful){
                    hourlyList.value=res.body()?.list
                    responseData.value= res.body()!!
                    Log.i("weatherapi", "getWeather: success ${res.body()}")
                }
                else
                    Log.i("weatherapi", "getWeather: failed ${res.code()} --- ${res.message()}")
            }catch (ex:Exception){
                Log.i("weatherapi", "getWeather: exception: ${ex.message}")

            }
        }

    }

}