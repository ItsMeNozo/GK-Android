package com.deadlineshooters.studentmanagementapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deadlineshooters.studentmanagementapp.Models.Student
import com.deadlineshooters.studentmanagementapp.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    private var students = arrayListOf<Student>()

    //    var studentListView: ListView? = null
    private var studentRV: RecyclerView? = null
    private  var density : Float = 0f
    private lateinit var adapter: StudentListAdapter
    private lateinit var adapterAutoCompleteTV: ArrayAdapter<String>
    private var addButton: ImageView? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        density = resources.displayMetrics.density

//        studentListView = findViewById(R.id.lv_student)
        studentRV = findViewById(R.id.rv_students)

        createDummyData()
//        clearData()
        readData()

        adapter = StudentListAdapter(this, students)
        studentRV!!.adapter = adapter

        studentRV!!.layoutManager = LinearLayoutManager(this)

        adapter.listener = object : StudentListAdapter.OnItemClickListener {
            override fun onItemClick(student: Student) {
                val intent = Intent(this@MainActivity, EditStudentActivity::class.java)
                intent.putExtra("student", student.id.toString())
                startActivity(intent)
            }
        }

        // Retrieve the Student object from EditStudentActivity
        val student = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("student", Student::class.java)
        } else {
            intent.getParcelableExtra<Student>("student")
        }

        addButton = findViewById(R.id.vector_button)
        addButton?.setOnClickListener {
            val intent = Intent(this@MainActivity, CreateStudentActivity::class.java)
            startActivity(intent)
        }



        var isListClicked = true
        var isGridClicked = false

        binding.buttonList.setOnClickListener {
            isListClicked = !isListClicked

            // Display the selected version
            binding.buttonList.setImageResource(R.drawable.baseline_list_check)
            val paddingInDpSelected = 5
            val paddingInPixelsSelected = (paddingInDpSelected * density).toInt()

            binding.buttonList.setPadding(paddingInPixelsSelected, paddingInPixelsSelected, paddingInPixelsSelected, paddingInPixelsSelected)

            // Deselect the other button
            isGridClicked = false
            binding.buttonGrid.setImageResource(R.drawable.grid_01)
             //
            val paddingInDp = 10
            val paddingInPixels = (paddingInDp * density).toInt()

            binding.buttonGrid.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels)

            // change layout
            studentRV!!.layoutManager = LinearLayoutManager(this)

        }

        binding.buttonGrid.setOnClickListener {
            isGridClicked = !isGridClicked

            // Display the selected version
            binding.buttonGrid.setImageResource(R.drawable.baseline_grid_check)

            val paddingInDpSelected = 6
            val paddingInPixelsSelected = (paddingInDpSelected * density).toInt()

            binding.buttonGrid.setPadding(paddingInPixelsSelected, paddingInPixelsSelected, paddingInPixelsSelected, paddingInPixelsSelected)


            // Deselect the other button
            isListClicked = false
            binding.buttonList.setImageResource(R.drawable.baseline_list_24)

            val paddingInDp = 11
            val paddingInPixels = (paddingInDp * density).toInt()

            binding.buttonList.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels)

            // change layout
            studentRV!!.layoutManager = GridLayoutManager(this, 2)

        }

        /** Set up auto text view*/
        var adapterAutoCompleteTV = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, students.map {  it.firstName + " " + it.lastName })
        binding.autoTVStudent.threshold = 1
        binding.autoTVStudent.setAdapter(adapterAutoCompleteTV)



        binding.autoTVStudent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No action needed here
            }

            override fun afterTextChanged(s: Editable) {
                val filteredStudents = students.filter { student ->
                    (student.firstName + " " + student.lastName).contains(s.toString(), ignoreCase = true)
                }

                adapter.updateStudents(filteredStudents)
                updateAutoCompleteTextView(filteredStudents)
            }
        })


    }

    fun updateAutoCompleteTextView(newStudents: List<Student>) {
        adapterAutoCompleteTV = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, newStudents.map {  it.firstName + " " + it.lastName })
        binding.autoTVStudent.setAdapter(adapterAutoCompleteTV)
    }

    override fun onResume() {
        super.onResume()

        // Check if data has changed before refreshing
        val previousSize = students.size
        students.clear()
        readData()
        adapter.notifyDataSetChanged()

        // Update the data in the ArrayAdapter and the AutoCompleteTextView
        updateAutoCompleteTextView(students)

        binding.autoTVStudent.dismissDropDown()
        binding.autoTVStudent.setText("")
    }

    /*private fun createDummyData() {

        val file = File(getExternalFilesDir(null), "students_data.txt")
        if (!file.exists()) {
            file.createNewFile()
        }

        file.printWriter().use { out ->
            repeat(3) {
                val student = Student(
                    firstName = "Victoria",
                    lastName = "Niego",
                    className = "21KTPM1",
                    birthday = "01/01/2003",
                    gender = "Female"
                )
                out.println("${student.id},${student.firstName},${student.lastName},${student.className},${student.birthday},${student.gender}")
            }
        }
    }

    private fun readData() {
        val file = File(getExternalFilesDir(null), "students_data.txt")
        Log.d("MainActivity", "Directory of students_data.txt: ${file.absolutePath}")


        file.forEachLine { line ->
            val parts = line.split(",")
            if (parts.size >= 6) {
                val student = Student(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5])
                students.add(student)
            }
        }
    }

    private fun clearData() {
        val file = File(getExternalFilesDir(null), "students_data.txt")
        if (file.exists()) {
            file.writeText("")
        }
    }*/
    private fun createDummyData() {
        Log.d("MainActivity", "Im called again")

        val file = File(getExternalFilesDir(null), "students_data.json")
        if (!file.exists()) {
            file.createNewFile()
        }

        file.printWriter().use { out ->
            repeat(10) {
                val student = Student(
                    firstName = "Victoria",
                    lastName = "Niego",
                    className = "21KTPM1",
                    birthday = "01/01/2003",
                    gender = "Female"
                )

                out.println(Json.encodeToString(student))
            }
        }
    }

    private fun readData() {
        val file = File(getExternalFilesDir(null), "students_data.json")
        Log.d("MainActivity", "Directory of students_data.json: ${file.absolutePath}")

        file.forEachLine { line ->
            val student = Json.decodeFromString<Student>(line)
            students.add(student)
        }
    }

    private fun clearData() {
        val file = File(getExternalFilesDir(null), "students_data.json")
        if (file.exists()) {
            file.writeText("")
        }
    }
}
