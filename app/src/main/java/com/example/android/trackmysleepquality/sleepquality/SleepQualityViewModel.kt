/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.launch

class SleepQualityViewModel(private val sleepNightKey: Long = 0L, val database: SleepDatabaseDao) : ViewModel() {
    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker : LiveData<Boolean?> = _navigateToSleepTracker

    /**
     * Once the navigation is done, this function will set the value of
     * navigation variable to null.
     */
    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    /**
     * This function will set different numbers of different faces of quality fragment layout
     * to sleep quality column of entity SleepNight.kt
     */
    fun onSetSleepQuality(quality: Int) {
        viewModelScope.launch {
            val toNight = database.get(sleepNightKey) ?: return@launch
            toNight.sleepQuality = quality
            database.update(toNight)

            /**
             * Setting this state variable to true will alert the observer and trigger navigation.
             */
            _navigateToSleepTracker.value = true
        }
    }


}