package tech.skot.model

import androidx.test.platform.app.InstrumentationRegistry

fun testPersistor(name: String = "testPersistor") =
    AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, name)