package com.mylektop.videomeeting.lisneters

import com.mylektop.videomeeting.models.User

/**
 * Created by iddangunawan on 11/08/20
 */
interface UsersListener {

    fun initiateAudioMeeting(user: User)

    fun initiateVideoMeeting(user: User)

}