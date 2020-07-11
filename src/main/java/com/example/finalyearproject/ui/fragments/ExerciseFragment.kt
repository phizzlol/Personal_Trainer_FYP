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
import com.example.finalyearproject.data.Exercise
import com.example.finalyearproject.data.NODE_EXERCISES
import com.example.finalyearproject.ui.ExerciseAdapter
import com.example.finalyearproject.ui.ExerciseViewModel
import com.example.finalyearproject.ui.RecyclerViewClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_exercise.*


@Suppress("UNUSED_ANONYMOUS_PARAMETER", "DEPRECATION")
class ExerciseFragment : Fragment(), RecyclerViewClickListener {
    private lateinit var viewModel: ExerciseViewModel
    private val adapter = ExerciseAdapter()
    private val dbExercises = FirebaseFirestore.getInstance().collection(NODE_EXERCISES)
    private val currentUser = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(currentUser?.isEmailVerified!!){
            button_add.visibility = View.VISIBLE
            text_view_authenticate_email.visibility = View.GONE
        } else{
            button_add.visibility = View.GONE
            text_view_authenticate_email.visibility = View.VISIBLE
        }

        adapter.listener = this
        recycler_view_exercises.adapter = adapter

        viewModel.fetchExercises()

        viewModel.exercises.observe(viewLifecycleOwner, Observer{
            adapter.setExercise(it)
        })

        viewModel.exercise.observe(viewLifecycleOwner, Observer {
            adapter.addExercise(it)
        })

        button_add.setOnClickListener{
            AddExerciseDialogFragment()
                .show(childFragmentManager, "")}

        swipeRefreshExercise.setOnRefreshListener {
            val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
            ft.detach(this).attach(this).commit()                  // refresh your list contents somehow
            swipeRefreshExercise.isRefreshing =
                false   // reset the SwipeRefreshLayout (stop the loading spinner)
        }


    }

    override fun onRecyclerViewItemClicked(view: View, exercise: Exercise) {
        when(view.id){
            R.id.button_delete ->{
                AlertDialog.Builder(requireContext()).also{
                    it.setTitle(getString(R.string.delete_confirmation))
                    it.setPositiveButton(getString(R.string.yes)){dialog, which ->
                        dbExercises.whereEqualTo("exercise", exercise.exercise)
                            .get().addOnSuccessListener { documents ->
                                for (document in documents){
                                    dbExercises.document(document.id).delete()
                                }
                            }
                    }
                }.create().show()
            }
        }
    }
}