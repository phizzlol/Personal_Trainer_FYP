package com.example.finalyearproject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalyearproject.data.CalendarEvent
import com.example.finalyearproject.data.NODE_CALENDAR_EVENTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@Suppress("unused", "PrivatePropertyName")
class CalendarEventViewModel : ViewModel() {
    private val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
    private val dbCalendarEvents = FirebaseFirestore.getInstance().collection(NODE_CALENDAR_EVENTS)
    var email : String? = null

    private val _calendarEvents = MutableLiveData<List<CalendarEvent>>()
    val calendarEvents: LiveData<List<CalendarEvent>>
        get() = _calendarEvents

    private val _calendarEvent = MutableLiveData<CalendarEvent>()
    val calendarEvent: LiveData<CalendarEvent>
        get() = _calendarEvent

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    fun addCalendarEvent(event: CalendarEvent) {
        dbCalendarEvents.add(event).addOnSuccessListener {
            _result.value = null
        }.addOnFailureListener{
            _result.value = it
        }
    }

  /*  fun fetchFilteredCalendarEvents(name: String?){
       val dbWorkouts=  FirebaseFirestore.getInstance().collection(NODE_CALENDAR_EVENTS).whereEqualTo("exercise", name)
        dbWorkouts.get().addOnSuccessListener { task ->
            exerciseExists = true

            }
        }*/


    fun fetchFilteredCalendarEvents() {

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            for (profile in it.providerData) {
                email = profile.email
            }
        }

        val dbWorkouts =
            FirebaseFirestore.getInstance().collection(NODE_CALENDAR_EVENTS).whereEqualTo("user", email)

        dbWorkouts.get().addOnSuccessListener { snapshot ->
            val calendarEvents = mutableListOf<CalendarEvent>()
            for (workoutSnapshot in snapshot) {
                calendarEvents.add(workoutSnapshot.toObject(CalendarEvent::class.java))
            }
            _calendarEvents.value = calendarEvents
        }.addOnFailureListener {


        }
        object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val calendarEvents = mutableListOf<CalendarEvent>()
                    for (workoutSnapshot in snapshot.children) {
                        val calendarEvent = workoutSnapshot.getValue(CalendarEvent::class.java)
                        calendarEvent?.id = workoutSnapshot.key
                        calendarEvent?.let { calendarEvents.add(it) }
                    }
                    _calendarEvents.value = calendarEvents
                }
            }
        }
    }
    fun getRandomString(sizeOfRandomString: Int): String{
        val random = Random()
        val stringBuilder = StringBuilder(sizeOfRandomString)
        for(i in 0 until sizeOfRandomString)
            stringBuilder.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
        return stringBuilder.toString()
    }
    fun fetchCalendarEvents() {

        dbCalendarEvents.get().addOnSuccessListener {snapshot ->
            val calendarEvents = mutableListOf<CalendarEvent>()
            for( workoutSnapshot in snapshot){
                calendarEvents.add(workoutSnapshot.toObject(CalendarEvent::class.java))
            }
            _calendarEvents.value = calendarEvents
        }.addOnFailureListener{


        }
    }

}
