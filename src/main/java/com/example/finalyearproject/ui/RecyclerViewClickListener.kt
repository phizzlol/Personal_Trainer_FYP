package com.example.finalyearproject.ui

import android.view.View
import com.example.finalyearproject.data.Exercise

interface RecyclerViewClickListener{
    fun onRecyclerViewItemClicked(view: View, exercise: Exercise)
}

