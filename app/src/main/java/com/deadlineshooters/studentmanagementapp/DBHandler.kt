package com.deadlineshooters.studentmanagementapp

import com.deadlineshooters.studentmanagementapp.Models.Student
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class DBHandler private constructor() {
    private val REALM_NAME = "STUDENT_MANAGEMENT"
    private lateinit var realm: Realm
    private fun openDB() {
        val configuration = RealmConfiguration.Builder(
            setOf(Student::class))
            .name(REALM_NAME).build()
        realm = Realm.open(configuration)
    }

    init {
        openDB()
    }

    companion object {
        private var instance: DBHandler? = null
        fun getInstance(): DBHandler {
            if (instance == null) {
                instance = DBHandler()
            }
            return instance!!
        }
    }

    fun readAll(): List<Student> {
        return realm.query<Student>().find()
    }
}