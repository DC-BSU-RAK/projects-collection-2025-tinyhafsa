package com.example.postexpress

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.os.Build
import android.view.WindowInsets
import android.view.WindowInsetsController


class MainActivity : AppCompatActivity() {

    // declaring buttons
    private lateinit var StartBtn: Button
    private lateinit var InstructionsBtn: Button
    private lateinit var RecentBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

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

        // attaching button variables to elements on the layout
        StartBtn = findViewById(R.id.homeStartButton)
        InstructionsBtn = findViewById(R.id.homeInstructionsButton)

        // start button - leads to next page
        StartBtn.setOnClickListener {
            val intent = Intent(this, NamesActivity::class.java)
            startActivity(intent)
        }

        // instructions button - shows instructions in a popup
        InstructionsBtn.setOnClickListener {
            showInstructionsPopup()
        }

        // recent button - shows recent page
        RecentBtn = findViewById(R.id.homeRecentButton)
        RecentBtn.setOnClickListener {
            val intent = Intent(this, RecentActivity::class.java)
            startActivity(intent)
        }
    }

    // Function - displays instruction popup
    private fun showInstructionsPopup() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_instructions_popup, null)
        // close button - closes instruction popup
        val closeBtn = dialogView.findViewById<Button>(R.id.instructionsCloseButton)

        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawableResource(R.drawable.instructions_bg)
        dialog.show()
    }

}