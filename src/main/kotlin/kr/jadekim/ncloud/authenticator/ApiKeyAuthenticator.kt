package kr.jadekim.ncloud.authenticator

import com.github.kittinunf.fuel.core.Request

class ApiKeyAuthenticator(private val apiKey: String) : Authenticator(ApiKeyAuthenticator) {

    companion object : AuthenticatorMeta {

        override val name: String = "API-KEY"
    }

    override fun Request.authenticate() {
        header("x-ncp-apigw-api-key", apiKey)
    }
}

fun Request.apiKey() = ApiKeyAuthenticator.asTag().attach(this)