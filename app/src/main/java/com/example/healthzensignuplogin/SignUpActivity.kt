package com.example.healthzensignuplogin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signupName: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupUsername: EditText
    private lateinit var signupPassword: EditText
    private lateinit var signupButton: Button
    private lateinit var loginRedirectText: TextView
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        signupName = findViewById(R.id.signup_name)
        signupEmail = findViewById(R.id.signup_email)
        signupUsername = findViewById(R.id.signup_username)
        signupPassword = findViewById(R.id.signup_password)
        signupButton = findViewById(R.id.signup_button)
        loginRedirectText = findViewById(R.id.loginRedirectText)

        signupButton.setOnClickListener {
            database = FirebaseDatabase.getInstance()
            reference = database.getReference("users")

            val name = signupName.text.toString().trim()
            val email = signupEmail.text.toString().trim()
            val username = signupUsername.text.toString().trim()
            val password = signupPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@SignUpActivity, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val helperClass = HelperClass(name, email, username, password)
            reference.child(name).setValue(helperClass)

            Toast.makeText(this@SignUpActivity, "You have Signed Up successfully!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@SignUpActivity, LogInActivity::class.java)
            startActivity(intent)

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    val userID = currentUser?.uid ?: ""
                    val helperClass = HelperClass(name, email, username, password)

                    reference.child(userID).setValue(helperClass).addOnCompleteListener { userTask ->
                        if (userTask.isSuccessful) {
                            Toast.makeText(this@SignUpActivity, "SignUp Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignUpActivity, LogInActivity::class.java)
                            startActivity(intent)
                        } else {
                            val errorMessage = "Failed to save user data: ${userTask.exception?.message}"
                            Log.e(TAG, errorMessage)
                            Toast.makeText(this@SignUpActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val errorMessage = "SignUp Failed: ${task.exception?.message}"
                    Log.e(TAG, errorMessage)
                    Toast.makeText(this@SignUpActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginRedirectText.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))
        }
    }
}

