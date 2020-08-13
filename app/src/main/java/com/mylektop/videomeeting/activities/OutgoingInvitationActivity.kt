package com.mylektop.videomeeting.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mylektop.videomeeting.R
import com.mylektop.videomeeting.models.User
import kotlinx.android.synthetic.main.activity_outgoing_invitation.*

class OutgoingInvitationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outgoing_invitation)

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
    }
}