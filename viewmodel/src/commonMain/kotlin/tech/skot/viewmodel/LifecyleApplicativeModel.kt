package tech.skot.viewmodel

import tech.skot.contract.modelcontract.Poker

interface LifecyleApplicativeModel {
    val resumed: Boolean
    val statusChangedPoker: Poker
}