package tech.skot.model

import android.os.Build
import java.util.*

class AndroidDevice:Device {
    override fun getLocaleInfos() = Locale.getDefault().let { Device.Locale(it.language, it.country) }

    override val osVersion = Build.VERSION.SDK_INT.toString()
}
