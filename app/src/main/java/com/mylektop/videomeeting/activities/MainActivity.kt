package com.mylektop.videomeeting.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.mylektop.videomeeting.R
import com.mylektop.videomeeting.adapters.UsersAdapter
import com.mylektop.videomeeting.lisneters.UsersListener
import com.mylektop.videomeeting.models.User
import com.mylektop.videomeeting.utilities.Constants
import com.mylektop.videomeeting.utilities.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UsersListener {

    companion object {
        private const val REQUEST_CODE_BATTERY_OPTIMIZATION: Int = 1
    }

    private lateinit var preferenceManager: PreferenceManager
    private var users: ArrayList<User>? = null
    private var userAdapter: UsersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferenceManager = PreferenceManager(applicationContext)

        textTitle.text = String.format(
            "%s %s",
            preferenceManager.getString(Constants.KEY_FIRST_NAME),
            preferenceManager.getString(Constants.KEY_LAST_NAME)
        )

        textSignOut.setOnClickListener {
            signOut()
        }

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                sendFCMTokenToDatabase(task.result!!.token)
            }
        }

        users = ArrayList()
        userAdapter = UsersAdapter(users!!, this)
        recyclerViewUsers.adapter = userAdapter

        swipeRefreshLayout.setOnRefreshListener(this::getUsers)

        getUsers()
        checkForBatteryOptimizations()
    }

    private fun sendFCMTokenToDatabase(token: String) {
        val database = FirebaseFirestore.getInstance()
        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
            .document(preferenceManager.getString(Constants.KEY_USER_ID))
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
            .addOnFailureListener { e -> Toast.makeText(this, "Unable to send token: ${e.message}", Toast.LENGTH_SHORT).show() }
//            .addOnSuccessListener { Toast.makeText(this, "Token update successfully", Toast.LENGTH_SHORT).show() }
    }

    private fun signOut() {
        Toast.makeText(this, "Signing Out ...", Toast.LENGTH_SHORT).show()

        val database = FirebaseFirestore.getInstance()
        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
            .document(preferenceManager.getString(Constants.KEY_USER_ID))
        val updates = HashMap<String, Any>()
        updates[Constants.KEY_FCM_TOKEN] = FieldValue.delete()
        documentReference.update(updates)
            .addOnSuccessListener {
                preferenceManager.clearPreferences()
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Unable to sign out: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getUsers() {
        swipeRefreshLayout.isRefreshing = true
        val database = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .get()
            .addOnCompleteListener { task ->
                swipeRefreshLayout.isRefreshing = false
                val myUserId = preferenceManager.getString(Constants.KEY_USER_ID)
                if (task.isSuccessful && task.result != null) {
                    users?.clear()
                    for (documentSnapshot: QueryDocumentSnapshot in task.result!!) {
                        if (myUserId.equals(documentSnapshot.id)) {
                            continue
                        }

                        val user = User()
                        user.firstName = documentSnapshot.getString(Constants.KEY_FIRST_NAME)
                        user.lastName = documentSnapshot.getString(Constants.KEY_LAST_NAME)
                        user.email = documentSnapshot.getString(Constants.KEY_EMAIL)
                        user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN)

                        users?.add(user)
                    }

                    if (users?.size!! > 0) {
                        userAdapter?.notifyDataSetChanged()
                    } else {
                        textErrorMessage.text = String.format("%s", "No Users available")
                        textErrorMessage.visibility = View.VISIBLE
                    }
                } else {
                    textErrorMessage.text = String.format("%s", "No Users available")
                    textErrorMessage.visibility = View.VISIBLE
                }
            }
    }

    override fun initiateAudioMeeting(user: User) {
        if (user.token == null || user.token?.trim()?.isEmpty()!!) {
            Toast.makeText(this, "${user.firstName} ${user.lastName} is not available for meeting", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(applicationContext, OutgoingInvitationActivity::class.java)
            intent.putExtra("user", user)
            intent.putExtra("type", "audio")
            startActivity(intent)
        }
    }

    override fun initiateVideoMeeting(user: User) {
        if (user.token == null || user.token?.trim()?.isEmpty()!!) {
            Toast.makeText(this, "${user.firstName} ${user.lastName} is not available for meeting", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(applicationContext, OutgoingInvitationActivity::class.java)
            intent.putExtra("user", user)
            intent.putExtra("type", "video")
            startActivity(intent)
        }
    }

    override fun onMultipleUsersAction(isMultipleUsersSelected: Boolean) {
        if (isMultipleUsersSelected) {
            imageConference.visibility = View.VISIBLE
            imageConference.setOnClickListener {
                val intent = Intent(applicationContext, OutgoingInvitationActivity::class.java)
                intent.putExtra("selectedUsers", Gson().toJson(userAdapter?.getSelectedUsers()))
                intent.putExtra("type", "video")
                intent.putExtra("isMultiple", true)
                startActivity(intent)
            }
        } else {
            imageConference.visibility = View.GONE
        }
    }

    private fun checkForBatteryOptimizations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager: PowerManager = getSystemService(POWER_SERVICE) as PowerManager
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Warning")
                builder.setMessage("Battery optimization is enabled. It can interrupt running background services")
                builder.setPositiveButton("Disabled") { dialog, which ->
                    val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                    startActivityForResult(intent, REQUEST_CODE_BATTERY_OPTIMIZATION)
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                builder.create().show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_BATTERY_OPTIMIZATION) {
            checkForBatteryOptimizations()
        }
    }
}