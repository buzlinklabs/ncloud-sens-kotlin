package kr.jadekim.ncloud.sens

import com.github.kittinunf.fuel.core.await
import com.github.kittinunf.fuel.core.awaitUnit
import com.github.kittinunf.fuel.gson.gsonDeserializer
import com.github.kittinunf.fuel.gson.jsonBody
import com.google.gson.ExclusionStrategy
import com.google.gson.GsonBuilder
import kr.jadekim.ncloud.Ncloud
import kr.jadekim.ncloud.NcloudClientFactory
import kr.jadekim.ncloud.authenticator.iam
import kr.jadekim.ncloud.sens.enumeration.Country
import kr.jadekim.ncloud.sens.enumeration.SmsContentType
import kr.jadekim.ncloud.sens.enumeration.SmsType
import kr.jadekim.ncloud.sens.protocol.*

class NcloudSens(clientFactory: NcloudClientFactory, serviceId: String) {

    companion object {
        const val MAX_BULK_SEND_SIZE = 1000
    }

    private val fuel = clientFactory.create(
        {
            basePath = "https://sens.apigw.ntruss.com/sms/v2/services/$serviceId"

            addRequestInterceptor { next ->
                {
                    it.iam()
                    next(it)
                }
            }
        }
    )

    private val gson = GsonBuilder()
        .registerTypeAdapter(SmsType::class.java, SmsType.GsonAdapter)
        .registerTypeAdapter(SmsContentType::class.java, SmsContentType.GsonAdapter)
        .registerTypeAdapter(Country::class.java, Country.GsonAdapter)
        .registerTypeAdapter(EmmaResultCode::class.java, EmmaResultCode.GsonAdapter)
        .create()

    suspend fun sendSms(message: SendSms.Request): SendSms.Response =
        fuel.post("/messages")
            .jsonBody(message, gson)
            .await(gsonDeserializer(gson))

    suspend fun getSendSmsRequest(requestId: String): GetSendSmsRequest.Response =
        fuel.get("/messages", mapOf("requestId" to requestId).toList())
            .await(gsonDeserializer(gson))

    suspend fun getSendSmsResult(messageId: String): GetSendSmsResult.Response =
        fuel.get("/messages/$messageId")
            .await(gsonDeserializer(gson))

    suspend fun getSmsReservationStatus(reserveId: String): GetSmsReservationStatus.Response =
        fuel.get("/reservations/$reserveId/reserve-status")
            .await(gsonDeserializer(gson))

    suspend fun cancelSmsReservation(reserveId: String) {
        fuel.delete("/reservations/$reserveId")
            .awaitUnit()
    }

    suspend fun cancelScheduledSms(scheduleCode: String, messageId: String) {
        fuel.delete("/schedules/$scheduleCode/messages/$messageId")
            .awaitUnit()
    }
}

fun Ncloud.sens(serviceId: String) = NcloudSens(clientFactory, serviceId)