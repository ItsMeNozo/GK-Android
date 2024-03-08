package com.deadlineshooters.studentmanagementapp.Models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class Class : RealmObject {
    @PrimaryKey
    var id: ObjectId  = ObjectId()
    var className: String = ""
}