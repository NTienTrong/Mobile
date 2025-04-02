package com.edu.sharedprefsdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var displayButton: Button
    private lateinit var displayTextView: TextView
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)
        displayButton = findViewById(R.id.displayButton)
        displayTextView = findViewById(R.id.displayTextView)
        preferenceHelper = PreferenceHelper(this)

        saveButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            preferenceHelper.save("username", username)
            preferenceHelper.save("password", password)
        }

        deleteButton.setOnClickListener {
            preferenceHelper.clearSharedPreference()
            displayTextView.text = ""
        }

        displayButton.setOnClickListener {
            val username = preferenceHelper.getValueString("username")
            val password = preferenceHelper.getValueString("password")
            displayTextView.text = "Tên đăng nhập: $username\nMật khẩu: $password"
        }
    }
}