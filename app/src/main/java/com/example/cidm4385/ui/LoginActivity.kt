package com.example.cidm4385.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cidm4385.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            performLogin()
        }

        back_to_register_textview.setOnClickListener{
            finish()
        }
    }
    // This function is able to check if the email and password values with the database and determine the user authentication
    private fun performLogin() {
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please type a username and password.", Toast.LENGTH_SHORT).show()
            return
        }
        //Firebase get instance .signInWithEmailAndPassword will cross reference the information typed in with the database
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")
                Toast.makeText(baseContext, "Authentication Successful.",
                    Toast.LENGTH_SHORT).show()

                // The Intent here i simple message object that is used to communicate between android components such as activities.
                val intent = Intent(this, LatestActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)


            }
                // OnFailure Message provides detailed information regarding authentication issues
            .addOnFailureListener {
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }



}