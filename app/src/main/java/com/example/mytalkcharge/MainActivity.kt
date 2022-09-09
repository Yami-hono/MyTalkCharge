package com.example.mytalkcharge

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mytalkcharge.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
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
        alphaAnimation(binding.hourlyList)
        binding.swipeLayout.setOnChildScrollUpCallback { parent, child -> binding.scrollable.getScrollY() > 10 }

        addObserver()
        trackRecordRoomState()
    }

    fun alphaAnimation(view: View) {
        val animation = AlphaAnimation(0f, 1f)
        animation.duration = 850
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.repeatCount = 1
        animation.repeatMode = Animation.REVERSE
        view.startAnimation(animation)
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
            val addresses: List<Address> = geocoder.getFromLocation(it.latitude, it.longitude, 1)
//            val address: String = addresses[0].getAddressLine(0)
//            val city: String = addresses[0].getLocality()
             state = addresses[0].getAdminArea()
//            val zip: String = addresses[0].getPostalCode()
            viewModel.getWeather(state)
//            val country: String = addresses[0].getCountryName()

        }
    }

    private fun showToast(text:String){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show()
    }

    fun addObserver(){
        viewModel.hourlyList.observe(this){
            if(it.isNotEmpty()){
                hourlyUpdateAdapter.setUpdatedList(it)
            }
        }

        viewModel.apiResponseStatus.observe(this){
            when(it.what){
                 SUCCESS -> {
                     showToast("Refreshed")
                 }
                FAILED -> {
                    showToast("Failed! Try Again")
                }
                EXCEPTION -> {
                    showToast("Oops! Something Went Wrong")
                }
            }
        }

        viewModel.currData.observe(this){
            binding.apply {
                tempMax.text=String.format("%.2f", (it.main.temp_max-273))+"°C"
                tempMin.text=String.format("%.2f", (it.main.temp_min-273))+"°C"
                swipeLayout.isRefreshing = false
                currTemp.text=String.format("%.2f", (it.main.temp-273))
            }
        }

        viewModel.responseData.observe(this){
            binding.apply {
                town.text = it.city.name
                sunRise.text=SimpleDateFormat("hh:mm a").format(it.city.sunrise*1000).toString()
                sunSet.text=SimpleDateFormat("hh:mm a").format(it.city.sunset*1000).toString()
                weatherDesc.text=it.list[0].weather[0].main
                windTxt.text = it.list[0].wind.speed.toString()+"km/h"
                visibilityTxt.text=(it.list[0].visibility/1000).toString()+ "km"
                pressureText.text=it.list[0].main.pressure.toString()+"hPa"
            }
        }
    }

    private fun trackRecordRoomState() {
        binding.motionView1.addTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                //to be used for improvisation possible in future
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                //to be used for improvisation possible in future
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if (currentId == R.id.end){
                    //to be used for improvisation possible in future
                } else {
                    //to be used for improvisation possible in future
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
                //to be used for improvisation possible in future
            }

        })
    }
}