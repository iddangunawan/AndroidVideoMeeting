package com.mylektop.videomeeting.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.mylektop.videomeeting.R
import com.mylektop.videomeeting.utilities.Constants
import com.mylektop.videomeeting.utilities.PreferenceManager
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        preferenceManager = PreferenceManager(applicationContext)

        imgBack.setOnClickListener {
            onBackPressed()
        }

        textSignIn.setOnClickListener {
            onBackPressed()
        }

        buttonSignUp.setOnClickListener {
            if (inputFirstName.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter first name", Toast.LENGTH_SHORT).show()
            } else if (inputLastName.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter last name", Toast.LENGTH_SHORT).show()
            } else if (inputEmail.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString()).matches()) {
                Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show()
            } else if (inputPassword.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
            } else if (inputConfirmPassword.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Confirm your password", Toast.LENGTH_SHORT).show()
            } else if (inputPassword.text.toString() != inputConfirmPassword.text.toString()) {
                Toast.makeText(this, "Password & confirm password must be same", Toast.LENGTH_SHORT).show()
            } else {
                signUp()
            }
        }
    }

    private fun signUp() {
        buttonSignUp.visibility = View.INVISIBLE
        progressBarSignUp.visibility = View.VISIBLE

        val database = FirebaseFirestore.getInstance()
        val user = HashMap<String, Any>()
        user[Constants.KEY_FIRST_NAME] = inputFirstName.text.toString()
        user[Constants.KEY_LAST_NAME] = inputLastName.text.toString()
        user[Constants.KEY_EMAIL] = inputEmail.text.toString()
        user[Constants.KEY_PASSWORD] = inputPassword.text.toString()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .add(user)
            .addOnSuccessListener { documentReference ->
                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
                preferenceManager.putString(Constants.KEY_USER_ID, documentReference.id)
                preferenceManager.putString(Constants.KEY_FIRST_NAME, inputFirstName.text.toString())
                preferenceManager.putString(Constants.KEY_LAST_NAME, inputLastName.text.toString())
                preferenceManager.putString(Constants.KEY_EMAIL, inputEmail.text.toString())

                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                buttonSignUp.visibility = View.VISIBLE
                progressBarSignUp.visibility = View.INVISIBLE

                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}