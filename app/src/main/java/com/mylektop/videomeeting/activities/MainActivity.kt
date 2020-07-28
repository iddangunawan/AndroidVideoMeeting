package com.mylektop.videomeeting.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.mylektop.videomeeting.R
import com.mylektop.videomeeting.utilities.Constants
import com.mylektop.videomeeting.utilities.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var preferenceManager: PreferenceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferenceManager = PreferenceManager(applicationContext)

        textTitle.text = String.format(
            "%s %s",
            preferenceManager?.getString(Constants.KEY_FIRST_NAME),
            preferenceManager?.getString(Constants.KEY_LAST_NAME)
        )

        textSignOut.setOnClickListener {
            signOut()
        }

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                sendFCMTokenToDatabase(task.result?.token!!)
            }
        }
    }

    private fun sendFCMTokenToDatabase(token: String) {
        val database = FirebaseFirestore.getInstance()
        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
            .document(preferenceManager?.getString(Constants.KEY_USER_ID)!!)
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
            .addOnSuccessListener {
                Toast.makeText(this, "Token update successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Unable to send token: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun signOut() {
        Toast.makeText(this, "Signing Out ...", Toast.LENGTH_SHORT).show()

        val database = FirebaseFirestore.getInstance()
        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
            .document(preferenceManager?.getString(Constants.KEY_USER_ID)!!)
        val updates = HashMap<String, Any>()
        updates[Constants.KEY_FCM_TOKEN] = FieldValue.delete()
        documentReference.update(updates)
            .addOnSuccessListener {
                preferenceManager?.clearPreferences()
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Unable to sign out: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}