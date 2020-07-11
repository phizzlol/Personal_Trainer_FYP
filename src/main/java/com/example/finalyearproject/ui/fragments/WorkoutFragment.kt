package com.example.finalyearproject.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalyearproject.R
import com.example.finalyearproject.data.NODE_WORKOUTS
import com.example.finalyearproject.data.Workout
import com.example.finalyearproject.ui.RecyclerViewClickListenerWorkout
import com.example.finalyearproject.ui.WorkoutAdapter
import com.example.finalyearproject.ui.WorkoutViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_exercise.button_add
import kotlinx.android.synthetic.main.fragment_workout.*


@Suppress("UNUSED_ANONYMOUS_PARAMETER", "DEPRECATION")
class WorkoutFragment : Fragment(), RecyclerViewClickListenerWorkout {
    private lateinit var viewModel: WorkoutViewModel
    private val adapter = WorkoutAdapter()
    private val dbWorkouts = FirebaseFirestore.getInstance().collection(NODE_WORKOUTS)
    private val authUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter.listener = this
        recycler_view_workouts.adapter = adapter

        viewModel.fetchFilteredWorkouts()

        viewModel.workouts.observe(viewLifecycleOwner, Observer {
            adapter.setWorkout(it)
        })

        viewModel.workout.observe(viewLifecycleOwner, Observer {
            adapter.addWorkout(it)
        })

        button_add.setOnClickListener {
            AddWorkoutDialogFragment()
                .show(childFragmentManager, "")
        }
        swipeRefresh.setOnRefreshListener {
            val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
            ft.detach(this).attach(this).commit()                  // refresh your list contents somehow
            swipeRefresh.isRefreshing =
                false   // reset the SwipeRefreshLayout (stop the loading spinner)
        }
    }

    override fun onRecyclerViewItemClicked(view: View, workout: Workout) {
        when(view.id){
            R.id.button_edit ->{
                val dialog = EditWorkoutDialogFragment(workout)
                dialog.show(childFragmentManager, "").also {
                    adapter.notifyDataSetChanged()
                }


            }

            R.id.button_delete ->{
                AlertDialog.Builder(requireContext()).also{
                    it.setTitle(getString(R.string.delete_confirmation))
                    it.setPositiveButton(getString(R.string.yes)){dialog, which ->
                        dbWorkouts.whereEqualTo("workout", workout.workout).whereEqualTo("user",
                            authUser?.email
                        )
                            .get().addOnSuccessListener { documents ->
                                for (document in documents){
                                    dbWorkouts.document(document.id).delete()
                                }
                            }
                    }
                }.create().show()
adapter.notifyDataSetChanged()
            }

        }
    }
}