package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.sleeptracker.SleepNightAdapter.SleepViewHolder.Companion.from

class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.SleepViewHolder>() {

    /**
     * ViewHolder class should contain all the codes that deal with layout or views.
     */
    class SleepViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
        val quality: TextView = itemView.findViewById(R.id.quality_string)
        val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)

        /**
         * These functions are here because,
         * We should structure our code so that everything related to a view holder is only in the view holder.
         */
        fun bind (item: SleepNight) {
            val res = itemView.context.resources
            sleepLength.text = convertDurationToFormatted(
                item.startTimeMilli, item.endTimeMilli, res)
            quality.text = convertNumericQualityToString(
                item.sleepQuality, res)
            qualityImage.setImageResource(when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_launcher_sleep_tracker_background
            })
        }

        /**
         * This [from] function is declared as a companion object because,
         * it can be called on the ViewHolder class, not called on a ViewHolder instance.
         */
        companion object {
            fun from(parent: ViewGroup): SleepViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
                return SleepViewHolder(view)
            }
        }
    }

    /**
     * This is the data inside our Adapter.
     */
    var data = listOf<SleepNight>()
    set (value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepViewHolder {
        /**
         * This from function is inside viewHolder class as it deals with views.
         */
        return from(parent)
    }

    override fun onBindViewHolder(holder: SleepViewHolder, position: Int) {
        val item = data[position]

        /**
         * This bind function is inside viewHolder class as it deals with views.
         */
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}