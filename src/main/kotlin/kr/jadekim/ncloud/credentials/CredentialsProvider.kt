package kr.jadekim.ncloud.credentials

interface CredentialsProvider {

    fun resolveCredentials(): Credentials
}