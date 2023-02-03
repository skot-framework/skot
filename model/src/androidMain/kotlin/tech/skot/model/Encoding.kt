package tech.skot.model

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

actual fun decodeBase64(str: String, urlSafe: Boolean): String {
    return String(
        Base64.decode(
            str, if (urlSafe) {
                Base64.URL_SAFE
            } else {
                Base64.DEFAULT
            }
        )
    )
}

actual fun encodeBase64(str: String) =
    Base64.encodeToString(str.toByteArray(), Base64.NO_WRAP or Base64.URL_SAFE)

actual fun hashSHA256(str: String) = hashString("SHA-256", str)


private fun hashString(type: String, input: String): String {
    val HEX_CHARS = "0123456789ABCDEF"
    val bytes = MessageDigest
        .getInstance(type)
        .digest(input.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
        val i = it.toInt()
        result.append(HEX_CHARS[i shr 4 and 0x0f])
        result.append(HEX_CHARS[i and 0x0f])
    }

    return result.toString()
}


actual fun aes128encrypt(
    textToEncrypt: String,
    secret: String,
    initializationVector: String,
): String {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
    val keySpec = SecretKeySpec(secret.toByteArray().copyOf(32), "AES")
    val ivSpec = IvParameterSpec(initializationVector.toByteArray())
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
    return Base64.encodeToString(cipher.doFinal(textToEncrypt.toByteArray()), Base64.NO_WRAP)
}