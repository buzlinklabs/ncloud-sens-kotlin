package kr.jadekim.ncloud.authenticator

import com.github.kittinunf.fuel.core.Request

interface AuthenticatorMeta {

    val name: String

    fun asTag() = AuthenticatorTag(this)
}

data class AuthenticatorTag(val meta: AuthenticatorMeta) {
    fun attach(request: Request) = request.tag(this)
}

fun Request.authenticate(authenticator: AuthenticatorMeta) {
    tag(authenticator)
}