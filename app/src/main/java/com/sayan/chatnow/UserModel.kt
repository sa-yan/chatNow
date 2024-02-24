package com.sayan.chatnow

import android.net.Uri

class UserModel {
    var name:String? = null
    var password:String? = null
    var uid:String? = null
    var pp:String? = null

    constructor(){}

    constructor(name: String?, password: String?, uid: String?, pp:String?) {
        this.name = name
        this.password = password
        this.uid = uid
        this.pp=pp
    }
}