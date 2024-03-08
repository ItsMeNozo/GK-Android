package com.deadlineshooters.studentmanagementapp

import android.util.Log
import com.deadlineshooters.studentmanagementapp.Models.Student
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

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
            Log.d("Realm path", instance!!.realm.configuration.path)

            return instance!!
        }
    }

    fun readAll(): RealmResults<Student> {
        return realm.query<Student>().find()
    }

    fun writeStudent(){
        val student = Student("John", "Doe", "21KTPM1", "01/01/2000","Male")

        realm.writeBlocking {
            copyToRealm(student)
        }
    }
}