package kr.jadekim.ncloud.sens.enumeration

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

enum class SmsType(
    val maxContentByteSize: Int,
    val maxSubjectByteSize: Int,
    val maxAttachmentNameByteSize: Int,
    val maxAttachmentByteSize: Int,
    val usableSubject: Boolean,
    val usableAttachment: Boolean
) {
    SMS(
        80,
        0,
        0,
        0,
        false,
        false
    ),
    LMS(
        2000,
        80,
        0,
        0,
        true,
        false
    ),
    MMS(
        2000,
        80,
        80,
        300 * 1024,
        true,
        true
    );

    object GsonAdapter : TypeAdapter<SmsType>() {

        override fun write(out: JsonWriter, value: SmsType?) {
            out.value(value?.name?.toUpperCase())
        }

        override fun read(input: JsonReader): SmsType = values().first { it.name.equals(input.nextString(), true) }
    }
}