package tech.skot.viewmodel

import tech.skot.core.Poker


interface LifecyleApplicativeModel {
    val resumed: Boolean
    val statusChangedPoker: Poker
}