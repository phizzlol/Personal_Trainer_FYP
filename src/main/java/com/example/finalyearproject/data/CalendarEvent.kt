package com.example.finalyearproject.data

import com.google.firebase.firestore.Exclude

data class CalendarEvent(
    var id :String? = null,
    var user: String? = null,
    var workout: String? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var color: String? = null,
    @get:Exclude
var isDeleted: Boolean = false

){
    override fun equals(other: Any?): Boolean {
        return if (other is Workout) {
            other.id == id
        } else false
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (workout?.hashCode() ?: 0)
        return result
    }
}
