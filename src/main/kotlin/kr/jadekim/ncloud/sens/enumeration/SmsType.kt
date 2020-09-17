package kr.jadekim.ncloud.sens.enumeration

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

enum class SmsType {
    SMS,
    LMS,
    MMS;

    object GsonAdapter : TypeAdapter<SmsType>() {

        override fun write(out: JsonWriter, value: SmsType?) {
            out.value(value?.name?.toUpperCase())
        }

        override fun read(input: JsonReader): SmsType = values().first { it.name.equals(input.nextString(), true) }
    }
}