package com.sayan.chatnow

class UserModel {
    var name:String? = null
    var password:String? = null
    var uid:String? = null

    constructor(){}

    constructor(name: String?, password: String?, uid: String?) {
        this.name = name
        this.password = password
        this.uid = uid
    }
}