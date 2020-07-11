package com.example.finalyearproject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalyearproject.data.Exercise
import com.example.finalyearproject.data.NODE_EXERCISES
import com.google.firebase.firestore.FirebaseFirestore

class ExerciseViewModel : ViewModel() {

    private val dbExercises = FirebaseFirestore.getInstance().collection(NODE_EXERCISES)


    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>>
        get() = _exercises

    private val _exercise = MutableLiveData<Exercise>()
    val exercise: LiveData<Exercise>
        get() = _exercise

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    fun addExercise(exercise: Exercise) {
        dbExercises.add(exercise).addOnSuccessListener {
            _result.value = null
        }.addOnFailureListener{
            _result.value = it
        }
    }

    fun fetchExercises() {

        dbExercises.get().addOnSuccessListener {snapshot ->
            val exercises = mutableListOf<Exercise>()
            for( exerciseSnapshot in snapshot){
                exercises.add(exerciseSnapshot.toObject(Exercise::class.java))
            }
            _exercises.value = exercises
        }.addOnFailureListener{


        }
    }

}