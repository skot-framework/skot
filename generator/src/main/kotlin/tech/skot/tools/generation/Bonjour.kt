package tech.skot.tools.generation

import kotlin.reflect.KClass

fun main(args: Array<String>) {
    println("--------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Bonjour!!!!!!!")
    println("-------- ${args[0]}")
    val startClass = Class.forName(args[0]).kotlin

    startClass.nestedClasses
//
//    println("Start Class : ${startClass.simpleName}")
}