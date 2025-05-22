package com.example.postexpress

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

class PreviewActivity : AppCompatActivity() {

    // declaring variable for preferences
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preview)

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

        // attaching variable to shared preferences
        preferences = getSharedPreferences("PostPrefs", MODE_PRIVATE)

        // declaring variables and attaching them to values saved inside the shared preferences
        val stampId = preferences.getInt("selectedStamp", R.drawable.stamp1)
        val imageId = preferences.getInt("selectedImage", R.drawable.image1)
        val greeting = preferences.getString("greeting", "Dear") + " " + preferences.getString("receiverName", "") + ","
        val message = preferences.getString("message", "")
        val signoff = preferences.getString("signoff", "With Love") + ", \n" + preferences.getString("senderName", "")
        val receiver = preferences.getString("receiverName", "mama")
        val sender = preferences.getString("senderName", "hafsa")

        // declaring variables - attaching them to layout elements
        val stampPreview = findViewById<ImageView>(R.id.stampPreview)
        val imagePreview = findViewById<ImageView>(R.id.imagePreview)
        val greetingPreview = findViewById<TextView>(R.id.greetingPreview)
        val messagePreview = findViewById<TextView>(R.id.messagePreview)
        val signoffPreview = findViewById<TextView>(R.id.signoffPreview)
        val receiverPreview = findViewById<TextView>(R.id.receieverPreview)
        val senderPreview = findViewById<TextView>(R.id.senderPreview)

        // attaching shared preferenced values to layout elements to be displayed on screen
        stampPreview.setImageResource(stampId)
        imagePreview.setImageResource(imageId)
        greetingPreview.text = greeting
        messagePreview.text = message
        signoffPreview.text = signoff
        receiverPreview.text = receiver
        senderPreview.text = sender

        // change button - return to names page
        findViewById<Button>(R.id.changeButton).setOnClickListener {
            startActivity(Intent(this, NamesActivity::class.java))
        }

        // send button
        val sendButton = findViewById<Button>(R.id.sendButton)
        sendButton.setOnClickListener {
            // store number of postcards created in preferences
            val prefs = getSharedPreferences("PostPrefs", Context.MODE_PRIVATE)
            val currentCount = prefs.getInt("postcardCount", 0)
            with(prefs.edit()) {
                putInt("postcardCount", currentCount + 1)
                apply()
            }

            // go to next page
            val intent = Intent(this, SentActivity::class.java)
            startActivity(intent)
        }
    }
}