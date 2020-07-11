package com.example.finalyearproject.ui

import android.view.View
import com.example.finalyearproject.data.Workout

interface RecyclerViewClickListenerWorkout{
    fun onRecyclerViewItemClicked(view: View, workout: Workout)

}

