package tech.skkot.model

import androidx.test.platform.app.InstrumentationRegistry
import tech.skot.model.AndroidSKPersistor

fun testPersistor(name:String = "testPersistor") = AndroidSKPersistor(InstrumentationRegistry.getInstrumentation().context, name)