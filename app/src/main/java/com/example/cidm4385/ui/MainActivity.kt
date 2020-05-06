package com.example.cidm4385.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cidm4385.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

//This is intiziling with firebase database and authentication
class MainActivity : AppCompatActivity() {
    // Intiziling auth with firebase
    private lateinit var auth: FirebaseAuth
    companion object {
        val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // This is linked to the sign up button which will perform the register sequence
        register_button_register.setOnClickListener{
            performRegister()
        }
        // This will check whether the user already has an account with the database
         already_have_account_textview.setOnClickListener{
            Log.d("MainActivity", "Try to show login activity")
            //launch the login acativity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
    // This function will perform the register ability to be able to sign users up
    // I created two variables below for the registeration IDs email and password
    private fun performRegister(){
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()
        // IF statement will check if the email and password field are empty and will display a toast message indicting such
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please enter a vaild username and password", Toast.LENGTH_SHORT).show()
            return
        }
        // Log.d was used to check in the debugging logcat whether proper information was being passed through the email and password field
        Log.d("MainActivity", "Email is:" + email)
        Log.d("MainActivity", "Password: + $password")

        //FireBase Auth Method to create user with email and password
        // We will use the .createUserWithEmailAndPassword to create new users through the firebase authentication methods
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            if(!it.isSuccessful) return@addOnCompleteListener
            Log.d("Main","Successfully created user with UID: ${it.result?.user?.uid}")
            saveUserToFirebaseDatabase(it.toString())
        }

            .addOnFailureListener {
                Log.d("Main", "Failed to Create user: ${it.message}")
                Toast.makeText(this, "Failed ${it.message}", Toast.LENGTH_SHORT).show()
            }


    }

    // this function saves users into the realtime database by saving the UDS and the usernames
    private fun saveUserToFirebaseDatabase( pass : String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid,
            username_edittext_register.text.toString(),
            pass
        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")

                val intent = Intent(this, LatestActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
            }
    }
}
class User(val uid: String, val username: String, val pass: String)