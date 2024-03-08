package com.deadlineshooters.studentmanagementapp

import com.deadlineshooters.studentmanagementapp.Models.Student
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class DBHandler {
    private val REALM_NAME = "STUDENT_MANAGEMENT"
    var realm: Realm? = null
    fun openDB() {
        val configuration = RealmConfiguration.Builder(
            setOf(Student::class))
            .name(REALM_NAME).build()
        realm = Realm.open(configuration)
    }
}