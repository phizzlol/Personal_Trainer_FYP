package com.example.finalyearproject.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalyearproject.R
import com.example.finalyearproject.data.Workout
import kotlinx.android.synthetic.main.recycler_view_exercises.view.button_delete
import kotlinx.android.synthetic.main.recycler_view_exercises.view.button_edit
import kotlinx.android.synthetic.main.recycler_view_workouts.view.*


class WorkoutAdapter : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewModel>() {

    private var workouts = mutableListOf<Workout>()
    var listener: RecyclerViewClickListenerWorkout? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WorkoutViewModel(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_workouts, parent, false)
    )

    override fun getItemCount() = workouts.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WorkoutViewModel, position: Int) {
        if (workouts[position].workout != null) {
            holder.view.text_view_workout.text = workouts[position].workout
            holder.view.text_view_exercises.text =

                    //exercise 1

                if (workouts[position].exercise1 != null && workouts[position].exercise1 != " None" && workouts[position].exercise1 != "" && workouts[position].exercise1 != "None") {
                    "${workouts[position].exercise1} | Reps : ${workouts[position].reps1} | Sets : ${workouts[position].sets1} \n"
                } else {
                    ""
                } +
                        //exercise2
                        if (workouts[position].exercise2 != null && workouts[position].exercise2 != " None" && workouts[position].exercise2 != "" && workouts[position].exercise2 != "None" ) {
                            "${workouts[position].exercise2} | Reps : ${workouts[position].reps2} | Sets : ${workouts[position].sets2} \n"
                        } else {
                            ""
                        } +
                        //exercise 3
                        if (workouts[position].exercise3 != null && workouts[position].exercise3 != " None" && workouts[position].exercise3 != "" && workouts[position].exercise3 != "None") {
                            "${workouts[position].exercise3} | Reps : ${workouts[position].reps3} | Sets : ${workouts[position].sets3} \n"
                        } else {
                            ""
                        } +
                        //exercise 4
                        if (workouts[position].exercise4 != null && workouts[position].exercise4 != " None" && workouts[position].exercise4 != "" && workouts[position].exercise4 != "None") {
                            "${workouts[position].exercise4} | Reps : ${workouts[position].reps4} | Sets : ${workouts[position].sets4} \n"
                        } else {
                            ""
                        } +
                        //exercise 5
                        if (workouts[position].exercise5 != null && workouts[position].exercise5 != " None" && workouts[position].exercise5 != "" && workouts[position].exercise5 != "None") {
                            "${workouts[position].exercise5} | Reps : ${workouts[position].reps5} | Sets : ${workouts[position].sets5} \n"
                        } else {
                            ""
                        } +
                        //exercise 6

                         if (workouts[position].exercise6 != null && workouts[position].exercise6 != " None" && workouts[position].exercise6 != "" && workouts[position].exercise6 != "None") {
                         "${workouts[position].exercise6} | Reps : ${workouts[position].reps6} | Sets : ${workouts[position].sets6} \n"
                         } else {
                             ""
                             }

        }

        holder.view.button_edit.setOnClickListener {
            listener?.onRecyclerViewItemClicked(it, workouts[position])
        }
        holder.view.button_delete.setOnClickListener {
            listener?.onRecyclerViewItemClicked(it, workouts[position])
        }
    }

    fun setWorkout(workouts: List<Workout>) {
        this.workouts = workouts as MutableList<Workout>
        notifyDataSetChanged()
    }

    fun addWorkout(workout: Workout) {
        if (!workouts.contains(workout)) {
            workouts.add(workout)
        } else {
            val index = workouts.indexOf(workout)
            if (workout.isDeleted) {
                workouts.removeAt(index)
            } else {
                workouts[index] = workout
            }
        }
        notifyDataSetChanged()
    }

    class WorkoutViewModel(val view: View) : RecyclerView.ViewHolder(view)
}