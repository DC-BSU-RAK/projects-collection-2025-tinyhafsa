package com.example.postexpress

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast


class StampActivity : AppCompatActivity() {

    // declaring buttons variables as a list
    private lateinit var StampButtons: List<ImageButton>
    private var selectedStampId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stamp)

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

        // list of stamp images
        StampButtons = listOf(
            findViewById(R.id.stamp1),
            findViewById(R.id.stamp2),
            findViewById(R.id.stamp3),
            findViewById(R.id.stamp4),
            findViewById(R.id.stamp5),
            findViewById(R.id.stamp6)
        )

        // colors
        val DefaultColor = getColor(R.color.two)
        val SelectedColor = getColor(R.color.three)

        // loop - if button is selected change background color of stamp, else keep it as default
        for (button in StampButtons) {
            button.setOnClickListener {
                for (b in StampButtons) {
                    b.setBackgroundColor(DefaultColor)
                }
                it.setBackgroundColor(SelectedColor)
                selectedStampId = button.id // save id of stamp image
            }
        }

        // continue button
        val ContinueButton = findViewById<Button>(R.id.messagePreviewButton)
        ContinueButton.setOnClickListener {
            if (selectedStampId != null) {
                // map stamp id ID to drawable resource
                val selectedDrawableId = when (selectedStampId) {
                    R.id.stamp1 -> R.drawable.stamp1
                    R.id.stamp2 -> R.drawable.stamp2
                    R.id.stamp3 -> R.drawable.stamp3
                    R.id.stamp4 -> R.drawable.stamp4
                    R.id.stamp5 -> R.drawable.stamp5
                    R.id.stamp6 -> R.drawable.stamp6
                    else -> R.drawable.stamp1 // default
                }
                // store stamp id in shared preferences
                val sharedPref = getSharedPreferences("PostPrefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putInt("selectedStamp", selectedDrawableId)
                    apply()
                }

                // go to next page
                val intent = Intent(this, ImageActivity::class.java)
                startActivity(intent)
            } else { // if a button is not selected, show toast message
                Toast.makeText(this, "Please select a stamp", Toast.LENGTH_SHORT).show()
            }
        }
    }
}