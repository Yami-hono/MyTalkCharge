package com.example.mytalkcharge

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mytalkcharge.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var hourlyUpdateAdapter:HourlyWeatherListAdapter
    var state=""


    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hourlyUpdateAdapter= HourlyWeatherListAdapter()

        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.hourlyList.adapter=hourlyUpdateAdapter

        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()

        binding.swipeLayout.setOnRefreshListener {
            viewModel.getWeather(state)
        }




        binding.hourlyCard.alpha=0.5f
        binding.dailyCard.alpha=0.5f
        binding.details.alpha=0.5f
        addObserver()
        trackRecordRoomState()
    }

    fun checkLocationPermission(){

        val task:Task<Location> = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            !=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)
            !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            val geocoder = Geocoder(this, Locale.getDefault())
            Log.i("locationService", "checkLocationPermission: $it")
            val addresses: List<Address> = geocoder.getFromLocation(it.latitude, it.longitude, 1)
            Log.i("locationService", "onCreate: $addresses")
            val address: String = addresses[0].getAddressLine(0)
            val city: String = addresses[0].getLocality()
             state = addresses[0].getAdminArea()
            val zip: String = addresses[0].getPostalCode()
            Log.i("locationService", "checkLocationPermission: $city -- $state")
            viewModel.getWeather(state)
            val country: String = addresses[0].getCountryName()

        }
    }

    fun addObserver(){
        viewModel.hourlyList.observe(this){
            if(it.isNotEmpty()){
                hourlyUpdateAdapter.setUpdatedList(it)
            }
        }

        viewModel.currData.observe(this){
            Log.i("currdataUPdate", "addObserver: currDAta UPdate")
            binding.swipeLayout.isRefreshing = false
            binding.currTemp.text= (it.main.temp-273).toString()+"°C"
        }

        viewModel.responseData.observe(this){
            binding.apply {
//                town.text = it.city.name
                sunRise.text=SimpleDateFormat("hh:mm a").format(it.city.sunrise*1000).toString()
                sunSet.text=SimpleDateFormat("hh:mm a").format(it.city.sunset*1000).toString()
                Log.i("epochTotime", "addObserver: ${it.city.sunset}")
                windTxt.text = it.list[0].wind.speed.toString()+"km/h"
                visibilityTxt.text=it.list[0].visibility.toString()+ "km"
                pressureText.text=it.list[0].main.pressure.toString()+"hPa"
            }
        }
    }

    private fun trackRecordRoomState() {
//        binding.motionView.addTransitionListener(object : MotionLayout.TransitionListener{
//            override fun onTransitionStarted(
//                motionLayout: MotionLayout?,
//                startId: Int,
//                endId: Int
//            ) {
//            }
//
//            override fun onTransitionChange(
//                motionLayout: MotionLayout?,
//                startId: Int,
//                endId: Int,
//                progress: Float
//            ) {
//            }
//
//            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
//                if (currentId == R.id.end){
//                    Log.i("motionRoomState", "onTransitionCompleted: end")
////                    viewModel.lvRoomState.value = LiveRoomState.COLLAPSED
//                } else {
//                    Log.i("motionRoomState", "onTransitionCompleted: start ")
////                    viewModel.lvRoomState.value = LiveRoomState.EXPANDED
//                }
//            }
//
//            override fun onTransitionTrigger(
//                motionLayout: MotionLayout?,
//                triggerId: Int,
//                positive: Boolean,
//                progress: Float
//            ) {
//            }
//
//        })
    }
}