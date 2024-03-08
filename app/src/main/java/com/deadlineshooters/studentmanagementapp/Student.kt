package com.deadlineshooters.studentmanagementapp

import android.graphics.drawable.Drawable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Serializable
import java.util.UUID

@Parcelize @Serializable
data class Student(
    val id: String = UUID.randomUUID().toString(),
    val firstName: String,
    val lastName: String,
    val className: String,
    val birthday: String,
    val gender: String,
) : Parcelable

