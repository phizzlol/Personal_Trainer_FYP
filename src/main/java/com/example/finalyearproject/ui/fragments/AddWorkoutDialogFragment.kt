package com.example.finalyearproject.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalyearproject.R
import com.example.finalyearproject.R.layout.dialog_fragment_add_workout
import com.example.finalyearproject.data.Workout
import com.example.finalyearproject.ui.WorkoutViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.dialog_fragment_add_workout.*


class AddWorkoutDialogFragment : DialogFragment() {

    private lateinit var viewModel: WorkoutViewModel
private var email : String? = null
    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)
        return inflater.inflate(dialog_fragment_add_workout,container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if(it == null){
                getString(R.string.exercise_added)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            dismiss()
        })

        val rootRef = FirebaseFirestore.getInstance()
        val subjectsRef = rootRef.collection("exercise")
        val spinner1 : Spinner = (view!!.findViewById(R.id.spinner1))
        val spinner2 : Spinner = (view!!.findViewById(R.id.spinner2))
        val spinner3 : Spinner = (view!!.findViewById(R.id.spinner3))
        val spinner4 : Spinner = (view!!.findViewById(R.id.spinner4))
        val spinner5 : Spinner = (view!!.findViewById(R.id.spinner5))
        val spinner6 : Spinner = (view!!.findViewById(R.id.spinner6))

        val exercises : ArrayList<String> = ArrayList()
        val adapter : ArrayAdapter<String> = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, exercises)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter
        spinner2.adapter = adapter
        spinner3.adapter = adapter
        spinner4.adapter = adapter
        spinner5.adapter = adapter
        spinner6.adapter = adapter
        subjectsRef.orderBy("exercise").startAt(" None").get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                for (document in task.result!!){
                    val exercise = document.getString("exercise")
                    exercises.add(exercise!!)
                }
                adapter.notifyDataSetChanged()
            }
        }




        button_add.setOnClickListener {
            val workout = Workout()
            val workoutName : String
            if(edit_text_workout.text?.isEmpty()!!) {
                input_layout_workout.error = getString(R.string.error_field_required)
                Toast.makeText(requireContext(), "Workout Name is Required", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else{
                workoutName = edit_text_workout.text.toString().trim()
            }
            if (edit_text_reps1.text?.isNotEmpty()!! && edit_text_reps1.text.toString().trim().toIntOrNull() != null) {
                    val reps1: Int = edit_text_reps1.text.toString().trim().toIntOrNull()!!
                    workout.reps1 = reps1
            } else {
                val reps1 = 0
                workout.reps1 = reps1
            }
            if (edit_text_sets1.text?.isNotEmpty()!!) {
                if (edit_text_sets1.text.toString().trim().toIntOrNull() != null) {
                    val sets1: Int = edit_text_sets1.text.toString().trim().toIntOrNull()!!
                    workout.sets1 = sets1
                }
            } else {
                val sets1 = 0
                workout.sets1 = sets1
            }
                val exercise1: String = spinner1.selectedItem.toString().trim()

                if (edit_text_reps2.text?.isNotEmpty()!! && edit_text_reps2.text.toString().trim().toIntOrNull() != null) {
                    val reps2: Int = edit_text_reps2.text.toString().trim().toIntOrNull()!!
                    workout.reps2 = reps2
                } else {
                    val reps2 = 0
                    workout.reps2 = reps2
                }
                if (edit_text_sets2.text?.isNotEmpty()!! && edit_text_sets2.text.toString().trim().toIntOrNull() != null) {
                    val sets2: Int = edit_text_sets2.text.toString().trim().toIntOrNull()!!
                    workout.sets2 = sets2
                } else {
                    val sets2 = 0
                    workout.sets2 = sets2
                }
                val exercise2: String = spinner2.selectedItem.toString().trim()
                if (edit_text_reps3.text?.isNotEmpty()!! && edit_text_reps3.text.toString().trim().toIntOrNull() != null) {
                    val reps3: Int = edit_text_reps3.text.toString().trim().toIntOrNull()!!
                    workout.reps3 = reps3
                } else {
                    val reps3 = 0
                    workout.reps3 = reps3
                }
                if (edit_text_sets3.text?.isNotEmpty()!! && edit_text_sets3.text.toString().trim().toIntOrNull() != null) {
                    val sets3: Int = edit_text_sets3.text.toString().trim().toIntOrNull()!!
                    workout.sets3 = sets3
                } else {
                    val sets3 = 0
                    workout.sets3 = sets3
                }
                val exercise3: String = spinner3.selectedItem.toString().trim()
                if (edit_text_reps4.text?.isNotEmpty()!! && edit_text_reps4.text.toString().trim().toIntOrNull() != null) {
                    val reps4: Int = edit_text_reps4.text.toString().trim().toIntOrNull()!!
                    workout.reps4 = reps4
                } else {
                    val reps4 = 0
                    workout.reps4 = reps4
                }
                if (edit_text_sets4.text?.isNotEmpty()!! && edit_text_sets4.text.toString().trim().toIntOrNull() != null) {
                    val sets4: Int = edit_text_sets4.text.toString().trim().toIntOrNull()!!
                    workout.sets4 = sets4
                } else {
                    val sets4 = 0
                    workout.sets4 = sets4
                }
                val exercise4: String = spinner4.selectedItem.toString().trim()
                if (edit_text_reps5.text?.isNotEmpty()!! && edit_text_reps5.text.toString().trim().toIntOrNull() != null) {
                    val reps5: Int = edit_text_reps5.text.toString().trim().toIntOrNull()!!
                    workout.reps5 = reps5
                } else {
                    val reps5 = 0
                    workout.reps5 = reps5
                }
                if (edit_text_sets5.text?.isNotEmpty()!! && edit_text_sets5.text.toString().trim().toIntOrNull() != null) {
                    val sets5: Int = edit_text_sets5.text.toString().trim().toIntOrNull()!!
                    workout.sets5 = sets5
                } else {
                    val sets5 = 0
                    workout.sets5 = sets5
                }
                val exercise6: String = spinner6.selectedItem.toString().trim()
            if (edit_text_reps6.text?.isNotEmpty()!! && edit_text_reps6.text.toString().trim().toIntOrNull() != null) {
                val reps6: Int = edit_text_reps6.text.toString().trim().toIntOrNull()!!
                workout.reps6 = reps6
            } else {
                val reps6 = 0
                workout.reps6 = reps6
            }
            if (edit_text_sets6.text?.isNotEmpty()!! && edit_text_sets6.text.toString().trim().toIntOrNull() != null) {
                val sets6: Int = edit_text_sets6.text.toString().trim().toIntOrNull()!!
                workout.sets6 = sets6
            } else {
                val sets6 = 0
                workout.sets6 = sets6
            }
            val exercise5: String = spinner5.selectedItem.toString().trim()
                viewModel.fetchFilteredExercises(exercise1)


                val user = FirebaseAuth.getInstance().currentUser
                user?.let {
                    for (profile in it.providerData) {
                        email = profile.email
                    }
                }
                workout.exercise1 = exercise1
                workout.user = email
                workout.workout = workoutName
                workout.exercise2 = exercise2
                workout.exercise3 = exercise3
                workout.exercise4 = exercise4
                workout.exercise5 = exercise5
                workout.exercise6 = exercise6



                viewModel.addWorkout(workout)
            }
        }

        }

