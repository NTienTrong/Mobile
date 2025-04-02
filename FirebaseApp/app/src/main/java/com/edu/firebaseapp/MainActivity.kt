package com.edu.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    private lateinit var displayDataButton: Button
    private lateinit var errorTextView: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        loginButton = findViewById(R.id.loginButton)
        displayDataButton = findViewById(R.id.displayDataButton)
        errorTextView = findViewById(R.id.errorTextView)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Đăng ký thành công.", Toast.LENGTH_SHORT).show()
                        saveUserData()
                    } else {
                        errorTextView.text = "Đăng ký thất bại: ${task.exception?.message}"
                        errorTextView.visibility = View.VISIBLE
                    }
                }
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, UserInfoActivity::class.java)
                        startActivity(intent)
                    } else {
                        errorTextView.text = "Đăng nhập thất bại: ${task.exception?.message}"
                        errorTextView.visibility = View.VISIBLE
                    }
                }
        }

        displayDataButton.setOnClickListener {
            val user = auth.currentUser
            if (user != null) {
                database.reference.child("users").child(user.uid).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val userData = snapshot.value as? Map<*, *>
                            if (userData != null) {
                                val intent = Intent(this@MainActivity, UserInfoActivity::class.java)
                                intent.putExtra("email", userData["email"] as? String)
                                intent.putExtra("createdAt", userData["createdAt"] as? Long)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@MainActivity, "Dữ liệu không hợp lệ.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Không có dữ liệu.", Toast.LENGTH_SHORT).show()
                        }                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity, "Lỗi khi đọc dữ liệu: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@MainActivity, "Vui lòng đăng nhập trước.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userData = hashMapOf(
                "email" to user.email,
                "createdAt" to System.currentTimeMillis()
            )
            database.reference.child("users").child(user.uid).setValue(userData)
        }
    }
}