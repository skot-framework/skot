package tech.skot.model

import java.util.*

class AndroidDevice:Device {
    override fun getLocaleInfos() = Locale.getDefault().let { Device.Locale(it.language, it.country) }

}
