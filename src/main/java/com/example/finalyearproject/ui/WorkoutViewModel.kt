package com.example.finalyearproject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalyearproject.data.NODE_EXERCISES
import com.example.finalyearproject.data.NODE_WORKOUTS
import com.example.finalyearproject.data.Workout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

@Suppress("PrivatePropertyName")
class WorkoutViewModel : ViewModel() {
    private val dbWorkouts = FirebaseFirestore.getInstance().collection(NODE_WORKOUTS)
    var email: String? = null
    var exerciseExists: Boolean = false

    private val _workouts = MutableLiveData<List<Workout>>()
    val workouts: LiveData<List<Workout>>
        get() = _workouts

    private val _workout = MutableLiveData<Workout>()
    val workout: LiveData<Workout>
        get() = _workout

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    fun addWorkout(workout: Workout) {
        dbWorkouts.add(workout).addOnSuccessListener {
            _result.value = null
        }.addOnFailureListener {
            _result.value = it
        }
    }

    fun fetchFilteredExercises(name: String?) {
        val dbWorkouts = FirebaseFirestore.getInstance().collection(NODE_EXERCISES)
            .whereEqualTo("exercise", name)
        dbWorkouts.get().addOnSuccessListener {
            exerciseExists = true

        }
    }


    fun fetchFilteredWorkouts() {

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            for (profile in it.providerData) {
                email = profile.email
            }
        }

        val dbWorkouts =
            FirebaseFirestore.getInstance().collection(NODE_WORKOUTS)
                .orderBy("preference", Query.Direction.DESCENDING).whereEqualTo("user", email)
        dbWorkouts.get().addOnSuccessListener { snapshot ->
            val workouts = mutableListOf<Workout>()
            for (workoutSnapshot in snapshot) {
                workouts.add(workoutSnapshot.toObject(Workout::class.java))
            }
            _workouts.value = workouts
        }.addOnFailureListener {


        }
        object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val workouts = mutableListOf<Workout>()
                    for (workoutSnapshot in snapshot.children) {
                        val workout = workoutSnapshot.getValue(Workout::class.java)
                        workout?.id = workoutSnapshot.key
                        workout?.let { workouts.add(it) }
                    }
                    _workouts.value = workouts
                }
            }
        }
    }

}
