package com.example.postexpress

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.os.Build
import android.view.WindowInsets
import android.view.WindowInsetsController

class MessageActivity : AppCompatActivity() {

    // declaring variables for spinners, text and buttons
    private lateinit var greetingSpinner: Spinner
    private lateinit var signoffSpinner: Spinner
    private lateinit var messageEditText: EditText
    private lateinit var previewButton: Button

    // declaring lists for greeting and sign off spinners
    private val greetings = listOf("Dear", "Greetings", "Dearest")
    private val signoffs = listOf("Sincerely", "With love", "Forever yours")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_message)

        // removes top and bottom system bars to show the app in full screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)

            val controller = window.insetsController
            controller?.hide(WindowInsets.Type.systemBars())
            controller?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // attaching variables to elements on layout page
        greetingSpinner = findViewById(R.id.greetingSpinner)
        signoffSpinner = findViewById(R.id.signoffSpinner)
        messageEditText = findViewById(R.id.messageEntry)
        previewButton = findViewById(R.id.messagePreviewButton)

        // attaching lists of options to spinners
        setupSpinner (greetingSpinner, greetings)
        setupSpinner (signoffSpinner, signoffs)

        // preview button
        previewButton.setOnClickListener {
            saveMessageAndProceed()
        }
    }

    // Function - setup spinners - using lists
    private fun setupSpinner (spinner: Spinner, items:List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    // Function - save message and going to preview page
    private fun saveMessageAndProceed() {
        // declaring message variables
        val greeting = greetingSpinner.selectedItem.toString()
        val message = messageEditText.text.toString().trim()
        val signoff = signoffSpinner.selectedItem.toString()

        // if the message field is empty, display toast message
        if (message.isEmpty()) {
            Toast.makeText(this, "Please write a message", Toast.LENGTH_SHORT).show()
            return
        }

        // store greeting, message and sign off values in shared preferences
        val sharedPref = getSharedPreferences("PostPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("greeting", greeting)
            putString("message", message)
            putString("sign-off", signoff)
            apply()
        }

        // go to next page
        val intent = Intent(this, PreviewActivity::class.java)
        startActivity(intent)
    }
}