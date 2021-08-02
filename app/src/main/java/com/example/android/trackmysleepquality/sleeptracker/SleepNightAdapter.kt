package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import com.example.android.trackmysleepquality.sleeptracker.SleepNightAdapter.SleepViewHolder.Companion.from

/**
 * Adapter class is extending  ListAdapter class instead of Adapter class because,
 * ListAdapter, that helps you build a RecyclerView adapter that's backed by a list.
 * ListAdapter keeps track of the list for you and notifies the adapter when the list is updated.
 *
 * It needs a DiffCallback() class as a parameter to the constructor, to check the difference between
 * two lists, i.e Old and New.
 *
 * There is no need of getItemCount() method anymore, because the ListAdapter implements this method for you.
 */
class SleepNightAdapter : ListAdapter<SleepNight,SleepNightAdapter.SleepViewHolder>(SleepNightDiffCallBack()) {

    /**
     * ViewHolder class should contain all the codes that deal with layout or views.
     */
    class SleepViewHolder private constructor(binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {
        val sleepLength: TextView = binding.sleepLength
        val quality: TextView = binding.qualityString
        val qualityImage: ImageView = binding.qualityImage

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
                /**
                 * Binding variable for reference of views in item layout.
                 */
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return SleepViewHolder(binding)
            }
        }
    }

    /**
     *  DiffUtil is used for calculating the differences between two lists.
     *  DiffUtil takes an old list and a new list and figures out what's different
     */
    class SleepNightDiffCallBack: DiffUtil.ItemCallback<SleepNight>() {
        override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            return oldItem.nightId == newItem.nightId
        }

        override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepViewHolder {
        /**
         * This from function is inside viewHolder class as it deals with views.
         */
        return from(parent)
    }

    override fun onBindViewHolder(holder: SleepViewHolder, position: Int) {
        /**
         * It will give you the item.
         */
        val item = getItem(position)

        /**
         * This bind function is inside viewHolder class as it deals with views.
         */
        holder.bind(item)
    }

}