package com.mylektop.videomeeting.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mylektop.videomeeting.R
import com.mylektop.videomeeting.utilities.Constants
import kotlinx.android.synthetic.main.activity_incoming_invitation.*

class IncomingInvitationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_invitation)

        val meetingType = intent.getStringExtra(Constants.REMOTE_MSG_MEETING_TYPE)

        if (meetingType != null) {
            if (meetingType == "video") {
                imageMeetingType.setImageResource(R.drawable.ic_video)
            }
        }

        val firstName = intent.getStringExtra(Constants.KEY_FIRST_NAME)
        val lastName = intent.getStringExtra(Constants.KEY_LAST_NAME)
        val email = intent.getStringExtra(Constants.KEY_EMAIL)

        if (firstName != null) {
            textFirstChar.text = firstName.substring(0, 1)
        }

        textUserName.text = String.format("%s %s", firstName, lastName)
        textEmail.text = email
    }
}