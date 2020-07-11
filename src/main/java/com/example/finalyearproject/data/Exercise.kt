package com.example.finalyearproject.data

import com.google.firebase.firestore.Exclude
import kotlin.random.Random


data class Exercise(
    @get:Exclude
    var id: String? = null,
    var exercise: String? = null,
    var muscle_group: String? = null,
    var difficulty: Int = Random.nextInt(1, 10),
    @get:Exclude
    var isDeleted: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        return if (other is Exercise) {
            other.id == id
        } else false
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (exercise?.hashCode() ?: 0)
        return result
    }

}