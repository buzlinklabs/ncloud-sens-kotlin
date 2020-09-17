package kr.jadekim.ncloud

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.RequestFactory
import kr.jadekim.ncloud.authenticator.ApiKeyAuthenticator
import kr.jadekim.ncloud.authenticator.ApiKeyIamAuthenticator
import kr.jadekim.ncloud.authenticator.IamAuthenticator
import kr.jadekim.ncloud.credentials.Credentials

class NcloudClientFactory (
    private val credentials: Credentials,
    private val globalConfigureClient: FuelManager.() -> Unit = {}
) {

    fun create(configure: FuelManager.() -> Unit = {}): RequestFactory.Convenience = FuelManager().apply {
        if (!credentials.apiKey.isNullOrBlank()) {
            addRequestInterceptor(ApiKeyAuthenticator(credentials.apiKey))
        }

        if (!(credentials.accessKey.isNullOrBlank() || credentials.secretKey.isNullOrBlank())) {
            addRequestInterceptor(IamAuthenticator(credentials.accessKey, credentials.secretKey))
        }

        if (!(credentials.apiKey.isNullOrBlank()
                    || credentials.accessKey.isNullOrBlank()
                    || credentials.secretKey.isNullOrBlank())
        ) {
            addRequestInterceptor(
                ApiKeyIamAuthenticator(
                    credentials.apiKey,
                    credentials.accessKey,
                    credentials.secretKey
                )
            )
        }

        globalConfigureClient()
        configure()
    }
}