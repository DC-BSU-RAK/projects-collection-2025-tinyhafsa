package com.example.postexpress

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RecentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recent)

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

        // declaring variables for shared preferences
        val prefs = getSharedPreferences("PostPrefs", Context.MODE_PRIVATE)
        val postcardCount = prefs.getInt("postcardCount", 0)

        // declaring variable and attaching to layout element
        val counterTextView = findViewById<TextView>(R.id.postcardCounter)
        // updating text to match value stored in shared preferences
        counterTextView.text = "$postcardCount"

        // declaring variables to store shared preferences values
        val recentImage = prefs.getInt("selectedImage", R.drawable.image1)
        val recentReceiver = prefs.getString("receiverName", "Unknown")
        val recentSender = prefs.getString("senderName", "Unknown")

        // updating layout elements to match values stored in shared preferences
        findViewById<TextView>(R.id.recentSender).text = "$recentSender"
        findViewById<TextView>(R.id.recentReceiver).text = "$recentReceiver"
        findViewById<ImageView>(R.id.recentPostcardImage).setImageResource(recentImage)

        // back button - return to homepage
        val backBtn = findViewById<Button>(R.id.recentBackButton)
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}