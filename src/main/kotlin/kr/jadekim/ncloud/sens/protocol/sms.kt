package kr.jadekim.ncloud.sens.protocol

import com.github.kittinunf.fuel.util.encodeBase64
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kr.jadekim.ncloud.sens.enumeration.*

class SendSms {
    data class Request(
        val type: SmsType,
        val contentType: SmsContentType,
        @SerializedName("countryCode") val county: Country,
        val from: String,
        val subject: String?,
        val content: String,
        val messages: List<Message>?,
        val files: List<Attachment>?,
        val reserveTime: String?,
        val reserveTimeZone: String?,
        val scheduleCode: String?
    ) {
        data class Message(
            val to: String,
            val subject: String?,
            val content: String?
        )

        data class Attachment(
            val name: String?,
            @SerializedName("body") val encodedBody: String?
        ) {

            constructor(name: String?, body: ByteArray?) : this(name, body?.let { String(it.encodeBase64()) })
        }
    }

    data class Response(
        val requestId: String,
        val requestTime: String,
        val statusCode: String,
        val statusName: String
    )
}

class GetSendSmsRequest {
    data class Response(
        val requestId: String,
        val statusCode: String,
        val statusName: String,
        val messages: List<Message>
    ) {
        data class Message(
            val messageId: String,
            val requestTime: String,
            val contentType: SmsContentType,
            @SerializedName("countryCode") val county: Country,
            val from: String,
            val to: String
        )
    }
}

class GetSendSmsResult {
    data class Response(
        val statusCode: String,
        val statusName: String,
        val messages: List<Message>
    ) {
        data class Message(
            val requestTime: String,
            val contentType: SmsContentType,
            val content: String,
            @SerializedName("countryCode") val country: String,
            val from: String,
            val to: String,
            val status: EmmaResultCode,
            val statusCode: EmmaResultCode,
            val statusMessage: String,
            val completeTime: String,
            val telcoCode: String,
            val files: List<Attachment>?
        )

        data class Attachment(
            val name: String?
        )
    }
}

class GetSmsReservationStatus {
    data class Response(
        val reserveId: String,
        val reserveTime: String,
        val reserveTimeZone: String,
        val reserveStatus: SmsReserveStatus
    )
}

class EmmaResultCode(@Expose val code: String) {

    val isSuccess = code == "0"

    val description by lazy {
        when(code) {
            //IB G/W Report Code : 이통사 전송 후 받은 결과코드
            "0" -> "성공"
            "2000" -> "전송 시간 초과"
            "2001" -> "전송 실패 (무선망단)"
            "2002" -> "전송 실패 (무선망 -> 단말기단)"
            "2003" -> "단말기 전원 꺼짐"
            "2004" -> "단말기 메시지 버퍼 풀"
            "2005" -> "음영지역"
            "2006" -> "메시지 삭제됨"
            "2007" -> "일시적인 단말 문제"
            "3000" -> "전송할 수 없음"
            "3001" -> "가입자 없음"
            "3002" -> "성인 인증 실패"
            "3003" -> "수신번호 형식 오류"
            "3004" -> "단말기 서비스 일시 정지"
            "3005" -> "단말기 호 처리 상태"
            "3006" -> "착신 거절"
            "3007" -> "Callback URL 을 받을 수 없는 폰"
            "3008" -> "기타 단말기 문제"
            "3009" -> "메시지 형식 오류"
            "3010" -> "MMS 미지원 단말"
            "3011" -> "서버 오류"
            "3012" -> "스팸"
            "3013" -> "서비스 거부"
            "3014" -> "기타"
            "3015" -> "전송 경로 없음"
            "3016" -> "첨부파일 사이즈 제한 실패"
            "3017" -> "발신번호 변작 방지 세칙 위반"
            "3018" -> "발신번호 변작 방지 서비스에 가입된 휴대폰 개인가입자 번호"
            "3019" -> "KISA 또는 미래부에서 모든 고객사에 대하여 차단 처리 요청한 발신번호"
            "3022" -> "Charset Conversion Error"
            "3023" -> "발신번호 사전등록제를 통해 등록되지 않은 번호"

            //IB G/W Response Code : Infobank G/W가 메시지 수신 후 주는 결과코드
            "1001" -> "Server Busy (RS 내부 저장 Queue Full)"
            "1002" -> "수신번호 형식 오류"
            "1003" -> "회신번호 형식 오류"
            "1004" -> "SPAM"
            "1005" -> "사용 건수 초과"
            "1006" -> "첨부 파일 없음"
            "1007" -> "첨부 파일 있음"
            "1008" -> "첨부 파일 저장 실패"
            "1009" -> "CLIENT_MSG_KEY 없음"
            "1010" -> "CONTENT 없음"
            "1011" -> "CALLBACK 없음"
            "1012" -> "RECIPIENT_INFO 없음"
            "1013" -> "SUBJECT 없음"
            "1014" -> "첨부 파일 KEY 없음"
            "1015" -> "첨부 파일 NAME 없음"
            "1016" -> "첨부 파일 크기 없음"
            "1017" -> "첨부 파일 Content 없음"
            "1018" -> "전송 권한 없음"
            "1019" -> "TTL 초과"
            "1020" -> "charset conversion error"

            //IB EMMA : EMMA가 메시지 전송 요청에 대해 처리한 오류 코드
            "E900" -> "Invalid-IB 전송키가 없는 경우"
            "E901" -> "수신번호가 없는 경우"
            "E902" -> "동보인 경우) 수신번소순번이 없는 경우"
            "E903" -> "제목 없는 경우"
            "E904" -> "메시지가 없는 경우"
            "E905" -> "회신번호가 없는 경우"
            "E906" -> "메시지키가 없는 경우"
            "E907" -> "동보 여부가 없는 경우"
            "E908" -> "서비스 타입이 없는 경우"
            "E909" -> "전송 요청 시각이 없는 경우"
            "E910" -> "TTL 타임이 없는 경우"
            "E911" -> "서비스 타입이 MMS MT인 경우, 첨부파일 확장자가 없는 경우"
            "E912" -> "서비스 타입이 MMS MT인 경우, attach_file 폴더에 첨부파일이 없는 경우"
            "E913" -> "서비스 타입이 MMS MT인 경우, 첨부파일 사이즈가 0인 경우"
            "E914" -> "서비스 타입이 MMS MT인 경우, 메시지 테이블에는 파일그룹키가 있는데 파일 테이블에 데이터가 없는 경우"
            "E915" -> "중복메시지"
            "E916" -> "인증서버 차단번호"
            "E917" -> "고객DB 차단번호"
            "E918" -> "USER CALLBACK FAIL"
            "E919" -> "발송 제한 시간인 경우, 메시지 재발송 처리가 금지된 경우"
            "E920" -> "서비스 타입이 LMS MT인 경우, 메시지 테이블에 파일그룹키가 있는 경우"
            "E921" -> "서비스 타입이 LMS MT인 경우, 메시지 테이블에 파일그룹키가 없는 경우"
            "E922" -> "동보단어 제약문자 사용 오류"
            "E999" -> "기타 오류"
            else -> "알 수 없는 오류"
        }
    }
}