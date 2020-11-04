package kr.jadekim.ncloud.authenticator

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.util.encodeBase64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class IamAuthenticator(
    private val accessKey: String,
    private val secretKey: String
) : Authenticator(IamAuthenticator) {

    companion object : AuthenticatorMeta {

        override val name: String = "IAM"
    }

    override fun Request.authenticate() {
        val timestamp = System.currentTimeMillis()
        header("x-ncp-apigw-timestamp", timestamp.toString())
        header("x-ncp-iam-access-key", accessKey)
        header("x-ncp-apigw-signature-v2", makeSignature(timestamp))
    }

    private fun Request.makeSignature(timestamp: Long): String {
        val uri = url.toURI()
        val plainSignature = buildString {
            append(method.value)
            append(" ")
            append(uri.rawPath)
            uri.rawQuery?.let {
                append("?$it")
            }
            appendLine()
            append(timestamp)
            appendLine()
            append(accessKey)
        }

        return signMessage(plainSignature)
    }

    private fun signMessage(message: String): String {
        val key = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256").run {
            init(key)
            doFinal(message.toByteArray(Charsets.UTF_8))
        }

        return String(mac.encodeBase64())
    }
}

fun Request.iam() = IamAuthenticator.asTag().attach(this)