package com.example.mytalkcharge

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mytalkcharge.databinding.LiHourlyUpdateBinding
import com.example.mytalkcharge.repository.Sky
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class HourlyWeatherListAdapter: RecyclerView.Adapter<HourlyWeatherListAdapter.UserViewHolder>(){
    var data=ArrayList<Sky>()
    fun setUpdatedList(data:ArrayList<Sky>){
        this.data= data
        notifyDataSetChanged()
    }

    inner class UserViewHolder(var binding: LiHourlyUpdateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Sky) {
            with(binding) {
                time.text= SimpleDateFormat("ha").format(item.dt*1000).toString()
                val tempC=item.main.temp-273
                if(item.weather[0].description.contains("clouds"))
                    skyStatus.setBackgroundResource(R.drawable.ic_cloud)
                else
                    skyStatus.setBackgroundResource(R.drawable.ic_sunny)
                Log.i("adapterHourly", "bind: tempC:$tempC &&-   ${item.main.temp}")
                temp.text=(tempC).toInt().toString()+"°C"
            }
        }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = DataBindingUtil.inflate<LiHourlyUpdateBinding>(
            LayoutInflater.from(parent.context),
            R.layout.li_hourly_update,
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
