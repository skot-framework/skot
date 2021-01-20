package tech.skot.core

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual fun currentTimeMillis() = (NSDate().timeIntervalSince1970 * 1000).toLong()