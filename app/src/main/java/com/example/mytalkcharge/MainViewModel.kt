package com.example.mytalkcharge

import android.os.Message
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytalkcharge.network.NetworkService
import com.example.mytalkcharge.network.RetrofitInstance
import com.example.mytalkcharge.repository.Sky
import com.example.mytalkcharge.repository.WeatherResponse
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {

    private val retrofitInstance= RetrofitInstance.getRetrofitInstance().create(NetworkService::class.java)
    val apikey="c3528ccdd115c63801af22bc093f916c"
    val apiResponseStatus=MutableLiveData<Message>()

    val hourlyList= MutableLiveData<ArrayList<Sky>>()
    val dailyList= MutableLiveData<ArrayList<Sky>>()
    var responseData=MutableLiveData<WeatherResponse>()
    var currData=MutableLiveData<TownWeather>()


    fun getWeather(state: String) {
        val msg=Message()
        viewModelScope.launch {
            try {
                val res=retrofitInstance.getWeatherInfo(state,apikey)
                if(res.isSuccessful){
                    msg.what= SUCCESS
                    apiResponseStatus.value= msg
                    currData.value=res.body()
                    res.body()?.id?.let {
                        getWeather(it) }
                }
                else{
                    msg.what= FAILED
                    apiResponseStatus.value= msg

                }
            }catch (ex:Exception){
                msg.what= EXCEPTION
                apiResponseStatus.value= msg

            }
        }
    }

    fun getWeather(id:Int) {
        val msg=Message()
        viewModelScope.launch {
            try {
                val res=retrofitInstance.getWeatherInfo(id,40,apikey)
                if(res.isSuccessful){
                    msg.what= SUCCESS
                    apiResponseStatus.value= msg
                    res.body()?.let {
                        hourlyList.value=it.list
                        dailyList.value=it.list
                        responseData.value= it
                    }

                }
                else{
                    msg.what= FAILED
                    apiResponseStatus.value= msg
                    hourlyList.value= arrayListOf()
                }
            }catch (ex:Exception){
                msg.what= EXCEPTION
                apiResponseStatus.value= msg
                hourlyList.value= arrayListOf()

            }
        }

    }

}