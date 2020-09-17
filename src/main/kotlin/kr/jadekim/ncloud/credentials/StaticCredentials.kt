package kr.jadekim.ncloud.credentials

class StaticCredentials(
    private val apiKey: String?,
    private val accessKey: String?,
    private val secretKey: String?
) : CredentialsProvider {

    override fun resolveCredentials(): Credentials = Credentials(apiKey, accessKey, secretKey)
}