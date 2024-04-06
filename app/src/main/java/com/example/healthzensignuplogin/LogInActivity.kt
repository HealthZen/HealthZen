package com.example.healthzensignuplogin

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LogInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupRedirectText: TextView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var forgetpassword:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        loginEmail = findViewById(R.id.login_email)
        loginPassword = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signupRedirectText = findViewById(R.id.signupRedirectText)
        forgetpassword=findViewById(R.id.forgot_password)

        loginButton.setOnClickListener {
            val email = loginEmail.text.toString().trim()
            val pass = loginPassword.text.toString().trim()

            if (email.isEmpty()) {
                loginEmail.setError("Email cannot be empty")
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginEmail.setError("Please enter a valid email")
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                loginPassword.setError("Password cannot be empty")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener { authResult ->
                    val user = authResult.user
                    if (user != null) {
                        fetchUserDataAndPassToProfile(user.uid)
                    } else {
                        Toast.makeText(this@LogInActivity, "User is null", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Login Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        forgetpassword.setOnClickListener {
            val builder= AlertDialog.Builder(this)
            val view=layoutInflater.inflate(R.layout.dialog_forgot,null)
            val userEmail=view.findViewById<EditText>(R.id.editBox)

            builder.setView(view)
            val dialog=builder.create()


            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                compareEmail(userEmail)
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }
            if (dialog.window!=null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()


        }


        signupRedirectText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }


    }
    private fun fetchUserDataAndPassToProfile(userId: String) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userData = document.data
                    val name = userData?.get("name").toString()
                    val email = userData?.get("email").toString()
                    val username = userData?.get("username").toString()
                    val password = userData?.get("password").toString()

                    // Store user data in SharedPreferences or ViewModel
                    val sharedPref = getSharedPreferences("UserData", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("name", name)
                        putString("email", email)
                        putString("username", username)
                        putString("password", password)
                        apply()
                    }

                    // Start MainActivity
                    startActivity(Intent(this@LogInActivity, MainActivity::class.java))
                    finish() // Finish the current activity to prevent going back to the login screen
                } else {
                    Toast.makeText(this@LogInActivity, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@LogInActivity, "Failed to fetch user data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun compareEmail(email:EditText){
        if(email.text.toString().isEmpty()){
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }
        auth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener {
                task->
            if (task.isSuccessful){
                Toast.makeText(this,"check your email",Toast.LENGTH_SHORT).show()
            }
        }
    }
}