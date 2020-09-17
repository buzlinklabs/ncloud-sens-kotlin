package kr.jadekim.ncloud.authenticator

import com.github.kittinunf.fuel.core.Request

interface AuthenticatorTag {

    val name: String
}

fun Request.authenticate(authenticator: AuthenticatorTag) {
    tag(authenticator)
}