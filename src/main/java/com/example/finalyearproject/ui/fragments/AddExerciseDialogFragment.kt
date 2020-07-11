package com.example.finalyearproject.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalyearproject.R
import com.example.finalyearproject.R.layout.dialog_fragment_add_exercise
import com.example.finalyearproject.data.Exercise
import com.example.finalyearproject.ui.ExerciseViewModel
import kotlinx.android.synthetic.main.dialog_fragment_add_exercise.*


class AddExerciseDialogFragment : DialogFragment() {

    private lateinit var viewModel: ExerciseViewModel

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)
        return inflater.inflate(dialog_fragment_add_exercise,container, false)
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

        button_add.setOnClickListener {
            val name = edit_text_exercise.text.toString().trim()
            var difficultyText = 0
            val muscleGroup: String = edit_text_muscle_group.text.toString().trim()
            if(edit_text_difficulty.text.toString().isNotEmpty() && edit_text_difficulty.text.toString().toIntOrNull() != null) {
                difficultyText = edit_text_difficulty.text.toString().toIntOrNull()!!
            }else{edit_text_difficulty.error = getString(R.string.field_required_must_be_number)}
            if(name.isEmpty()){
                input_layout_exercise.error = getString(R.string.error_field_required)
                return@setOnClickListener
            }else{

            val exercise = Exercise()
            exercise.exercise = name
            exercise.difficulty = difficultyText
            exercise.muscle_group = muscleGroup

            viewModel.addExercise(exercise)
        }
        }
    }
}