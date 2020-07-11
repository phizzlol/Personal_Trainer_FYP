package com.example.finalyearproject.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalyearproject.R
import com.example.finalyearproject.data.Exercise
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.recycler_view_exercises.view.*


class ExerciseAdapter : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewModel>() {

    private var exercises = mutableListOf<Exercise>()
    var listener: RecyclerViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ExerciseViewModel(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_exercises, parent, false)

    )

    override fun getItemCount() = exercises.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ExerciseViewModel, position: Int) {
        holder.view.text_view_exercise.text = exercises[position].exercise
        holder.view.text_view_muscle_group.text =
            "${exercises[position].muscle_group} | Difficulty : ${exercises[position].difficulty}"
        holder.view.button_edit.setOnClickListener {
            listener?.onRecyclerViewItemClicked(it, exercises[position])
        }
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser?.isEmailVerified!! && currentUser.email == "phil-webb1996@hotmail.co.uk"){
            holder.view.button_delete.visibility = View.VISIBLE
        }else{
            holder.view.button_delete.visibility = View.GONE
        }
        holder.view.button_delete.setOnClickListener {
            listener?.onRecyclerViewItemClicked(it, exercises[position])
        }

    }

    fun setExercise(exercises: List<Exercise>) {
        this.exercises = exercises as MutableList<Exercise>
        notifyDataSetChanged()
    }

    fun addExercise(exercise: Exercise) {
        if (!exercises.contains(exercise)) {
            exercises.add(exercise)
        } else {
            val index = exercises.indexOf(exercise)
            if (exercise.isDeleted) {
                exercises.removeAt(index)
            } else {
                exercises[index] = exercise
            }
        }
        notifyDataSetChanged()
    }

    class ExerciseViewModel(val view: View) : RecyclerView.ViewHolder(view)
}