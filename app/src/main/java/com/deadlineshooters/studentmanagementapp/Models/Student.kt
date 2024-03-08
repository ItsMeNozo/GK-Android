package com.deadlineshooters.studentmanagementapp.Models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.UUID

class Student : RealmObject{
    @PrimaryKey
    var id: ObjectId  = ObjectId()
    var firstName: String = ""
    var lastName: String = ""
    var className: String = ""
    var birthday: String = ""
    var gender: String = ""
}