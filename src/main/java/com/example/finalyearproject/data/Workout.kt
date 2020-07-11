package com.example.finalyearproject.data

import com.google.firebase.firestore.Exclude


data class Workout(
    @get:Exclude
    var id: String? = null,
    var user: String? = null,
    var workout: String? = null,
    var exercise1: String? = null,
    var reps1: Int = 0,
    var sets1:Int = 0,
   var exercise2: String? = null,
    var reps2: Int = 0,
    var sets2:Int = 0,
    var exercise3: String? = null,
    var reps3: Int = 0,
    var sets3:Int = 0,
    var exercise4: String? = null,
    var reps4: Int = 0,
    var sets4:Int = 0,
    var exercise5: String? = null,
    var reps5: Int = 0,
    var sets5:Int = 0,
   var exercise6: String? = null,
    var reps6: Int = 0,
    var sets6:Int = 0,
  /*  var exercise7: String? = null,
    var reps7: Int = 0,
    var sets7:Int = 0,
    var exercise8: String? = null,
    var reps8: Int = 0,
    var sets8:Int = 0,*/
    var preference : String = "5",
    @get:Exclude
    var isDeleted: Boolean = false
) {
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
