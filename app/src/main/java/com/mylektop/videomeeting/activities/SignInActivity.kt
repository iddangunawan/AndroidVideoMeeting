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
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private var preferenceManager: PreferenceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        preferenceManager = PreferenceManager(applicationContext)

        if (preferenceManager?.getBoolean(Constants.KEY_IS_SIGNED_IN)!!) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        textSignUp.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }

        buttonSignIn.setOnClickListener {
            if (inputEmail.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString()).matches()) {
                Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show()
            } else if (inputPassword.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
            } else {
                signIn()
            }
        }
    }

    private fun signIn() {
        buttonSignIn.visibility = View.INVISIBLE
        progressBarSignIn.visibility = View.VISIBLE

        val database = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .whereEqualTo(Constants.KEY_EMAIL, inputEmail.text.toString())
            .whereEqualTo(Constants.KEY_PASSWORD, inputPassword.text.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null && task.result!!.documents.size > 0) {
                    val documentSnapshot = task.result!!.documents[0]
                    preferenceManager?.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
                    preferenceManager?.putString(Constants.KEY_FIRST_NAME, documentSnapshot.getString(Constants.KEY_FIRST_NAME)!!)
                    preferenceManager?.putString(Constants.KEY_LAST_NAME, documentSnapshot.getString(Constants.KEY_LAST_NAME)!!)
                    preferenceManager?.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL)!!)

                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else {
                    buttonSignIn.visibility = View.VISIBLE
                    progressBarSignIn.visibility = View.INVISIBLE
                    Toast.makeText(this, "Unable to sign in", Toast.LENGTH_SHORT).show()
                }
            }
    }
}