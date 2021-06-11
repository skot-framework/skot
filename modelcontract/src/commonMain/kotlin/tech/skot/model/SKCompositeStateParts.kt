package tech.skot.model

@Target(AnnotationTarget.PROPERTY)
annotation class SKCompositeStateParts(val composingStatesNames:Array<String>)
