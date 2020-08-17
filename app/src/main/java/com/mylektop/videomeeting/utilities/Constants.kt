package com.mylektop.videomeeting.utilities

/**
 * Created by iddangunawan on 27/07/20
 */
object Constants {
    const val KEY_COLLECTION_USERS: String = "users"
    const val KEY_FIRST_NAME: String = "first_name"
    const val KEY_LAST_NAME: String = "last_name"
    const val KEY_EMAIL: String = "email"
    const val KEY_PASSWORD: String = "password"
    const val KEY_USER_ID: String = "user_id"
    const val KEY_FCM_TOKEN: String = "fcm_token"

    const val KEY_PREFERENCE_NAME: String = "videoMeetingPreference"
    const val KEY_IS_SIGNED_IN: String = "isSignedIn"

    const val REMOTE_MSG_AUTHORIZATION: String = "Authorization"
    const val REMOTE_MSG_CONTENT_TYPE: String = "Content-Type"

    const val REMOTE_MSG_TYPE: String = "type"
    const val REMOTE_MSG_INVITATION: String = "invitation"
    const val REMOTE_MSG_MEETING_TYPE: String = "meetingType"
    const val REMOTE_MSG_INVITER_TOKEN: String = "inviterToken"
    const val REMOTE_MSG_DATA: String = "data"
    const val REMOTE_MSG_REGISTRATION_IDS: String = "registration_ids"

    fun getRemoteMessageHeaders(): HashMap<String, String> {
        val headers: HashMap<String, String> = HashMap()
        headers[REMOTE_MSG_AUTHORIZATION] = "key=AAAAWtNdij0:APA91bH13VJRlA42sj3YBw7iF6VI_en1B0lF9xyuTjUoFnmpALtiL83Nzmu_3Ic2giXiH82sJ0Lb_YoYKhh3ch3WBvqz1yaHVKX1VyA85Vv20GUGJdZQpjRLQkTwQT0ofwL9aU3jy4N1"
        headers[REMOTE_MSG_CONTENT_TYPE] = "application/json"

        return headers
    }
}