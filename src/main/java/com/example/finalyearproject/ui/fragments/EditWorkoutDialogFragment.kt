package com.example.finalyearproject.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalyearproject.R
import com.example.finalyearproject.data.NODE_WORKOUTS
import com.example.finalyearproject.data.Workout
import com.example.finalyearproject.ui.WorkoutViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.dialog_fragment_add_exercise.*
import kotlinx.android.synthetic.main.dialog_fragment_add_exercise.button_add
import kotlinx.android.synthetic.main.dialog_fragment_edit_workout.*

class EditWorkoutDialogFragment(private var workout: Workout) : DialogFragment() {

    private lateinit var viewModel: WorkoutViewModel
    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>>
        get() = _workouts
    var email: String? = null
    private var previousWorkoutName: String? = null
    private val dbWorkouts = FirebaseFirestore.getInstance().collection(NODE_WORKOUTS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)
        return inflater.inflate(R.layout.dialog_fragment_edit_workout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            for (profile in it.providerData) {
                email = profile.email
            }
        }
        dbWorkouts.whereEqualTo("workout", workout.workout).whereEqualTo(
            "user",
            email
        ).get().addOnSuccessListener { snapshot ->
            var workouts: Workout? = null
            for (workoutSnapshot in snapshot) {
                workouts = (workoutSnapshot.toObject(Workout::class.java))
            }
            workout = workouts!!
        }


            edit_text_name.setText(workout.workout)
            edit_text_exercise1.setText(workout.exercise1)
            edit_text_reps1.setText(workout.reps1.toString())
            edit_text_sets1.setText(workout.sets1.toString())
            edit_text_exercise2.setText(workout.exercise2)
            edit_text_reps2.setText(workout.reps2.toString())
            edit_text_sets2.setText(workout.sets2.toString())
            edit_text_exercise3.setText(workout.exercise3)
            edit_text_reps3.setText(workout.reps3.toString())
            edit_text_sets3.setText(workout.sets3.toString())
            edit_text_exercise4.setText(workout.exercise4)
            edit_text_reps4.setText(workout.reps4.toString())
            edit_text_sets4.setText(workout.sets4.toString())
            edit_text_exercise5.setText(workout.exercise5)
            edit_text_reps5.setText(workout.reps5.toString())
            edit_text_sets5.setText(workout.sets5.toString())
            edit_text_exercise6.setText(workout.exercise6)
            edit_text_reps6.setText(workout.reps6.toString())
            edit_text_sets6.setText(workout.sets6.toString())
            previousWorkoutName = workout.workout

            viewModel.result.observe(viewLifecycleOwner, Observer {
                val message = if (it == null) {
                    getString(R.string.workout_added)
                } else {
                    getString(R.string.error, it.message)
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                dismiss()
            })

            button_add.setOnClickListener {
                val name = edit_text_name.text.toString().trim()
                //exercise 1
                val exercise1 = edit_text_exercise1.text.toString().trim()
                if (edit_text_reps1.text!!.isNotEmpty() && edit_text_reps1.text.toString().trim().toIntOrNull() != null){
                val reps1 = edit_text_reps1.text.toString().trim().toInt()
                workout.reps1 = reps1}else{
                    workout.reps1 = 0
                }
                if (edit_text_sets1.text!!.isNotEmpty() && edit_text_sets1.text.toString().trim().toIntOrNull() != null){
                    val sets1 = edit_text_sets1.text.toString().trim().toInt()
                    workout.sets1 = sets1}else{
                    workout.sets1 = 0
                }
                //exercise 2
                val exercise2 = edit_text_exercise2.text.toString().trim()
                if (edit_text_reps2.text!!.isNotEmpty() && edit_text_reps2.text.toString().trim().toIntOrNull() != null){
                    val reps2 = edit_text_reps2.text.toString().trim().toInt()
                    workout.reps2 = reps2}else{
                workout.reps2 = 0
            }
                if (edit_text_sets2.text!!.isNotEmpty() && edit_text_sets2.text.toString().trim().toIntOrNull() != null){
                    val sets2 = edit_text_sets2.text.toString().trim().toInt()
                    workout.sets2 = sets2}else{
                    workout.sets2 = 0
                }
                //exercise 3
                val exercise3 = edit_text_exercise3.text.toString().trim()
                if (edit_text_reps3.text!!.isNotEmpty() && edit_text_reps3.text.toString().trim().toIntOrNull() != null){
                    val reps3 = edit_text_reps3.text.toString().trim().toInt()
                    workout.reps3 = reps3}else{
                    workout.reps3 = 0
                }
                if (edit_text_sets3.text!!.isNotEmpty() && edit_text_sets3.text.toString().trim().toIntOrNull() != null){
                    val sets3 = edit_text_sets3.text.toString().trim().toInt()
                    workout.sets3 = sets3}else{
                    workout.sets3 = 0
                }
                val exercise4 = edit_text_exercise4.text.toString().trim()
                if (edit_text_reps4.text!!.isNotEmpty() && edit_text_reps4.text.toString().trim().toIntOrNull() != null){
                    val reps4 = edit_text_reps4.text.toString().trim().toInt()
                    workout.reps4 = reps4}else{
                    workout.reps4 = 0
                }
                if (edit_text_sets4.text!!.isNotEmpty() && edit_text_sets4.text.toString().trim().toIntOrNull() != null){
                    val sets4 = edit_text_sets4.text.toString().trim().toInt()
                    workout.sets4 = sets4}else{
                    workout.sets4 = 0
                }

                val exercise5 = edit_text_exercise5.text.toString().trim()
                if (edit_text_reps5.text!!.isNotEmpty() && edit_text_reps5.text.toString().trim().toIntOrNull() != null){
                    val reps5 = edit_text_reps5.text.toString().trim().toInt()
                    workout.reps5 = reps5}else{
                    workout.reps5 = 0
                }
                if (edit_text_sets4.text!!.isNotEmpty() && edit_text_sets4.text.toString().trim().toIntOrNull() != null){
                    val sets5 = edit_text_sets5.text.toString().trim().toInt()
                    workout.sets5 = sets5}else{
                    workout.sets5 = 0
                }
                val exercise6 = edit_text_exercise6.text.toString().trim()
                if (edit_text_reps6.text!!.isNotEmpty() && edit_text_reps6.text.toString().trim().toIntOrNull() != null){
                    val reps6 = edit_text_reps6.text.toString().trim().toInt()
                    workout.reps6 = reps6}else{
                    workout.reps6 = 0
                }
                if (edit_text_sets6.text!!.isNotEmpty() && edit_text_sets6.text.toString().trim().toIntOrNull() != null){
                    val sets6 = edit_text_sets6.text.toString().trim().toInt()
                    workout.sets6 = sets6}else{
                    workout.sets6 = 0
                }
                if (name.isEmpty()) {
                    input_layout_exercise.error = getString(R.string.error_field_required)
                    return@setOnClickListener
                }
                workout.workout = name
                workout.exercise1 = exercise1

                workout.exercise2 = exercise2

                workout.exercise3 = exercise3

                workout.exercise4 = exercise4

                workout.exercise5 = exercise5

                workout.exercise5 = exercise5

                workout.exercise6 = exercise6


                dbWorkouts.whereEqualTo("workout", previousWorkoutName).whereEqualTo(
                    "user",
                    email
                ).get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        dbWorkouts.document(document.id).set(workout)
                    }
                }

                dismiss()
            }



    }
}