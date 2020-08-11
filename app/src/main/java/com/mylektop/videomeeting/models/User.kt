package com.mylektop.videomeeting.models

import java.io.Serializable

/**
 * Created by iddangunawan on 30/07/20
 */
class User : Serializable {
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var token: String = ""
}