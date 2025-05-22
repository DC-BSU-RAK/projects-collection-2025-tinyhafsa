package com.example.postexpress

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NamesActivity : AppCompatActivity() {

    // declaring variables for inputs and button
    private lateinit var ReceiverName: EditText
    private lateinit var SenderName: EditText
    private lateinit var ContinueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_names)

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

        // attaching variables to elements in layout
        ReceiverName = findViewById(R.id.messageEntry)
        SenderName = findViewById(R.id.namesFromEntry)
        ContinueButton = findViewById(R.id.namesContinueButton)

        // continue button function
        ContinueButton.setOnClickListener {
            // variables for receiver and sender names
            val receiver = ReceiverName.text.toString().trim()
            val sender = SenderName.text.toString().trim()

            // if either field is empty, display toast message
            if (receiver.isEmpty() || sender.isEmpty()) {
                Toast.makeText(this, "Please enter both names", Toast.LENGTH_SHORT).show()
            } else { // if both fields are filled, save values in shared preferences
                val sharedPref = getSharedPreferences("PostPrefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("receiverName", receiver)
                    putString("senderName", sender)
                    apply()
                }

                // go to next page
                val intent = Intent(this, StampActivity::class.java)
                startActivity(intent)
            }
        }

    }
}