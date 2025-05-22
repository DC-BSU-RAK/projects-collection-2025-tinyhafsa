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

class ImageActivity : AppCompatActivity() {

    private lateinit var ImageButtons: List<ImageButton>
    private var selectedImageId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_image)

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

        // lisitng image buttons
        ImageButtons = listOf(
            findViewById(R.id.image1),
            findViewById(R.id.image2),
            findViewById(R.id.image3),
            findViewById(R.id.image4),
            findViewById(R.id.image5),
            findViewById(R.id.image6)
        )

        // colors
        val DefaultColor = getColor(R.color.two)
        val SelectedColor = getColor(R.color.three)

        // loop - set a default color for the buttons unless a button is selected
        for (button in ImageButtons) {
            button.setOnClickListener {
                for (b in ImageButtons) {
                    b.setBackgroundColor(DefaultColor)
                }

                it.setBackgroundColor(SelectedColor)
                selectedImageId = button.id // get selected button id
            }
        }

        // continue button
        val continueButton = findViewById<Button>(R.id.messagePreviewButton)
        continueButton.setOnClickListener {
            // if a button is selected, store the button image
            if (selectedImageId != null) {
                // 🔁 Map view ID to drawable resource ID
                val selectedDrawableId = when (selectedImageId) {
                    R.id.image1 -> R.drawable.image1
                    R.id.image2 -> R.drawable.image2
                    R.id.image3 -> R.drawable.image3
                    R.id.image4 -> R.drawable.image4
                    R.id.image5 -> R.drawable.image5
                    R.id.image6 -> R.drawable.image6
                    else -> R.drawable.image1 // fallback default
                }

                // store the button id in shared preferences
                val sharedPref = getSharedPreferences("PostPrefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putInt("selectedImage", selectedDrawableId)
                    apply()
                }

                // show next screen
                val intent = Intent(this, MessageActivity::class.java)
                startActivity(intent)

            } else {// if an image is not selected, show toast message
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}