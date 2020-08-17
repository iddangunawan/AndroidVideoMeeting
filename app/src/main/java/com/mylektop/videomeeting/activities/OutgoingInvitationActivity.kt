package com.mylektop.videomeeting.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.mylektop.videomeeting.R
import com.mylektop.videomeeting.models.User
import com.mylektop.videomeeting.network.ApiClient
import com.mylektop.videomeeting.network.ApiService
import com.mylektop.videomeeting.utilities.Constants
import com.mylektop.videomeeting.utilities.PreferenceManager
import kotlinx.android.synthetic.main.activity_outgoing_invitation.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutgoingInvitationActivity : AppCompatActivity() {

    private var preferenceManager: PreferenceManager? = null
    private var inviterToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outgoing_invitation)

        preferenceManager = PreferenceManager(applicationContext)

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                inviterToken = task.result!!.token
            }
        }

        val meetingType = intent.getStringExtra("type")

        if (meetingType != null) {
            if (meetingType == "video") {
                imageMeetingType.setImageResource(R.drawable.ic_video)
            }
        }

        val user: User? = intent.getSerializableExtra("user") as User

        if (user != null) {
            textFirstChar.text = user.firstName?.substring(0, 1)
            textUserName.text = String.format("%s %s", user.firstName, user.lastName)
            textEmail.text = user.email
        }

        imageStopInvitation.setOnClickListener {
            onBackPressed()
        }

        if (meetingType != null && user != null) {
            initiateMeeting(meetingType, user.token!!)
        }
    }

    private fun initiateMeeting(meetingType: String, receiverToken: String) {
        try {
            val tokens = JSONArray()
            tokens.put(receiverToken)

            val body = JSONObject()
            val data = JSONObject()

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION)
            data.put(Constants.REMOTE_MSG_MEETING_TYPE, meetingType)
            data.put(Constants.KEY_FIRST_NAME, preferenceManager?.getString(Constants.KEY_FIRST_NAME))
            data.put(Constants.KEY_LAST_NAME, preferenceManager?.getString(Constants.KEY_LAST_NAME))
            data.put(Constants.KEY_EMAIL, preferenceManager?.getString(Constants.KEY_EMAIL))
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, inviterToken)

            body.put(Constants.REMOTE_MSG_DATA, data)
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens)

            sendRemoteMessage(body.toString(), Constants.REMOTE_MSG_INVITATION)
        } catch (e: Exception) {
            Toast.makeText(this@OutgoingInvitationActivity, e.message, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun sendRemoteMessage(remoteMessageBody: String, type: String) {
        ApiClient.getClient().create(ApiService::class.java).sendRemoteMessage(Constants.getRemoteMessageHeaders(), remoteMessageBody)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        if (type == Constants.REMOTE_MSG_INVITATION) {
                            Toast.makeText(this@OutgoingInvitationActivity, "Invitation sent successfully", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@OutgoingInvitationActivity, response.message(), Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@OutgoingInvitationActivity, t.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
    }
}