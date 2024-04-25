package com.example.healthzensignuplogin

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var signupName: EditText
    private lateinit var signupEmail: EditText
    private lateinit var signupUsername: EditText
    private lateinit var signupPassword: EditText
    private lateinit var signupButton: Button
    private lateinit var loginRedirectText: TextView
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        signupName = findViewById(R.id.signup_name)
        signupEmail = findViewById(R.id.signup_email)
        signupUsername = findViewById(R.id.signup_username)
        signupPassword = findViewById(R.id.signup_password)
        signupButton = findViewById(R.id.signup_button)
        loginRedirectText = findViewById(R.id.loginRedirectText)


        signupButton.setOnClickListener {
            val name = signupName.text.toString().trim()
            val email = signupEmail.text.toString().trim()
            val username = signupUsername.text.toString().trim()
            val password = signupPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@SignUpActivity, "All fields are required", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    val userId = currentUser?.uid ?: ""
                    val user = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "username" to username,
                        // Add other user data as needed
                    )

                    firestore.collection("users").document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            val userDocRef = firestore.collection("users").document(userId)
                            userDocRef.collection("liked_posts").document("init")
                                .set(hashMapOf("init" to true)) // Add a dummy document to trigger the success callback
                                .addOnSuccessListener {
                                    // Remove the dummy document
                                    userDocRef.collection("liked_posts").document("init").delete()
                                        .addOnSuccessListener {
                                            Log.d(
                                                TAG,
                                                "Liked posts subcollection created for user $userId"
                                            )
                                            Toast.makeText(
                                                this@SignUpActivity,
                                                "SignUp Successful",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            startActivity(
                                                Intent(
                                                    this@SignUpActivity,
                                                    LogInActivity::class.java
                                                )
                                            )
                                        }
                                        .addOnFailureListener { exception ->
                                            // Handle any errors
                                            Log.e(TAG, "Error deleting dummy document: $exception")
                                            Toast.makeText(
                                                this@SignUpActivity,
                                                "Error deleting dummy document",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                                .addOnFailureListener { exception ->
                                    // Handle any errors
                                    Log.e(
                                        TAG,
                                        "Error creating liked_posts subcollection: $exception"
                                    )
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "Error creating liked_posts subcollection",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            Toast.makeText(
                                this@SignUpActivity,
                                "SignUp Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))

                        }
                        .addOnFailureListener { e ->
                            val errorMessage = "Failed to save user data: ${e.message}"
                            Log.e(TAG, errorMessage)
                            Toast.makeText(this@SignUpActivity, errorMessage, Toast.LENGTH_SHORT)
                                .show()
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
