package co.infinum.thrifty.kotlin

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
data class BrokenPhone(@JvmField @ThriftField(fieldId = 1, isOptional = true) val number: String?, @JvmField @ThriftField(fieldId = 2, isOptional = true) val voicemail: Boolean?) : Struct {
    override fun write(protocol: Protocol) {
        // intentionally broken
    }
}
