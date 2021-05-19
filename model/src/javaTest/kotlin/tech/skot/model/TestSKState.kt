package tech.skot.model

import kotlinx.coroutines.runBlocking
import org.junit.Test

class TestSKState {



    @Test
    fun testSKState() {
        class SubState(val name:String):SKState() {
            override suspend fun restore() {

            }
        }
        val testName = "NAME"
        class RootTestState:SKState() {

            override suspend fun restore() {
                subState1.setValue(SubState(testName))
            }

            val subState1 = SKSubState<SubState>()

        }

        runBlocking {
            val root = RootTestState()

            assert(root.subState1()?.name == testName)
        }

    }

}