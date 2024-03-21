package com.example.healthzensignuplogin

class HelperClass(
    var name: String = "",
    var email: String = "",
    var username: String = "",
    var password: String = ""
) {
    // Secondary constructor
    constructor() : this("", "", "", "")
}
