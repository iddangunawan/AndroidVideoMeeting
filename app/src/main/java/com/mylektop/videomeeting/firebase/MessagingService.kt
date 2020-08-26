package com.mylektop.videomeeting.firebase

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mylektop.videomeeting.activities.IncomingInvitationActivity
import com.mylektop.videomeeting.utilities.Constants

/**
 * Created by iddangunawan on 27/07/20
 */
class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Token : $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val type = remoteMessage.data[Constants.REMOTE_MSG_TYPE]

        if (type != null) {
            if (type == Constants.REMOTE_MSG_INVITATION) {
                val intent = Intent(applicationContext, IncomingInvitationActivity::class.java)
                intent.putExtra(
                    Constants.REMOTE_MSG_MEETING_TYPE,
                    remoteMessage.data[Constants.REMOTE_MSG_MEETING_TYPE]
                )
                intent.putExtra(
                    Constants.KEY_FIRST_NAME,
                    remoteMessage.data[Constants.KEY_FIRST_NAME]
                )
                intent.putExtra(
                    Constants.KEY_LAST_NAME,
                    remoteMessage.data[Constants.KEY_LAST_NAME]
                )
                intent.putExtra(
                    Constants.KEY_EMAIL,
                    remoteMessage.data[Constants.KEY_EMAIL]
                )
                intent.putExtra(
                    Constants.REMOTE_MSG_INVITER_TOKEN,
                    remoteMessage.data[Constants.REMOTE_MSG_INVITER_TOKEN]
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else if (type == Constants.REMOTE_MSG_INVITATION_RESPONSE) {
                val intent = Intent(Constants.REMOTE_MSG_INVITATION_RESPONSE)
                intent.putExtra(
                    Constants.REMOTE_MSG_INVITATION_RESPONSE,
                    remoteMessage.data[Constants.REMOTE_MSG_INVITATION_RESPONSE]
                )
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
        }
    }
}