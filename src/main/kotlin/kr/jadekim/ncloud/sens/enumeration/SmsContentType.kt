package kr.jadekim.ncloud.sens.enumeration

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

enum class SmsContentType(val apiValue: String) {
    NORMAL("COMM"),
    AD("AD");

    object GsonAdapter : TypeAdapter<SmsContentType>() {

        override fun write(out: JsonWriter, value: SmsContentType?) {
            out.value(value?.apiValue)
        }

        override fun read(input: JsonReader): SmsContentType = values().first { it.apiValue == input.nextString() }
    }
}