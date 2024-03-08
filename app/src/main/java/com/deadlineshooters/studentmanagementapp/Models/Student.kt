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

    constructor(){
        
    }

    constructor(
        id: ObjectId,
        firstName: String,
        lastName: String,
        className: String,
        birthday: String,
        gender: String
    ) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.className = className
        this.birthday = birthday
        this.gender = gender
    }

    constructor(
        firstName: String,
        lastName: String,
        className: String,
        birthday: String,
        gender: String
    ) {
        this.id = ObjectId()
        this.firstName = firstName
        this.lastName = lastName
        this.className = className
        this.birthday = birthday
        this.gender = gender
    }


}