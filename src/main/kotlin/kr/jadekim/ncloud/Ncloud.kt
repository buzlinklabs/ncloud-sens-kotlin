package kr.jadekim.ncloud

import com.github.kittinunf.fuel.core.FuelManager
import kr.jadekim.ncloud.credentials.CredentialsProvider

class Ncloud(
    credentialsProvider: CredentialsProvider,
    configureClient: FuelManager.() -> Unit = {}
) {

    companion object {
        fun create(
            credentialsProvider: CredentialsProvider,
            configureClient: FuelManager.() -> Unit = {}
        ) = Ncloud(credentialsProvider, configureClient)
    }

    private val credentials = credentialsProvider.resolveCredentials()
    val clientFactory = NcloudClientFactory(credentials, configureClient)
}