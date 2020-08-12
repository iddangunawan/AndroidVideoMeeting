package com.mylektop.videomeeting.models

import java.io.Serializable

/**
 * Created by iddangunawan on 30/07/20
 */
class User : Serializable {
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var token: String? = null
}