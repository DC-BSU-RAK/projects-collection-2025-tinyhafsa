package com.example.stampgeneratorapp

// necessary imports
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StampActivity : AppCompatActivity() {

    // declaring variables to be used later
    private lateinit var categoryTitle: TextView
    private lateinit var subjectTitle: TextView
    private lateinit var subjectGroup: RadioGroup
    private lateinit var showStampButton: ImageButton

    private lateinit var subject1: RadioButton
    private lateinit var subject2: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stamp)

        // removes the top and bottom system bars to show the app in full screen size
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

        // attaching variables to elements in the layout
        categoryTitle = findViewById(R.id.categoryTitle)
        subjectTitle = findViewById(R.id.subjectTitle)
        subjectGroup = findViewById(R.id.subjectGroup)
        subject1 = findViewById(R.id.subject1Btn)
        subject2 = findViewById(R.id.subject2Btn)
        showStampButton = findViewById(R.id.showStampBtn)


        // declaring Category Buttons
        val flowerBtn: RadioButton = findViewById(R.id.flowerCategory)
        val fruitBtn: RadioButton = findViewById(R.id.fruitCategory)
        val insectBtn: RadioButton = findViewById(R.id.insectCategory)

        // declaring Instructions Button
        val instructionsBtn: ImageButton = findViewById(R.id.instructionsBtn)
        instructionsBtn.setOnClickListener {
            showInstructionsPopup()
        }

        // hiding Show Stamp Button initially
        showStampButton.visibility = View.GONE

        // Category Button functions
        flowerBtn.setOnClickListener { showSubjects("flowers") }
        fruitBtn.setOnClickListener { showSubjects("fruits") }
        insectBtn.setOnClickListener { showSubjects("insects") }

        // subject function
        subjectGroup.setOnCheckedChangeListener { _, checkedId ->
            // if an option is selected, display subject name and show stamp button
            if (checkedId != -1 ) {
                val selectedSubject = findViewById<RadioButton>(checkedId)
                subjectTitle.text = selectedSubject.contentDescription.toString()
                showStampButton.visibility = View.VISIBLE
            } else { // if no subject is selected, hide subject name and stamp button
                subjectTitle.text = ""
                showStampButton.visibility = View.GONE
            }
        }

        // show stamp button function
        showStampButton.setOnClickListener {
            showStampPopup()
        }
    }

    // Function - show subjects when a category is selected
    private fun showSubjects(category: String) {

        // make title, and subject visible
        // [if category is switched] hide show stamp button - remove subject selection
        subjectTitle.visibility = View.VISIBLE
        subjectGroup.visibility = View.VISIBLE
        showStampButton.visibility = View.GONE
        subjectGroup.clearCheck()

        // subjects to be displayed when a certain category is selected
        when (category) {
            "flowers" -> {
                categoryTitle.text = "Flowers"
                subject1.setBackgroundResource(R.drawable.flower_subject_1)
                subject1.contentDescription = "Aster" // to be displayed when a subject is selected
                subject1.tag = R.drawable.aster // to be shown as the result
                subject2.setBackgroundResource(R.drawable.flower_subject_2)
                subject2.contentDescription = "Peony"
                subject2.tag = R.drawable.peony
            }
            "fruits" -> {
                categoryTitle.text = "Fruits"
                subject1.setBackgroundResource(R.drawable.fruit_subject_1)
                subject1.contentDescription = "Raspberry"
                subject1.tag = R.drawable.blueberry
                subject2.setBackgroundResource(R.drawable.fruit_subject_2)
                subject2.contentDescription = "Blueberry"
                subject2.tag = R.drawable.raspberry
            }
            "insects" -> {
                categoryTitle.text = "Insects"
                subject1.setBackgroundResource(R.drawable.insect_subject_1)
                subject1.contentDescription = "Dragonfly"
                subject1.tag = R.drawable.dragonfly
                subject2.setBackgroundResource(R.drawable.insect_subject_2)
                subject2.contentDescription = "Moth"
                subject2.tag = R.drawable.moth
            }
        }

        // intial title text
        subjectTitle.text = ""
    }

    // Function - result popup
    private fun showStampPopup() {
        // display popup
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_result_popup, null)
        // result image
        val resultStampImage = dialogView.findViewById<ImageView>(R.id.resultStamp)
        // create again button
        val createAgainBtn = dialogView.findViewById<ImageButton>(R.id.createAgainBtn)

        // showing the result based on subject selection
        val selectedSubjectId = subjectGroup.checkedRadioButtonId
        val stampResult = when (selectedSubjectId) {
            R.id.subject1Btn -> subject1.tag as? Int
            R.id.subject2Btn -> subject2.tag as? Int
            else -> null
        }

        // setting the result image
        stampResult?.let {
            resultStampImage.setImageResource(it)
        }

        // display popup
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // create again button - dismisses popup and resets selection
        createAgainBtn.setOnClickListener {
            dialog.dismiss()
            resetToInitialState()
        }
        // popup background is transparent
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        // show popup
        dialog.show()
    }

    // Function - reset options
    private fun resetToInitialState() {
        // title, subject and show stamp button are hidden
        subjectTitle.visibility = View.GONE
        subjectGroup.visibility = View.GONE
        showStampButton.visibility = View.GONE
        // cleared subject selection
        subjectGroup.clearCheck()

        // remove category name
        categoryTitle.text = ""
        subjectTitle.text = ""

        // declaring variables
        val flowerBtn: RadioButton = findViewById(R.id.flowerCategory)
        val fruitBtn: RadioButton = findViewById(R.id.fruitCategory)
        val insectBtn: RadioButton = findViewById(R.id.insectCategory)

        // removing category selection
        flowerBtn.isChecked = false
        fruitBtn.isChecked = false
        insectBtn.isChecked = false
    }

    // Function - show instructions popup
    private fun showInstructionsPopup() {
        // display popup
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_instructions_popup, null)
        // close instructions button
        val closeBtn = dialogView.findViewById<ImageButton>(R.id.closeBtn)

        // setting up popup
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // close button closes instructions popup
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        // popup window background is transparent
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        // show popup
        dialog.show()
    }

}