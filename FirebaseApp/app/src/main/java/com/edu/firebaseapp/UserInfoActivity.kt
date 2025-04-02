package com.edu.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserInfoActivity : AppCompatActivity() {

    private lateinit var emailTextView: TextView
    private lateinit var uidTextView: TextView
    private lateinit var loginTimeTextView: TextView
    private lateinit var createdAtTextView: TextView
    private lateinit var lastLoginTextView: TextView
    private lateinit var emailDatabaseTextView: TextView
    private lateinit var logoutButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        emailTextView = findViewById(R.id.emailTextView)
        uidTextView = findViewById(R.id.uidTextView)
        loginTimeTextView = findViewById(R.id.loginTimeTextView)
        createdAtTextView = findViewById(R.id.createdAtTextView)
        lastLoginTextView = findViewById(R.id.lastLoginTextView)
        emailDatabaseTextView = findViewById(R.id.emailDatabaseTextView)
        logoutButton = findViewById(R.id.logoutButton)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val user = auth.currentUser
        if (user != null) {
            emailTextView.text = "Email: ${user.email}"
            uidTextView.text = "UID: ${user.uid}"
            loginTimeTextView.text = "Thời gian đăng nhập: ${getCurrentDateTime()}"

            database.reference.child("users").child(user.uid).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.value as? Map<*, *>
                    if (userData != null) {
                        createdAtTextView.text = "createdAt: ${formatTimestamp(userData["createdAt"] as? Long)}"
                        lastLoginTextView.text = "lastLogin: ${getCurrentDateTime()}"
                        emailDatabaseTextView.text = "email: ${userData["email"]}"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@UserInfoActivity, "Lỗi khi đọc dữ liệu: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun formatTimestamp(timestamp: Long?): String {
        if (timestamp == null) return "N/A"
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}