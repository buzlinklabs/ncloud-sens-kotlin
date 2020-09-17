package kr.jadekim.ncloud.authenticator

import com.github.kittinunf.fuel.core.Request

class ApiKeyIamAuthenticator(
    apiKey: String,
    accessKey: String,
    secretKey: String
) : Authenticator(ApiKeyIamAuthenticator) {

    companion object : AuthenticatorTag {

        override val name: String = "API-KEY_IAM"
    }

    private val apiKeyDelegate = ApiKeyAuthenticator(apiKey)
    private val iamDelegate = IamAuthenticator(accessKey, secretKey)

    override fun Request.authenticate() {
        with(apiKeyDelegate) { this@authenticate.authenticate() }
        with(iamDelegate) { this@authenticate.authenticate() }
    }
}

fun Request.apiKeyIam() = tag(ApiKeyIamAuthenticator)