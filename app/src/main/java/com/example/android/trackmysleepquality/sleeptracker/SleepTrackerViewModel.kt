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

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.*
import androidx.recyclerview.widget.DiffUtil
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.launch

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

        private val _navigateToSleepQuality = MutableLiveData<SleepNight>()

        val navigateToSleepQuality: LiveData<SleepNight> = _navigateToSleepQuality

        private var tonight = MutableLiveData<SleepNight?>()

        private val nights = database.getAllNights()
        val nightString = Transformations.map(nights) { nights->
                formatNights(nights, application.resources)
        }

        /**
         * This variable showSnackBarEvent is used for showing snack bar.
         */
        private var _showSnackbarEvent = MutableLiveData<Boolean>()
        var showSnackbarEvent: LiveData<Boolean> = _showSnackbarEvent

        init {
            initalizeTonight()
        }

        fun doneNavigating() {
                _navigateToSleepQuality.value = null
        }

        /**
         * Here coroutine is used as this function is doing dataBase operation [Long-running task]
         *
         * Originally [getTonightFromDatabase] function dose dataBase operation, but
         * [getTonightFromDatabase] function is called inside this function, that means
         * dataBase operation is done via this fundtion.
         */
        private fun initalizeTonight() {
                viewModelScope.launch {
                        tonight.value = getTonightFromDatabase()
                }
        }

        private suspend fun getTonightFromDatabase(): SleepNight? {
                var night = database.getTonight()
                if (night?.startTimeMilli != night?.endTimeMilli) {
                        night = null
                }
                return night
        }

        /**
         * Below 6 methods are used as click handlers for buttons in our app.
         */
        fun onStartTracking() {
                viewModelScope.launch {
                        val newNight = SleepNight()
                        insert(newNight)
                        tonight.value = getTonightFromDatabase()

                }
        }

        private suspend fun insert(night: SleepNight) {
                database.insert(night)
        }


        fun onStoptrackung() {
                viewModelScope.launch {
                        val oldNight = tonight.value ?: return@launch
                        oldNight.endTimeMilli = System.currentTimeMillis()

                        _navigateToSleepQuality.value = oldNight

                        update(oldNight)
                }
        }

        private suspend fun update(night: SleepNight) {
                database.update(night)
        }

        fun onClear() {
                viewModelScope.launch {
                        clear()
                        tonight.value = null
                        _showSnackbarEvent.value = true
                }
        }

        private suspend fun clear() {
                database.clear()
        }
        /**
         * Click handler methods end here.
         */


        /**
         * Below three variables are used for setting visibility  of buttons.
         */
        val startButtonVisible = Transformations.map(tonight) {
                it == null
        }
        val stopButtonVisible = Transformations.map(tonight) {
                it != null
        }
        val clearButtonVisible = Transformations.map(nights) {
                it?.isNotEmpty()
        }
        /**
         * Visibility variables ends here.
         */

        /**
         * This doneShowingSnackbar function is used for snack bar's visibility.
         */
        fun doneShowingSnackbar() {
                _showSnackbarEvent.value = false
        }
}

