package com.example.healthzensignuplogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class LogInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupRedirectText: TextView
    //private lateinit var database: FirebaseDatabase
    //private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        auth = FirebaseAuth.getInstance()
        //database = FirebaseDatabase.getInstance()
        //reference = database.reference.child("users")

        loginEmail = findViewById(R.id.login_email)
        loginPassword = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signupRedirectText = findViewById(R.id.signupRedirectText)

        loginButton.setOnClickListener {
            val email = loginEmail.text.toString().trim()
            val pass = loginPassword.text.toString().trim()

            if (email.isEmpty()) {
                loginEmail.setError("Email cannot be empty")
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginEmail.setError("Please enter valid email")
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                loginPassword.setError("Password cannot be empty")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    val user: FirebaseUser? = auth.currentUser
                    val userID: String = user!!.uid

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Login Failed: " + e.message, Toast.LENGTH_SHORT).show()
                }
        }

        signupRedirectText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun checkUser() {
        val userEmail = loginEmail.text.toString().trim()
        val userPassword = loginPassword.text.toString().trim()

        val reference = FirebaseDatabase.getInstance().getReference("users")
        val checkUserDatabase = reference.orderByChild("email").equalTo(userEmail)

        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    loginEmail.setError(null)
                    val passwordFromDB = snapshot.child(userEmail).child("password").getValue(String::class.java)

                    if (passwordFromDB != userPassword) {
                        loginEmail.setError(null)
                        val intent = Intent(this@LogInActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        loginPassword.setError("Invalid Credentials")
                        loginPassword.requestFocus()
                    }
                } else {
                    loginEmail.setError("User does not exist")
                    loginEmail.requestFocus()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}