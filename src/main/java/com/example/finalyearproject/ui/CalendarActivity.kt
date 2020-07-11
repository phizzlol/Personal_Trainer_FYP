package com.example.finalyearproject.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chintanpatel.materialeventcalendar.EventItem
import com.example.finalyearproject.R
import com.example.finalyearproject.data.CalendarEvent
import com.example.finalyearproject.data.NODE_CALENDAR_EVENTS
import com.example.finalyearproject.ui.fragments.AddCalendarEventDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_calendar.*


@Suppress("unused")
class CalendarActivity : AppCompatActivity() {
    private var calendarEvents = mutableListOf<CalendarEvent>()
    private var email: String? = null
    private val eventList: ArrayList<EventItem> = arrayListOf()
    private val _calendarEvents = MutableLiveData<List<CalendarEvent>>()
    val events: LiveData<List<CalendarEvent>>
        get() = _calendarEvents

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        fun getItemCount() = calendarEvents.size
        //val ftran: FragmentTransaction? = this.parentFragmentManager.beginTransaction()
        //ftran?.detach(this)?.attach(this)?.commit()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            for (profile in it.providerData) {
                email = profile.email
            }
        }
        val dbWorkouts =
            FirebaseFirestore.getInstance().collection(NODE_CALENDAR_EVENTS)
                .whereEqualTo("user", email)

        dbWorkouts.get().addOnSuccessListener { snapshot ->
            val events = mutableListOf<CalendarEvent>()
            for (workoutSnapshot in snapshot) {
                events.add(workoutSnapshot.toObject(CalendarEvent::class.java))
            }
            _calendarEvents.value = events
            calendarEvents = (_calendarEvents.value as MutableList<CalendarEvent>)
            val length: Int = calendarEvents.size
            var lengthIndex: Int = length - 1
            while (lengthIndex >= 0) {
                eventList.add(
                    EventItem(
                        calendarEvents[lengthIndex].startDate.toString(),
                        calendarEvents[lengthIndex].endDate.toString(),
                        calendarEvents[lengthIndex].workout.toString(),
                        calendarEvents[lengthIndex].color.toString(),
                        calendarEvents[lengthIndex].id.toString()
                    )
                )
                lengthIndex--
                //println("THIS IS A TEST for calendar events" + calendarEvents[length].toString())
            }
            eventCalendar.addEventList(eventList)

        }.addOnFailureListener {


        }
        object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val events = mutableListOf<CalendarEvent>()
                    for (workoutSnapshot in snapshot.children) {
                        val event = workoutSnapshot.getValue(CalendarEvent::class.java)
                        event?.id = workoutSnapshot.key
                        event?.let { events.add(it) }
                    }
                }
            }
        }
        add_fab.setOnClickListener{
            val newFragment :DialogFragment = AddCalendarEventDialogFragment().newInstance()!!
                newFragment.show(supportFragmentManager,  "Adding Calendar Event")
            add_fab.visibility = View.GONE
        }
        refresh_fab.setOnClickListener{
            resetActivity()
            add_fab.visibility = View.VISIBLE
        }
    }

    private fun resetActivity(){
        finish()
        startActivity(intent)
    }
}












