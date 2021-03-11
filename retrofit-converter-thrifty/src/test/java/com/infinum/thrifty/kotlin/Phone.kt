package com.infinum.thrifty.kotlin

import com.microsoft.thrifty.Adapter
import com.microsoft.thrifty.Struct
import com.microsoft.thrifty.StructBuilder
import com.microsoft.thrifty.TType
import com.microsoft.thrifty.ThriftField
import com.microsoft.thrifty.protocol.Protocol
import com.microsoft.thrifty.util.ProtocolUtil
import javax.annotation.Generated

@Generated(
    value = ["com.microsoft.thrifty.kgen.KotlinCodeGenerator"],
    comments = "https://github.com/microsoft/thrifty"
)
data class Phone(@JvmField @ThriftField(fieldId = 1, isOptional = true) val number: String?, @JvmField @ThriftField(fieldId = 2, isOptional = true) val voicemail: Boolean?) : Struct {
    override fun write(protocol: Protocol) {
        ADAPTER.write(protocol, this)
    }

    class Builder : StructBuilder<Phone> {
        private var number: String? = null

        private var voicemail: Boolean? = null

        constructor() {
            this.number = null
            this.voicemail = null
        }

        constructor(source: Phone) {
            this.number = source.number
            this.voicemail = source.voicemail
        }

        fun number(number: String?) = apply { this.number = number }

        fun voicemail(voicemail: Boolean?) = apply { this.voicemail = voicemail }

        override fun build(): Phone = Phone(number = this.number, voicemail = this.voicemail)
        override fun reset() {
            this.number = null
            this.voicemail = null
        }
    }

    private class PhoneAdapter : Adapter<Phone, Builder> {
        override fun read(protocol: Protocol) = read(protocol, Builder())

        override fun read(protocol: Protocol, builder: Builder): Phone {
            protocol.readStructBegin()
            while (true) {
                val fieldMeta = protocol.readFieldBegin()
                if (fieldMeta.typeId == TType.STOP) {
                    break
                }
                when (fieldMeta.fieldId.toInt()) {
                    1 -> {
                        if (fieldMeta.typeId == TType.STRING) {
                            val number = protocol.readString()
                            builder.number(number)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    2 -> {
                        if (fieldMeta.typeId == TType.BOOL) {
                            val voicemail = protocol.readBool()
                            builder.voicemail(voicemail)
                        } else {
                            ProtocolUtil.skip(protocol, fieldMeta.typeId)
                        }
                    }
                    else -> ProtocolUtil.skip(protocol, fieldMeta.typeId)
                }
                protocol.readFieldEnd()
            }
            protocol.readStructEnd()
            return builder.build()
        }

        override fun write(protocol: Protocol, struct: Phone) {
            protocol.writeStructBegin("Phone")
            if (struct.number != null) {
                protocol.writeFieldBegin("number", 1, TType.STRING)
                protocol.writeString(struct.number)
                protocol.writeFieldEnd()
            }
            if (struct.voicemail != null) {
                protocol.writeFieldBegin("voicemail", 2, TType.BOOL)
                protocol.writeBool(struct.voicemail)
                protocol.writeFieldEnd()
            }
            protocol.writeFieldStop()
            protocol.writeStructEnd()
        }
    }

    companion object {
        @JvmField
        val ADAPTER: Adapter<Phone, Builder> = PhoneAdapter()
    }
}
