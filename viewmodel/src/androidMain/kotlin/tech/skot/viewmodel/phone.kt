package tech.skot.viewmodel

import android.telephony.PhoneNumberUtils

actual fun String?.formatPhoneNumber():String? {
    return this?.let { PhoneNumberUtils.formatNumber(it, "FR") ?: this }
}