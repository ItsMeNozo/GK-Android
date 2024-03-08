package com.deadlineshooters.studentmanagementapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.deadlineshooters.studentmanagementapp.databinding.ActivityCreateStudentBinding
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class CreateStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStudentBinding.inflate(layoutInflater)

        setContentView(binding.root)

        /** Bind adapter to class spinner */
        val classes = arrayListOf<String>()

        for (i in 1..10) {
            classes.add("21KTPM$i")
        }
        val spinner: Spinner = findViewById(R.id.classButton)

        ArrayAdapter(this, android.R.layout.simple_spinner_item, classes)
            .also { adapter ->
// Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
                )
// Apply the adapter to the spinner
                spinner.adapter = adapter
            }

        /** Using the data from the spinner */
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                // TODO: do nothing – needed by the interface
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem = spinner.getItemAtPosition(p2) as String
                Log.d("selectedItem", "Selected item: $selectedItem")
                binding.classButton.setSelection(p2)
            }
        }

        binding.btnSave.setOnClickListener {
            if (isValidDateFormat(binding.etBd.text.toString())) {
                // Date format is valid
                if (isAllFieldsFilled()) {
                    // All fields are filled
                    val firstName = binding.etFullname.text.toString().split(" ")[0]
                    val lastName = if (binding.etFullname.text.contains(" ")) binding.etFullname.text.toString().split(" ")[1] else ""
                    val birthday = binding.etBd.text.toString()
                    val className = binding.classButton.selectedItem.toString()
                    val gender = when {
                        binding.radioMale.isChecked -> "Male"
                        binding.radioFemale.isChecked -> "Female"
                        else -> "Other"
                    }

                    val newStudent = Student(firstName = firstName, lastName = lastName, className = className, birthday = birthday, gender = gender)
                    saveStudentData(newStudent)
                    Toast.makeText(this, "Student created successfully", Toast.LENGTH_SHORT).show()
                    Log.d("created", "$newStudent")

                    val intent = Intent(this@CreateStudentActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT

                    startActivity(intent)
                    finish()
                } else {
                    // Some fields are empty
                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Invalid date format
                Toast.makeText(this, "Invalid date format. Please enter a valid date.", Toast.LENGTH_SHORT).show()
            }

        }

    }


    private fun isAllFieldsFilled(): Boolean {
        val fullName = binding.etFullname.text.toString().trim()
        val firstName = fullName.split(" ").getOrNull(0) ?: ""
        val lastName = fullName.split(" ").getOrNull(1) ?: ""

        return firstName.isNotBlank() && lastName.isNotBlank() &&
                binding.etBd.text.isNotBlank() &&
                (binding.radioMale.isChecked || binding.radioFemale.isChecked || binding.radioOther.isChecked)
    }


    private fun saveStudentData(student: Student) {
        val file = File(getExternalFilesDir(null), "students_data.json")
        if (!file.exists()) {
            file.createNewFile()
        }

        file.appendText(Json.encodeToString(student) + "\n")
    }

    private fun isValidDateFormat(date: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false  // strict
        return try {
            dateFormat.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }
}