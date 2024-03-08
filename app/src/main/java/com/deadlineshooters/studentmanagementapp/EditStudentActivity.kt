package com.deadlineshooters.studentmanagementapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.deadlineshooters.studentmanagementapp.Models.Student
import com.deadlineshooters.studentmanagementapp.databinding.ActivityEditStudentBinding
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Locale

class EditStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditStudentBinding.inflate(layoutInflater)

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

        // Retrieve the Student object from MainActivity
//        val student = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            intent.getParcelableExtra("student", Student::class.java)
//        } else {
//            intent.getParcelableExtra<Student>("student")
//        }
        val student = intent.getStringExtra("student")


//        student?.let {
//            binding.etFullname.setText("${it.firstName} ${it.lastName}")
//            binding.etBd.setText(it.birthday)
//
//            when (it.gender) {
//                "Male" -> binding.radioMale.isChecked = true
//                "Female" -> binding.radioFemale.isChecked = true
//                "Other" -> binding.radioOther.isChecked = true
//            }
//        }

//        binding.btnSave.setOnClickListener {
//            // Perform validity check for birthday format
//            if (isValidDateFormat(binding.etBd.text.toString())) {
//                // Split the full name into first name and last name
//                val fullNameParts = binding.etFullname.text.toString().split(" ", limit = 2)
//                val firstName = fullNameParts.getOrElse(0) { "" }
//                val lastName = fullNameParts.getOrElse(1) { "" }
//
//                // Check if both first name and last name are provided
//                if (firstName.isNotBlank() && lastName.isNotBlank()) {
//                    // Check if all fields are filled
//                    if (isAllFieldsFilled()) {
//                        // Create a new Student object with the updated data
//                        val birthday = binding.etBd.text.toString()
//                        val className = binding.classButton.selectedItem.toString()
//                        val gender = when {
//                            binding.radioMale.isChecked -> "Male"
//                            binding.radioFemale.isChecked -> "Female"
//                            else -> "Other"
//                        }
//
//                        student?.let {
//                            val updatedStudent = Student(
//                                id = student.id,
//                                firstName = firstName,
//                                lastName = lastName,
//                                className = className,
//                                birthday = birthday,
//                                gender = gender
//                            )
//                            updateStudentData(updatedStudent)
//                        }
//
//                        // Navigate back to MainActivity
//                        val intent = Intent(this@EditStudentActivity, MainActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//                        startActivity(intent)
//                        finish()
//                    } else {
//                        // Notify the user to fill all the fields
//                        Toast.makeText(
//                            this@EditStudentActivity,
//                            "Please fill all the fields",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    // Notify the user that both first name and last name are required
//                    Toast.makeText(
//                        this@EditStudentActivity,
//                        "Please provide both first name and last name",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            } else {
//                // Notify the user about the invalid birthday format
//                Toast.makeText(
//                    this@EditStudentActivity,
//                    "Invalid birthday. Please use dd/mm/yyyy",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }


//        binding.btnDelete.setOnClickListener {
//            student?.let { deleteStudent(it) }
//            // Navigate back to MainActivity
//            val intent = Intent(this@EditStudentActivity, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//            startActivity(intent)
//        }

/*        binding.classButton.setOnClickListener {
            val intent = Intent(this@EditStudentActivity, ChooseClassActivity::class.java)
            intent.putExtra("selectedClass", binding.classButton.text.toString())
            resultLauncher.launch(intent)
        }*/
    }

   /* var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                Log.d("Editstudent", "im calledddd ${result.resultCode} ")

                // There are no request codes
                val data: Intent? = result.data
                Log.d("Editstudent", "${data?.getStringExtra("selectedClass")}")
                binding.classButton.setText(data?.getStringExtra("selectedClass"))
            }
        }*/

    private fun isAllFieldsFilled(): Boolean {
        val fullName = binding.etFullname.text.toString().trim()
        val firstName = fullName.split(" ").getOrNull(0) ?: ""
        val lastName = fullName.split(" ").getOrNull(1) ?: ""

        return firstName.isNotBlank() && lastName.isNotBlank() &&
                binding.etBd.text.isNotBlank()  &&
                (binding.radioMale.isChecked || binding.radioFemale.isChecked || binding.radioOther.isChecked)
    }


    private fun updateStudentData(updatedStudent: Student) {
        Log.d("EditStudent", "Saving ${updatedStudent.className}")

        val file = File(getExternalFilesDir(null), "students_data.json")
        if (!file.exists()) {
            file.createNewFile()
        }

        val students = mutableListOf<Student>()
        file.forEachLine { line ->
            val student = Json.decodeFromString<Student>(line)
            if (student.id == updatedStudent.id) {
                students.add(updatedStudent)
            } else {
                students.add(student)
            }
        }

        file.printWriter().use { out ->
            for (student in students) {
                out.println(Json.encodeToString(student))
            }
        }
    }

    private fun deleteStudent(student: Student) {
        // Delete student from your data file
        val file = File(getExternalFilesDir(null), "students_data.json")
        if (file.exists()) {
            val students = mutableListOf<Student>()
            file.forEachLine { line ->
                val existingStudent = Json.decodeFromString<Student>(line)
                if (existingStudent.id != student.id) {
                    students.add(existingStudent)
                }
            }

            file.printWriter().use { out ->
                for (stu in students) {
                    out.println(Json.encodeToString(stu))
                }
            }
        }
    }


    // Function to check if the date is in dd/mm/yyyy format
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