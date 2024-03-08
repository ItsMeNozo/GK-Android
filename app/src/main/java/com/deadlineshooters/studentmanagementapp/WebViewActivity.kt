package com.deadlineshooters.studentmanagementapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class WebViewActivity : AppCompatActivity() {
    private var student: Student? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val student: Student? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("student", Student::class.java)
        } else {
            intent.getParcelableExtra<Student>("student")
        }


        val webView: WebView = findViewById(R.id.webview)
        webView.settings.javaScriptEnabled = true


        val htmlData = """
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
        }

        h1 {
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        @media screen and (max-width: 600px) {
            table {
                font-size: 12px;
            }
        }
    </style>
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            document.getElementById("fullName").textContent = "${student?.fullName}";
            document.getElementById("birthday").textContent = "${student?.birthday}";
            document.getElementById("class").textContent = "${student?.className}";
            document.getElementById("gender").textContent = "${student?.gender}";
        });
    </script>
</head>
<body>
<h1>Student Information</h1>
<table>
    <tr>
        <th>Full Name</th>
        <td id="fullName"></td>
    </tr>
    <tr>
        <th>Birthday</th>
        <td id="birthday"></td>
    </tr>
    <tr>
        <th>Class</th>
        <td id="class"></td>
    </tr>
    <tr>
        <th>Gender</th>
        <td id="gender"></td>
    </tr>
</table>
</body>
</html>
"""
        webView.loadDataWithBaseURL(null, htmlData, "text/html", "UTF-8", null)
    }

}