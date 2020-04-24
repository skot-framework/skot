package tech.skot.model

expect fun decodeBase64(str:String):String

expect fun encodeBase64(str: String):String

expect fun hashSHA256(str:String):String

expect fun aes128encrypt(textToEncrypt:String, secret:String, initializationVector:String):String