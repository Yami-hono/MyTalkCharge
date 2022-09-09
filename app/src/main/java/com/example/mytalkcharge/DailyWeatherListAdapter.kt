package com.example.mytalkcharge

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mytalkcharge.databinding.LiDailyUpdateBinding
import com.example.mytalkcharge.repository.Sky
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class DailyWeatherListAdapter: RecyclerView.Adapter<DailyWeatherListAdapter.UserViewHolder>(){
    var data=ArrayList<Sky>()
    fun setUpdatedList(data:ArrayList<Sky>){
        var dailyList=ArrayList<Sky>()
        Log.i("dailyLsit", "setUpdatedList: sourceList size ${data.size}")
        var dayVal:Long=0
        for(i in data){
            if(i.dt/100000!=dayVal){
                Log.i("dailyLsit", "setUpdatedList: $dayVal")
                dailyList.add(i)
                dayVal=i.dt/100000
            }
        }

        Log.i("dailyLsit", "setUpdatedList: ${dailyList.size}")
        this.data=data
        this.data= dailyList
        notifyDataSetChanged()
    }

    inner class UserViewHolder(var binding: LiDailyUpdateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Sky) {
            with(binding) {
//                time.text= SimpleDateFormat("d/m").format(item.dt_txt.substring(0,5))
                time.text=item.dt_txt.substring(5,10)
                val tempC=item.main.temp-273
                if(item.weather[0].description.contains("clouds"))
                    skyStatus.setBackgroundResource(R.drawable.ic_cloud)
                else
                    skyStatus.setBackgroundResource(R.drawable.ic_sunny)
                temp.text=(tempC).toInt().toString()+"°C"
            }
        }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = DataBindingUtil.inflate<LiDailyUpdateBinding>(
            LayoutInflater.from(parent.context),
            R.layout.li_daily_update,
            parent,
            false
        )
        return UserViewHolder(view)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val searchResult = data.get(position)
        searchResult.let {
            holder.bind(searchResult)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
