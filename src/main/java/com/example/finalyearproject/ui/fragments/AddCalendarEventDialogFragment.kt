package com.example.finalyearproject.ui.fragments

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.finalyearproject.R
import com.example.finalyearproject.data.CalendarEvent
import com.example.finalyearproject.ui.CalendarEventViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.dialog_fragment_add_calendar_event.*
import java.util.*

@Suppress("NAME_SHADOWING", "UNUSED_ANONYMOUS_PARAMETER")
class AddCalendarEventDialogFragment : DialogFragment() {
    private lateinit var viewModel: CalendarEventViewModel
    private var dateString: String = ""
    private var email: String? = null
    private var workoutPreference : String? = null
    private var workoutPreferenceModifier : String = ""
    private var workoutPreferenceInt : Int = 2
    fun newInstance(): AddCalendarEventDialogFragment? {
        return AddCalendarEventDialogFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CalendarEventViewModel::class.java)
        return inflater.inflate(R.layout.dialog_fragment_add_calendar_event, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {
                getString(R.string.calendar_event_added)
            } else {
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            dismiss()
        })
        var user = FirebaseAuth.getInstance().currentUser
        user?.let {
            for (profile in it.providerData) {
                email = profile.email
            }
        }

        val rootRef = FirebaseFirestore.getInstance()
        val subjectsRef = rootRef.collection("workout").orderBy("preference", Query.Direction.DESCENDING).whereEqualTo("user", email)
        val datePicker: DatePicker = (view!!.findViewById(R.id.datePicker))
        val workoutSpinner: Spinner = (view!!.findViewById(R.id.workoutSpinner))
        val colorSpinner: Spinner = (view!!.findViewById(R.id.colorSpinner))
        val workouts: ArrayList<String> = ArrayList<String>()
        val colors: Array<String> = resources.getStringArray(R.array.colors)

        val workoutSpinnerAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, workouts)
        workoutSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val colorSpinnerAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, colors)
        colorSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        workoutSpinner.adapter = workoutSpinnerAdapter
        colorSpinner.adapter = colorSpinnerAdapter

        subjectsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val workout = document.getString("workout")
                    workouts.add(workout!!)
                }
                workoutSpinnerAdapter.notifyDataSetChanged()
            }
        }



        val today = Calendar.getInstance()
        datePicker.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            val msg = "You Selected: $day/$month/$year"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
        button_add.setOnClickListener {
            val id: Int = radio_group_preference.checkedRadioButtonId
            var radio: RadioButton = view!!.findViewById(id)
            if (id != -1) { // If any radio button checked from radio group
                // Get the instance of radio button using id
                radio = view!!.findViewById(id)
            }else {
                // If no radio button checked in this radio group
                Toast.makeText(
                    context, "On button click :" +
                            " nothing selected",
                    Toast.LENGTH_SHORT
                ).show()
            }


            val startDay: Int = datePicker.dayOfMonth
            val startMonth: Int = datePicker.month + 1
            val startYear: Int = datePicker.year
            dateString = "$startDay-$startMonth-$startYear"
            val workout: String = workoutSpinner.selectedItem.toString()
            val color: String = colorSpinner.selectedItem.toString()
            val endDateMonth = datePicker.month + 1
            val endDateDay = datePicker.dayOfMonth + 1
            val endDateYear = datePicker.year
            val endDateString = "$endDateDay-$endDateMonth-$endDateYear"
            subjectsRef.whereEqualTo("workout", workout).limit(1).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val workoutPreference = document.getString("preference")
                            workoutPreferenceInt = workoutPreference?.toInt()!!
                        }
                        workoutPreference = workoutPreferenceInt.toString()

                        workoutPreferenceModifier = radio.text.toString()

                        if (workoutPreferenceModifier == "Great") {
                            val workoutPreferenceVal = workoutPreferenceInt + 1
                            workoutPreference = workoutPreferenceVal.toString()
                            subjectsRef.whereEqualTo("workout", workout).limit(1).get().addOnCompleteListener{ task ->
                                if (task.isSuccessful) {
                                    for (document in task.result!!) {
                                        val map:HashMap<Any, String> = HashMap()
                                        map["preference"] = workoutPreference!!
                                        rootRef.collection("workout").document(document.id).set(map, SetOptions.merge())
                                    }
                                }
                            }
                        } else if (workoutPreferenceModifier == "Bad") {
                            workoutPreferenceInt -= 1
                            workoutPreference = workoutPreferenceInt.toString()
                            subjectsRef.whereEqualTo("workout", workout).limit(1).get().addOnCompleteListener{ task ->
                                if (task.isSuccessful) {
                                    for (document in task.result!!) {
                                        val map:HashMap<Any, String> = HashMap()
                                        map["preference"] = workoutPreference!!
                                        rootRef.collection("workout").document(document.id).set(map, SetOptions.merge())
                                    }
                                }
                            }
                        }
                    }
                }



            user = FirebaseAuth.getInstance().currentUser
            user?.let {
                for (profile in it.providerData) {
                    email = profile.email
                }
            }
            val calendarEvent = CalendarEvent()
            calendarEvent.id = viewModel.getRandomString(10)
            calendarEvent.startDate = dateString
            calendarEvent.endDate = endDateString
            calendarEvent.color = color
            calendarEvent.workout = workout
            calendarEvent.user = email

            viewModel.addCalendarEvent(calendarEvent)
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        /* val fragment =
            fragmentManager!!.findFragmentById(R.id.calendar_fragment)
        val ft =
            activity!!.supportFragmentManager.beginTransaction()
        ft.remove(fragment!!)
        ft.commit()*/
        val ftran: FragmentTransaction? = activity!!.supportFragmentManager.beginTransaction()
        ftran?.detach(this)?.remove(this)?.commit()
    }

    override fun onDismiss(dialogInterface: DialogInterface) {
super.onDismiss(dialogInterface)
        val ftran: FragmentTransaction? = activity!!.supportFragmentManager.beginTransaction()
        ftran?.detach(this)?.remove(this)?.commit()
        val ftran2: FragmentTransaction? = this.parentFragmentManager.beginTransaction()
        ftran2?.detach(this)?.remove(this)?.commit()
    }

   /* fun onRadioButtonClicked(view: View){
        // Get the clicked radio button instance
        val radio: RadioButton = getView()!!.findViewById(radio_group_preference.checkedRadioButtonId)
        Toast.makeText(context,"On click : ${radio.text}",
            Toast.LENGTH_SHORT).show()
    }*/
}





