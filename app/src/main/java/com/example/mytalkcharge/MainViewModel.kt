package com.example.mytalkcharge

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
        viewModelScope.launch {
            try {
                val res=retrofitInstance.getWeatherInfo(state,apikey)
                if(res.isSuccessful){
                    currData.value=res.body()
                    res.body()?.id?.let {
                        getWeather(it) }
                }
                else{

                }
            }catch (ex:Exception){

            }
        }
    }

    fun getWeather(id:Int) {
        viewModelScope.launch {
            try {
                val res=retrofitInstance.getWeatherInfo(id,apikey)
                if(res.isSuccessful){
                    res.body()?.let {
                        hourlyList.value=it.list
                        responseData.value= it
                    }

                }
                else{

                }
            }catch (ex:Exception){

            }
        }

    }

}